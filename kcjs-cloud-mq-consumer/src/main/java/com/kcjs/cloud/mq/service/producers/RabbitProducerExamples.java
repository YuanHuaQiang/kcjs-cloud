package com.kcjs.cloud.mq.service.producers;

import com.kcjs.cloud.mq.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * RabbitMQ 生产者示例，展示不同场景下的消息发送
 */
@Service
@RequiredArgsConstructor
public class RabbitProducerExamples {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 发送订单创建消息
     * 使用Topic Exchange
     */
    public void sendOrderCreatedMessage(String orderInfo) {
        String routingKey = "order.created";
        String message = String.format("[订单创建] %s - %s", orderInfo, LocalDateTime.now());
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.ORDER_EXCHANGE,
            routingKey,
            message
        );
        System.out.println("发送订单创建消息: " + message);
    }

    /**
     * 发送订单支付消息
     * 使用Topic Exchange
     */
    public void sendOrderPaidMessage(String paymentInfo) {
        String routingKey = "order.paid";
        String message = String.format("[订单支付] %s - %s", paymentInfo, LocalDateTime.now());
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.ORDER_EXCHANGE,
            routingKey,
            message
        );
        System.out.println("发送订单支付消息: " + message);
    }

    /**
     * 发送订单发货消息
     * 使用Topic Exchange
     */
    public void sendOrderShippedMessage(String shippingInfo) {
        String routingKey = "order.shipped.notify";
        String message = String.format("[订单发货] %s - %s", shippingInfo, LocalDateTime.now());
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.ORDER_EXCHANGE,
            routingKey,
            message
        );
        System.out.println("发送订单发货消息: " + message);
    }

    /**
     * 发送日志消息（会广播到所有日志相关的队列）
     * 使用Fanout Exchange
     */
    public void sendLogMessage(String logMessage) {
        String message = String.format("[日志] %s - %s", logMessage, LocalDateTime.now());
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.LOG_EXCHANGE,
            "", // Fanout Exchange不需要routing key
            message
        );
        System.out.println("发送日志消息: " + message);
    }

    /**
     * 发送普通任务消息
     * 使用Direct Exchange
     */
    public void sendNormalTaskMessage(String taskInfo) {
        String routingKey = "task.process";
        String message = String.format("[普通任务] %s - %s", taskInfo, LocalDateTime.now());
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.TASK_EXCHANGE,
            routingKey,
            message
        );
        System.out.println("发送普通任务消息: " + message);
    }

    /**
     * 发送高优先级任务消息
     * 使用Direct Exchange
     */
    public void sendHighPriorityTaskMessage(String taskInfo) {
        String routingKey = "task.high.priority";
        String message = String.format("[高优先级任务] %s - %s", taskInfo, LocalDateTime.now());
        
        // 设置消息优先级
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.TASK_EXCHANGE,
            routingKey,
            message,
            msg -> {
                msg.getMessageProperties().setPriority(8); // 设置高优先级
                return msg;
            }
        );
        System.out.println("发送高优先级任务消息: " + message);
    }

    /**
     * 发送延迟消息
     * 通过设置消息TTL实现延迟效果
     */
    public void sendDelayMessage(String delayMessage, int delayTimeMs) {
        String routingKey = "delay.start";
        String message = String.format("[延迟消息] %s - 延迟%s毫秒 - %s", 
                                     delayMessage, delayTimeMs, LocalDateTime.now());
        
        // 设置消息的TTL（生存时间），实现延迟效果
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.DELAY_EXCHANGE,
            routingKey,
            message,
            msg -> {
                msg.getMessageProperties().setExpiration(String.valueOf(delayTimeMs));
                return msg;
            }
        );
        System.out.println("发送延迟消息: " + message + ", 延迟时间: " + delayTimeMs + "ms");
    }

    /**
     * 发送秒杀消息
     * 使用Direct Exchange
     */
    public void sendSeckillMessage(String seckillInfo) {
        String routingKey = "seckill:stock:1001";
        String message = String.format("[秒杀消息] %s - %s", seckillInfo, LocalDateTime.now());
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.SECKILL_EXCHANGE,
            routingKey,
            message
        );
        System.out.println("发送秒杀消息: " + message);
    }

    /**
     * 发送多种类型的消息用于测试
     */
    public void sendTestMessages() {
        System.out.println("\n=== 开始发送测试消息 ===");
        
        // 发送订单相关消息
        sendOrderCreatedMessage("订单ID: ORD001, 用户: 张三, 金额: ¥99.99");
        sendOrderPaidMessage("订单ID: ORD001, 支付方式: 微信, 金额: ¥99.99");
        sendOrderShippedMessage("订单ID: ORD001, 快递: SF, 单号: SF123456789");
        
        // 发送日志消息
        sendLogMessage("用户登录成功 - IP: 192.168.1.100");
        sendLogMessage("ERROR - 数据库连接异常");
        
        // 发送任务消息
        sendNormalTaskMessage("生成报表 - 月度销售统计");
        sendHighPriorityTaskMessage("紧急修复 - 系统漏洞补丁");
        
        // 发送延迟消息
        sendDelayMessage("订单ORD002超时未支付，自动取消", 5000); // 5秒后处理
        
        // 发送秒杀消息
        sendSeckillMessage("用户ID: U1001, 商品ID: P1001, 数量: 1");
        
        System.out.println("=== 测试消息发送完成 ===\n");
    }
}