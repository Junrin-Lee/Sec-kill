package cn.lrl.rabbitMQ;

import cn.lrl.exception.OrderException;
import cn.lrl.exception.SeckillException;
import cn.lrl.generator.pojo.Goods;
import cn.lrl.generator.pojo.SeckillOrder;
import cn.lrl.generator.service.IGoodsService;
import cn.lrl.generator.service.IOrderService;
import cn.lrl.generator.service.ISeckillGoodsService;
import cn.lrl.generator.service.ISeckillOrderService;
import cn.lrl.po.MQMessage;
import cn.lrl.utils.RedisUtil;
import cn.lrl.vo.GoodsVo;
import cn.lrl.vo.RespMsgEnum;
import cn.lrl.vo.SeckillGoodsVo;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    ISeckillGoodsService seckillGoodsService;

    @Autowired
    ISeckillOrderService seckillOrderService;

    @Autowired
    IGoodsService goodsService;

    @Autowired
    IOrderService orderService;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 秒杀订单商品异步下单
     *
     * @param msg JSON字符串
     */
    @RabbitListener(queues = "seckillQueue")
    @Transactional
    public void receiver_seckill(String msg) {
        log.info("mqReceiver接收消息：" + msg);

        // 解析Json
        MQMessage MQMessage = JSON.parseObject(msg, MQMessage.class);
        Long seckillGoodsId = MQMessage.getSeckillGoodsId();
        Long userId = MQMessage.getUserId();

        // 判断数据库库存是否为0
        SeckillGoodsVo seckillGoodsVo = seckillGoodsService.findSeckillGoodsVoById(seckillGoodsId);
        if (seckillGoodsVo.getStockCount() < 1) {
            throw new SeckillException(RespMsgEnum.EMPTY_SECKILL_STOCK);
        }

        // 判断redis中预减库存是否为0
        SeckillOrder seckillOrder = (SeckillOrder) redisUtil.get("order:" + userId + "-" + seckillGoodsId);
        if (seckillOrder != null) {
            throw new SeckillException(RespMsgEnum.EMPTY_SECKILL_STOCK);
        }
        seckillOrderService.doSeckillOrder(userId, seckillGoodsVo);
    }

    /**
     * 商品异步下单
     *
     * @param msg JSON字符串
     */
    @RabbitListener(queues = "goodsQueue")
    @Transactional
    public void receiver_goods(String msg) {
        log.info("mqReceiver接收消息：" + msg);

        // 解析Json
        MQMessage MQMessage = JSON.parseObject(msg, MQMessage.class);
        Long goodsId = MQMessage.getGoodsId();
        Long userId = MQMessage.getUserId();
        Integer stockCount = MQMessage.getStockCount();

        // 判断数据库库存是否为0
        Goods good = goodsService.findGoodById(goodsId);
        if (good.getGoodsStock() < 1) {
            throw new OrderException(RespMsgEnum.EMPTY_GOODS_STOCK);
        }
        GoodsVo goodsVo = (GoodsVo) new GoodsVo()
                .setStockCount(stockCount)
                .setId(goodsId)
                .setGoodsName(good.getGoodsName())
                .setGoodsPrice(good.getGoodsPrice());

        orderService.doGoodsOrder(userId, goodsVo);
    }
}
