package cn.pointer.lrl.rabbitMQ;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendSeckillMessage(String message) {
        log.info("mqSender发送消息：" + message);
        rabbitTemplate.convertAndSend("seckillTopicExchange", "order.seckill.message", message);
    }

    public void sendGoodsMessage(String message) {
        log.info("mqSender发送消息：" + message);
        rabbitTemplate.convertAndSend("goodsTopicExchange", "order.goods.message", message);
    }
}
