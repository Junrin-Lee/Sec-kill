package cn.lrl.generator.controller;

import cn.lrl.annotation.AccessLimited;
import cn.lrl.exception.GoodsException;
import cn.lrl.generator.pojo.Order;
import cn.lrl.generator.service.CommonService;
import cn.lrl.generator.service.IOrderService;
import cn.lrl.po.MQMessage;
import cn.lrl.rabbitMQ.MQSender;
import cn.lrl.vo.*;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 订单管理
 * </p>
 *
 * @author lrl
 * @since 2022-01-13
 */
@RestController
@RequestMapping("/order")
@CacheConfig(cacheNames = "dataCache")
@Slf4j
public class OrderController {

    @Autowired
    IOrderService orderService;

    @Autowired
    CommonService commonService;

    @Autowired
    MQSender sender;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 异步下单业务
     *
     * @param goodsId 商品id
     * @param vo      用户登录信息
     * @return 模版对象
     */
    @PostMapping("/doOrder")
    public RespMsgVo doOrder(@RequestAttribute("login_vo") LoginVo vo, @NotNull Long goodsId, @NotNull Integer stockCount) {

        // 判断是否存在刷单行为
        boolean isGoodCreate = commonService.isGoodCreate(Long.parseLong(vo.getMobile()), goodsId);
        if (isGoodCreate) {
            throw new GoodsException(RespMsgEnum.BUYYOURSELF_ERROR);
        }

        // mq异步下单
        MQMessage MQMessage = new MQMessage()
                .setUserId(Long.parseLong(vo.getMobile()))
                .setGoodsId(goodsId)
                .setStockCount(stockCount);
        sender.sendGoodsMessage(JSON.toJSONString(MQMessage));

        return RespMsgVo.success();
    }

    /**
     * 获取订单信息
     *
     * @param orderId 订单号
     * @return RespMsgVo
     */
    @GetMapping("/toDetail")
    public RespMsgVo getOrderHtml(@NotNull Long orderId) {
        OrderDetailVo orderDetailVo = orderService.getDetail(orderId);
        return RespMsgVo.success(orderDetailVo);
    }

    /**
     * 获取订单结果
     *
     * @param vo      用户登录信息
     * @param goodsId 商品id
     * @return RespMsgVo
     */
    @GetMapping("/result")
    public RespMsgVo getResult(@RequestAttribute("login_vo") LoginVo vo, @NotNull Long goodsId) {
        long orderId = orderService.getResult(Long.parseLong(vo.getMobile()), goodsId);
        return RespMsgVo.success(orderId);
    }

    /**
     * 获取个人订单信息
     *
     * @param vo      用户登录信息
     * @param orderId 搜索的订单号（这里不用Long是因为String默认赋值""，在查询条件为空的时候可以走模糊查询查出所有结果，有利于缓存构建）
     * @return RespMsgVo
     */
    @AccessLimited(second = 20, maxCount = 5)
    @GetMapping("/myOrderMsg")
    public RespMsgVo myOrderMsg(@RequestAttribute(name = "login_vo") LoginVo vo,
                                @RequestParam(name = "id", required = false) String orderId) {
        List<OrderVo> list = null;
        if (orderId == null) {
            // 未进行搜索的前走查询全部，用缓存重复使用
            list = orderService.getOrderMsgByUid(Long.parseLong(vo.getMobile()));
        } else {
            // 进行查询后，之后每次查询都走模糊查询
            list = orderService.getOrderMsgByUid(Long.parseLong(vo.getMobile()), orderId);
        }

        return RespMsgVo.success(RespMsgEnum.DATA_SUCCESS, list);
    }

    /**
     * 获取用户订单信息，同时实现搜索功能
     *
     * @param vo      用户登录信息
     * @param orderId 搜索订单号
     * @return RespMsgVo
     */
    @AccessLimited(second = 20, maxCount = 5)
    @GetMapping("/consumerOrderMsg")
    public RespMsgVo consumerOrderMsg(@RequestAttribute(name = "login_vo") LoginVo vo,
                                      @RequestParam(name = "id", required = false) String orderId) {
        List<OrderVo> list = null;
        if (orderId == null) {
            list = orderService.getConsumerOrderMsgByUid(Long.parseLong(vo.getMobile()));
        } else {
            list = orderService.getConsumerOrderMsgByUid(Long.parseLong(vo.getMobile()), orderId);
        }

        return RespMsgVo.success(RespMsgEnum.DATA_SUCCESS, list);
    }

    /**
     * 修改订单信息
     *
     * @param vo      用户登录信息
     * @param orderId 订单号
     * @param addr    联系地址
     * @return RespMsgVo
     */
    @Caching(evict = {
            @CacheEvict(key = "#p0.mobile+'::orderMsg'"),
            @CacheEvict(key = "#p0.mobile+'::consumerOrderMsg'")
    })
    @PutMapping("/updateUserAddr")
    public RespMsgVo updateUserAddr(@RequestAttribute(name = "login_vo") LoginVo vo,
                                    @RequestParam(name = "id") Long orderId, @NotNull String addr) {
        orderService.updateUserAddr(orderId, addr);
        return RespMsgVo.success(RespMsgEnum.SUCCESS);

    }

    /**
     * 取消订单
     *
     * @param vo            用户登录信息
     * @param delOrdersJson 前端数据JSON
     * @return RespMsgVo
     */
    @Caching(evict = {
            @CacheEvict(key = "#p0.mobile+'::orderMsg'"),
            @CacheEvict(key = "#p0.mobile+'::consumerOrderMsg'")
    })
    @DeleteMapping("/delOrders")
    public RespMsgVo delOrders(@RequestAttribute(name = "login_vo") LoginVo vo, @RequestBody String delOrdersJson) {

        // 解析JSON
        List<Order> orders = JSON.parseArray(delOrdersJson, Order.class);
        orderService.delOrders(orders);

        return RespMsgVo.success();
    }

}

