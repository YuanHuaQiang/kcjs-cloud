package com.kcjs.cloud.provider.config;

import com.kcjs.cloud.mysql.pojo.MessageLog;
import com.kcjs.cloud.mysql.repository.MessageLogRepository;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
        return BindingBuilder.bind(normalQueue()).to(normalExchange()).with("seckill.order");
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


    /**
     * 消息确认/补偿
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageLogRepository messageLogRepository) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // 开启 Publisher Confirm 模式（Spring Boot 2.2+）
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            String msgId = correlationData != null ? correlationData.getId() : "无ID";
            if (ack) {
                System.out.println("【ConfirmCallback】发送成功 messageId = " + msgId);
                if (correlationData != null) {
                    MessageLog messageLog = messageLogRepository.findByCorrelationId(correlationData.getId());
                    if (messageLog != null) {
                        messageLog.setStatus(1); // Sent Success
                        messageLog.setSendTime(System.currentTimeMillis());
                        messageLogRepository.save(messageLog);
                    }
                }
            } else {
                System.out.println("【ConfirmCallback】发送失败 messageId = " + msgId + "，原因：" + cause);
                if (correlationData != null) {
                    MessageLog messageLog = messageLogRepository.findByCorrelationId(correlationData.getId());
                    if (messageLog != null) {
                        messageLog.setErrorMessage("ConfirmCallback failed: " + cause);
                        messageLogRepository.save(messageLog);
                    }
                }
            }
        });

        // 如果还要 ReturnCallback（消息投递失败）
        rabbitTemplate.setReturnsCallback(returned -> {
            System.out.println("【ReturnsCallback】消息路由失败：" + returned.getMessage());
            // TODO 记录日志、补偿等
        });

        return rabbitTemplate;
    }
}