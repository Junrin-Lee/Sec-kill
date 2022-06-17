package cn.lrl.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecKillTopicConfig {
    private static final String QUEUE = "seckillQueue";

    private static final String EXCHANGE = "seckillTopicExchange";

    /*
     * #:能匹配一个或多个单词
     * *:只能匹配1个单词
     *  */
    private static final String ROUTEKEY = "#.order.seckill.#";


    @Bean
    public TopicExchange seckillTopicExchangeManager() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue seckillQueue() {
        return new Queue(QUEUE);
    }

    @Bean
    public Binding seckillBinding() {
        return BindingBuilder.bind(seckillQueue()).to(seckillTopicExchangeManager()).with(ROUTEKEY);
    }
}
