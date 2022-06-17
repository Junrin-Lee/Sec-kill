package cn.lrl.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoodsTopicConfig {
    private static final String QUEUE = "goodsQueue";

    private static final String EXCHANGE = "goodsTopicExchange";

    /*
     * #:能匹配一个或多个单词
     * *:只能匹配1个单词
     *  */
    private static final String ROUTEKEY = "#.order.goods.#";


    @Bean
    public TopicExchange goodsTopicExchangeManager() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue goodsQueue() {
        return new Queue(QUEUE);
    }

    @Bean
    public Binding goodsBinding() {
        return BindingBuilder.bind(goodsQueue()).to(goodsTopicExchangeManager()).with(ROUTEKEY);
    }
}
