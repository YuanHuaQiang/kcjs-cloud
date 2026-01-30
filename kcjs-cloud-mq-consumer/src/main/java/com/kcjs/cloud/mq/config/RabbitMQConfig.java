package com.kcjs.cloud.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ==================== 秒杀场景 - 正常队列 ====================

    public static final String SECKILL_EXCHANGE = "seckill.exchange";
    public static final String SECKILL_QUEUE = "seckill.queue";

    public static final String SECKILL_DLX_EXCHANGE = "seckill.dlx.exchange";
    public static final String SECKILL_DLX_QUEUE = "seckill.dlx.queue";

    @Bean
    public DirectExchange seckillExchange() {
        return new DirectExchange(SECKILL_EXCHANGE);
    }

    @Bean
    public Queue seckillQueue() {
        return QueueBuilder.durable(SECKILL_QUEUE)
                // 指定死信交换机
                .withArgument("x-dead-letter-exchange", SECKILL_DLX_EXCHANGE)
                // 可选：指定死信路由 key
                .withArgument("x-dead-letter-routing-key", "seckill.dlx.routingkey")
                // 可选：TTL（消息超时测试）
                .withArgument("x-message-ttl", 10000)  // 10秒超时
                .build();
    }

    @Bean
    public Binding seckillBinding() {
        return BindingBuilder.bind(seckillQueue()).to(seckillExchange()).with("seckill.order");
    }

    // ==================== 秒杀场景 - 死信队列 ====================

    @Bean
    public DirectExchange seckillDlxExchange() {
        return new DirectExchange(SECKILL_DLX_EXCHANGE);
    }

    @Bean
    public Queue seckillDlxQueue() {
        return QueueBuilder.durable(SECKILL_DLX_QUEUE).build();
    }

    @Bean
    public Binding seckillDlxBinding() {
        return BindingBuilder.bind(seckillDlxQueue()).to(seckillDlxExchange()).with("seckill.dlx.routingkey");
    }

    // ==================== 订单处理场景 - Topic Exchange ====================

    public static final String ORDER_EXCHANGE = "order.topic.exchange";
    public static final String ORDER_CREATED_QUEUE = "order.created.queue";
    public static final String ORDER_PAID_QUEUE = "order.paid.queue";
    public static final String ORDER_SHIPPED_QUEUE = "order.shipped.queue";

    @Bean
    public TopicExchange orderTopicExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return QueueBuilder.durable(ORDER_CREATED_QUEUE).build();
    }

    @Bean
    public Queue orderPaidQueue() {
        return QueueBuilder.durable(ORDER_PAID_QUEUE).build();
    }

    @Bean
    public Queue orderShippedQueue() {
        return QueueBuilder.durable(ORDER_SHIPPED_QUEUE).build();
    }

    @Bean
    public Binding orderCreatedBinding() {
        return BindingBuilder.bind(orderCreatedQueue()).to(orderTopicExchange()).with("order.created");
    }

    @Bean
    public Binding orderPaidBinding() {
        return BindingBuilder.bind(orderPaidQueue()).to(orderTopicExchange()).with("order.paid");
    }

    @Bean
    public Binding orderShippedBinding() {
        return BindingBuilder.bind(orderShippedQueue()).to(orderTopicExchange()).with("order.shipped.#");
    }

    // ==================== 日志收集场景 - Fanout Exchange ====================

    public static final String LOG_EXCHANGE = "log.fanout.exchange";
    public static final String LOG_EMAIL_QUEUE = "log.email.queue";
    public static final String LOG_STORAGE_QUEUE = "log.storage.queue";
    public static final String LOG_MONITOR_QUEUE = "log.monitor.queue";

    @Bean
    public FanoutExchange logFanoutExchange() {
        return new FanoutExchange(LOG_EXCHANGE);
    }

    @Bean
    public Queue logEmailQueue() {
        return QueueBuilder.durable(LOG_EMAIL_QUEUE).build();
    }

    @Bean
    public Queue logStorageQueue() {
        return QueueBuilder.durable(LOG_STORAGE_QUEUE).build();
    }

    @Bean
    public Queue logMonitorQueue() {
        return QueueBuilder.durable(LOG_MONITOR_QUEUE).build();
    }

    @Bean
    public Binding logEmailBinding() {
        return BindingBuilder.bind(logEmailQueue()).to(logFanoutExchange());
    }

    @Bean
    public Binding logStorageBinding() {
        return BindingBuilder.bind(logStorageQueue()).to(logFanoutExchange());
    }

    @Bean
    public Binding logMonitorBinding() {
        return BindingBuilder.bind(logMonitorQueue()).to(logFanoutExchange());
    }

    // ==================== 异步任务处理场景 - Work Queues ====================

    public static final String TASK_EXCHANGE = "task.direct.exchange";
    public static final String TASK_QUEUE = "task.process.queue";
    public static final String HIGH_PRIORITY_TASK_QUEUE = "task.high.priority.queue";

    @Bean
    public DirectExchange taskDirectExchange() {
        return new DirectExchange(TASK_EXCHANGE);
    }

    @Bean
    public Queue taskQueue() {
        return QueueBuilder.durable(TASK_QUEUE)
                .withArgument("x-max-priority", 10)  // 设置最大优先级
                .build();
    }

    @Bean
    public Queue highPriorityTaskQueue() {
        return QueueBuilder.durable(HIGH_PRIORITY_TASK_QUEUE)
                .withArgument("x-max-priority", 10)
                .withArgument("x-priority", 5)  // 默认优先级
                .build();
    }

    @Bean
    public Binding taskBinding() {
        return BindingBuilder.bind(taskQueue()).to(taskDirectExchange()).with("task.process");
    }

    @Bean
    public Binding highPriorityTaskBinding() {
        return BindingBuilder.bind(highPriorityTaskQueue()).to(taskDirectExchange()).with("task.high.priority");
    }

    // ==================== 延迟消息场景 - 延迟队列 ====================

    public static final String DELAY_EXCHANGE = "delay.direct.exchange";
    public static final String DELAY_PROCESS_QUEUE = "delay.process.queue";
    public static final String DELAY_DLX_EXCHANGE = "delay.dlx.exchange";
    public static final String DELAY_TARGET_QUEUE = "delay.target.queue";

    @Bean
    public DirectExchange delayDirectExchange() {
        return new DirectExchange(DELAY_EXCHANGE);
    }

    @Bean
    public Queue delayProcessQueue() {
        return QueueBuilder.durable(DELAY_PROCESS_QUEUE)
                .withArgument("x-dead-letter-exchange", DELAY_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "delay.target")
                .build();
    }

    @Bean
    public Queue delayTargetQueue() {
        return QueueBuilder.durable(DELAY_TARGET_QUEUE).build();
    }

    @Bean
    public Binding delayProcessBinding() {
        return BindingBuilder.bind(delayProcessQueue()).to(delayDirectExchange()).with("delay.start");
    }

    @Bean
    public Binding delayTargetBinding() {
        return BindingBuilder.bind(delayTargetQueue()).to(delayDlxExchange()).with("delay.target");
    }

    @Bean
    public DirectExchange delayDlxExchange() {
        return new DirectExchange(DELAY_DLX_EXCHANGE);
    }
}