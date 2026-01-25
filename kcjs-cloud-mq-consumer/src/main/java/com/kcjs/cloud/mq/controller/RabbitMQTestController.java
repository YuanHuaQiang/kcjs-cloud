package com.kcjs.cloud.mq.controller;

import com.kcjs.cloud.mq.service.producers.RabbitProducerExamples;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RabbitMQ 测试控制器，提供API接口用于测试不同的消息队列场景
 */
@RestController
@RequestMapping("/rabbitmq/test")
@RequiredArgsConstructor
public class RabbitMQTestController {

    private final RabbitProducerExamples rabbitProducerExamples;

    /**
     * 发送订单创建消息
     */
    @GetMapping("/send-order-created")
    public String sendOrderCreated(@RequestParam(defaultValue = "测试订单") String orderInfo) {
        rabbitProducerExamples.sendOrderCreatedMessage(orderInfo);
        return "订单创建消息已发送: " + orderInfo;
    }

    /**
     * 发送订单支付消息
     */
    @GetMapping("/send-order-paid")
    public String sendOrderPaid(@RequestParam(defaultValue = "测试支付") String paymentInfo) {
        rabbitProducerExamples.sendOrderPaidMessage(paymentInfo);
        return "订单支付消息已发送: " + paymentInfo;
    }

    /**
     * 发送订单发货消息
     */
    @GetMapping("/send-order-shipped")
    public String sendOrderShipped(@RequestParam(defaultValue = "测试发货") String shippingInfo) {
        rabbitProducerExamples.sendOrderShippedMessage(shippingInfo);
        return "订单发货消息已发送: " + shippingInfo;
    }

    /**
     * 发送日志消息
     */
    @GetMapping("/send-log")
    public String sendLog(@RequestParam(defaultValue = "测试日志") String logMessage) {
        rabbitProducerExamples.sendLogMessage(logMessage);
        return "日志消息已发送: " + logMessage;
    }

    /**
     * 发送普通任务消息
     */
    @GetMapping("/send-normal-task")
    public String sendNormalTask(@RequestParam(defaultValue = "测试任务") String taskInfo) {
        rabbitProducerExamples.sendNormalTaskMessage(taskInfo);
        return "普通任务消息已发送: " + taskInfo;
    }

    /**
     * 发送高优先级任务消息
     */
    @GetMapping("/send-high-priority-task")
    public String sendHighPriorityTask(@RequestParam(defaultValue = "高优先级任务") String taskInfo) {
        rabbitProducerExamples.sendHighPriorityTaskMessage(taskInfo);
        return "高优先级任务消息已发送: " + taskInfo;
    }

    /**
     * 发送延迟消息
     */
    @GetMapping("/send-delay-message")
    public String sendDelayMessage(
            @RequestParam(defaultValue = "延迟测试消息") String delayMessage,
            @RequestParam(defaultValue = "5000") int delayTimeMs) {
        rabbitProducerExamples.sendDelayMessage(delayMessage, delayTimeMs);
        return "延迟消息已发送: " + delayMessage + ", 延迟时间: " + delayTimeMs + "ms";
    }

    /**
     * 发送秒杀消息
     */
    @GetMapping("/send-seckill")
    public String sendSeckill(@RequestParam(defaultValue = "秒杀测试") String seckillInfo) {
        rabbitProducerExamples.sendSeckillMessage(seckillInfo);
        return "秒杀消息已发送: " + seckillInfo;
    }

    /**
     * 发送所有类型的测试消息
     */
    @GetMapping("/send-all-test-messages")
    public String sendAllTestMessages() {
        rabbitProducerExamples.sendTestMessages();
        return "所有测试消息已发送";
    }

    /**
     * API 接口说明
     */
    @GetMapping("/help")
    public String getHelp() {

        return "<h1>RabbitMQ 测试接口说明</h1>" +
                "<ul>" +
                "<li><strong>/rabbitmq/test/send-order-created?orderInfo=xxx</strong> - 发送订单创建消息</li>" +
                "<li><strong>/rabbitmq/test/send-order-paid?paymentInfo=xxx</strong> - 发送订单支付消息</li>" +
                "<li><strong>/rabbitmq/test/send-order-shipped?shippingInfo=xxx</strong> - 发送订单发货消息</li>" +
                "<li><strong>/rabbitmq/test/send-log?logMessage=xxx</strong> - 发送日志消息</li>" +
                "<li><strong>/rabbitmq/test/send-normal-task?taskInfo=xxx</strong> - 发送普通任务消息</li>" +
                "<li><strong>/rabbitmq/test/send-high-priority-task?taskInfo=xxx</strong> - 发送高优先级任务消息</li>" +
                "<li><strong>/rabbitmq/test/send-delay-message?delayMessage=xxx&delayTimeMs=5000</strong> - 发送延迟消息</li>" +
                "<li><strong>/rabbitmq/test/send-seckill?seckillInfo=xxx</strong> - 发送秒杀消息</li>" +
                "<li><strong>/rabbitmq/test/send-all-test-messages</strong> - 发送所有测试消息</li>" +
                "<li><strong>/rabbitmq/test/help</strong> - 显示此帮助信息</li>" +
                "</ul>" +
                "<p>查看消费者日志以验证消息处理结果。</p>";
    }
}