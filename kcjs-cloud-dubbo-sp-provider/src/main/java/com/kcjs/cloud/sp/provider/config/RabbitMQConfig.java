package com.kcjs.cloud.sp.provider.config;



import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ==================== 正常队列 ====================

    public static final String NORMAL_EXCHANGE = "seckill.exchange";
    public static final String NORMAL_QUEUE = "seckill.queue";

    public static final String DLX_EXCHANGE = "seckill.dlx.exchange";
    public static final String DLX_QUEUE = "seckill.dlx.queue";

    @Bean
    public DirectExchange normalExchange() {
        return new DirectExchange(NORMAL_EXCHANGE);
    }

    @Bean
    public Queue normalQueue() {
        return QueueBuilder.durable(NORMAL_QUEUE)
                // 指定死信交换机
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                // 可选：指定死信路由 key
                .withArgument("x-dead-letter-routing-key", "seckill.dlx.routingkey")
                // 可选：TTL（消息超时测试）
                .withArgument("x-message-ttl", 10000)  // 10秒超时
                .build();
    }

    @Bean
    public Binding normalBinding() {
        return BindingBuilder.bind(normalQueue()).to(normalExchange()).with("seckill:stock:1001");
    }

    // ==================== 死信队列 ====================

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    @Bean
    public Queue dlxQueue() {
        return QueueBuilder.durable(DLX_QUEUE).build();
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with("seckill.dlx.routingkey");
    }
}