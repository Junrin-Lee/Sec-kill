package cn.pointer.lrl.generator.service.imp;

import cn.pointer.lrl.exception.GoodsException;
import cn.pointer.lrl.exception.OrderException;
import cn.pointer.lrl.exception.SeckillException;
import cn.pointer.lrl.exception.UserException;
import cn.pointer.lrl.generator.mapper.OrderMapper;
import cn.pointer.lrl.generator.pojo.Goods;
import cn.pointer.lrl.generator.pojo.Order;
import cn.pointer.lrl.generator.service.IGoodsService;
import cn.pointer.lrl.generator.service.IOrderService;
import cn.pointer.lrl.utils.RedisUtil;
import cn.pointer.lrl.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lrl
 * @since 2022-01-13
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    IGoodsService goodsService;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 订单生成
     *
     * @param userId  用户id
     * @param goodsVo 商品id
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "dataCache", key = "#p0+'::orderMsg'", beforeInvocation = false),// 清空该用户的订单缓存
            @CacheEvict(cacheNames = "pageCache", key = "'goodsList'", beforeInvocation = false),// 清除普通商品页面缓存
    })
    public void doGoodsOrder(Long userId, GoodsVo goodsVo) {

        // 修改商品库存
        boolean update = goodsService.update(new UpdateWrapper<Goods>()
                .setSql("goods_stock=" + "goods_stock-1")
                .eq("id", goodsVo.getId())
                .gt("goods_stock", 0)
        );

        if (!update) {
            redisUtil.set("isStockEmpty:" + goodsVo.getId(), 0);
            throw new SeckillException(RespMsgEnum.EMPTY_SECKILL_STOCK);
        }

        // 生成订单
        Order order = new Order()
                .setUserId(userId)
                .setGoodsId(goodsVo.getId())
                .setGoodsName(goodsVo.getGoodsName())
                .setGoodsCount(goodsVo.getStockCount())
                .setGoodsPrice(goodsVo.getGoodsPrice())
                .setOrderChannel(1)
                .setStatus(0)
                .setType(0)
                .setCreateTime(new Date());

        // 数据库下单
        orderMapper.insert(order);
    }


    /**
     * 订单详情
     *
     * @param orderId 订单id
     * @return 订单前端模版返回
     */
    @Override
    @Transactional
    public OrderDetailVo getDetail(Long orderId) {
        if (orderId == null) {
            throw new OrderException(RespMsgEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodById(order.getGoodsId());

        return new OrderDetailVo(order, goodsVo);
    }

    @Override
    @Cacheable(cacheNames = "dataCache", key = "#p0+'::orderMsg'")
    public List<OrderVo> getOrderMsgByUid(Long userId) {
        return orderMapper.selectOrderVoByUid(userId);
    }

    @Override
    @CachePut(cacheNames = "dataCache", key = "#p0+'::orderMsg'")
    public List<OrderVo> getOrderMsgByUid(Long userId, String orderId) {
        return orderMapper.selectOrderVoLikeOrderId(userId, orderId);
    }

    @Override
    @Cacheable(cacheNames = "dataCache", key = "#p0+'::consumerOrderMsg'")
    public List<OrderVo> getConsumerOrderMsgByUid(Long userId) {
        return orderMapper.selectConsumerOrderVoByUid(userId);
    }

    @Override
    @CachePut(cacheNames = "dataCache", key = "#p0+'::consumerOrderMsg'")
    public List<OrderVo> getConsumerOrderMsgByUid(Long userId, String orderId) {
        return orderMapper.selectConsumerOrderVoLikeOrderId(userId, orderId);
    }

    @Override
    public void updateUserAddr(Long orderId, String addr) {
        try {
            orderMapper.update(new Order(), new UpdateWrapper<Order>()
                    .set("delivery_addr", addr)
                    .eq("id", orderId)
            );
        } catch (Exception e) {
            throw new UserException(RespMsgEnum.USER_UPDATE_ADDR_ERROR);
        }
    }

    @Override
    public long getResult(Long userId, Long goodsId) {
        // 获取订单
        Order order = orderMapper.selectOne(new QueryWrapper<Order>()
                .eq("user_id", userId)
                .eq("goods_id", goodsId)
                .eq("type", 0)
        );

        if (order != null) {// 订单存在
            return order.getId();
        } else {// 这次请求没有捕获到订单信息
            return 0L;
        }
    }

    @Override
    public void delOrders(List<Order> orders) {
        try {
            for (Order order : orders) {
                orderMapper.deleteById(order.getId());
            }
        } catch (Exception e) {
            throw new GoodsException(RespMsgEnum.GOODS_DELETE_ERROR);
        }
    }
}
