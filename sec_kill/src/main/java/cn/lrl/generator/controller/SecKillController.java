package cn.lrl.generator.controller;

import cn.lrl.exception.SeckillException;
import cn.lrl.generator.pojo.SeckillGoods;
import cn.lrl.generator.pojo.SeckillOrder;
import cn.lrl.generator.service.CommonService;
import cn.lrl.generator.service.ISeckillGoodsService;
import cn.lrl.po.MQMessage;
import cn.lrl.rabbitMQ.MQSender;
import cn.lrl.utils.RedisUtil;
import cn.lrl.vo.LoginVo;
import cn.lrl.vo.RespMsgEnum;
import cn.lrl.vo.RespMsgVo;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seckill")
@Slf4j
public class SecKillController implements InitializingBean {

    @Autowired
    ISeckillGoodsService seckillGoodsService;

    @Autowired
    CommonService commonService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    MQSender sender;

    // 内存标记-库存是否为空的标识变量，true表示空
    public static final Map<Long, Boolean> EmptyStockMap = new HashMap<>();

    /**
     * 实现秒杀业务
     *
     * @param path           随机路径
     * @param seckillGoodsId 秒杀商品id
     * @param vo             用户登录信息
     * @return 模版对象
     */
    @PostMapping("/{path}/doSeckill")
    public RespMsgVo doSecKill(
            @PathVariable("path") String path,
            @NotNull Long seckillGoodsId,
            @RequestAttribute("login_vo") LoginVo vo) {

        // 校验随机路径的合法性
        boolean isAvailablePath = commonService.checkPath(Long.parseLong(vo.getMobile()), seckillGoodsId, path);
        if (!isAvailablePath) {
            throw new SeckillException(RespMsgEnum.ILLEGAL_REQUEST);
        }

        // 判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisUtil.get("order:" + vo.getMobile() + "-" + seckillGoodsId);
        if (seckillOrder != null) {
            throw new SeckillException(RespMsgEnum.DUPLICATE_SECKILL);
        }

        // 判断是否存在刷单行为
        boolean isGoodCreate = commonService.isSeckillGoodCreate(Long.parseLong(vo.getMobile()), seckillGoodsId);
        if (isGoodCreate) {
            throw new SeckillException(RespMsgEnum.BUYYOURSELF_ERROR);
        }

        // 高并发下避免空库存依旧访问redis去拿stock
        if (EmptyStockMap.get(seckillGoodsId)) {
            throw new SeckillException(RespMsgEnum.EMPTY_SECKILL_STOCK);
        }

        // 通过redis单线程原子性使库存--
        long stock = redisUtil.decr("seckillGoods:" + seckillGoodsId, 1);

        if (stock < 0) {
            EmptyStockMap.put(seckillGoodsId, true);
            redisUtil.incr("seckillGoods:" + seckillGoodsId, 1);
            throw new SeckillException(RespMsgEnum.EMPTY_SECKILL_STOCK);
        }

        // mq异步下单
        MQMessage MQMessage = new MQMessage()
                .setUserId(Long.parseLong(vo.getMobile()))
                .setSeckillGoodsId(seckillGoodsId);
        sender.sendSeckillMessage(JSON.toJSONString(MQMessage));

        return RespMsgVo.success();
    }

    /**
     * 初始化bean的时候就加载秒杀商品，获取秒杀商品的数量存入Redis库存
     */
    @Override
    public void afterPropertiesSet() {
        List<SeckillGoods> goodsList = seckillGoodsService.findSeckillGoods();
        if (CollectionUtils.isEmpty(goodsList)) {
            log.warn("秒杀库存为空！！！请确认秒杀商品！！！");
            return;
        }

        goodsList.forEach(seckillGood -> {
            if (seckillGood.getEndTime().getTime() - new Date().getTime() < 0) {
                log.info(seckillGood.getId() + "号秒杀商品已经结束！");
            } else {
                redisUtil.set("seckillGoods:" + seckillGood.getId(), seckillGood.getStockCount(), (seckillGood.getEndTime().getTime() - new Date().getTime()) / 1000);
                EmptyStockMap.put(seckillGood.getId(), false);
            }
        });
    }
}
