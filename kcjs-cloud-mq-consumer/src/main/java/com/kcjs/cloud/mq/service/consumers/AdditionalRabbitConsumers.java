package com.kcjs.cloud.mq.service.consumers;

import com.kcjs.cloud.mq.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 额外的RabbitMQ消费者示例，展示不同使用场景
 */
@Service
@Slf4j
public class AdditionalRabbitConsumers {

    /**
     * 订单创建消费者 - 处理订单创建事件
     * 使用Topic Exchange，路由键为 order.created
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
    public void handleOrderCreated(String orderInfo) {
        log.info("订单创建消费者接收到消息: {}, 时间: {}", orderInfo, new Date());
        // 实际业务逻辑：保存订单、发送邮件等
        try {
            // 模拟订单创建处理
            Thread.sleep(1000); // 模拟处理时间
            log.info("订单创建处理完成: {}", orderInfo);
        } catch (Exception e) {
            log.error("订单创建处理失败", e);
            // 这里可以进行错误处理或重试逻辑
        }
    }

    /**
     * 订单支付消费者 - 处理订单支付事件
     * 使用Topic Exchange，路由键为 order.paid
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_PAID_QUEUE)
    public void handleOrderPaid(String paymentInfo) {
        log.info("订单支付消费者接收到消息: {}, 时间: {}", paymentInfo, new Date());
        // 实际业务逻辑：更新订单状态、扣减积分等
        try {
            // 模拟支付处理
            Thread.sleep(800); // 模拟处理时间
            log.info("订单支付处理完成: {}", paymentInfo);
        } catch (Exception e) {
            log.error("订单支付处理失败", e);
        }
    }

    /**
     * 订单发货消费者 - 处理订单发货事件
     * 使用Topic Exchange，路由键为 order.shipped.*
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_SHIPPED_QUEUE)
    public void handleOrderShipped(String shippingInfo) {
        log.info("订单发货消费者接收到消息: {}, 时间: {}", shippingInfo, new Date());
        // 实际业务逻辑：更新物流信息、发送通知等
        try {
            // 模拟发货处理
            Thread.sleep(1200); // 模拟处理时间
            log.info("订单发货处理完成: {}", shippingInfo);
        } catch (Exception e) {
            log.error("订单发货处理失败", e);
        }
    }

    /**
     * 日志邮件通知消费者 - 处理需要邮件通知的日志
     * 使用Fanout Exchange
     */
    @RabbitListener(queues = RabbitMQConfig.LOG_EMAIL_QUEUE)
    public void handleLogForEmail(String logMessage) {
        log.info("日志邮件通知消费者接收到消息: {}, 时间: {}", logMessage, new Date());
        // 实际业务逻辑：发送邮件通知
        try {
            // 模拟邮件发送
            Thread.sleep(2000); // 模拟邮件发送时间
            log.info("邮件通知发送完成: {}", logMessage);
        } catch (Exception e) {
            log.error("邮件发送失败", e);
        }
    }

    /**
     * 日志存储消费者 - 处理需要存储的日志
     * 使用Fanout Exchange
     */
    @RabbitListener(queues = RabbitMQConfig.LOG_STORAGE_QUEUE)
    public void handleLogForStorage(String logMessage) {
        log.info("日志存储消费者接收到消息: {}, 时间: {}", logMessage, new Date());
        // 实际业务逻辑：写入数据库或日志文件
        try {
            // 模拟日志存储
            Thread.sleep(500); // 模拟存储时间
            log.info("日志存储完成: {}", logMessage);
        } catch (Exception e) {
            log.error("日志存储失败", e);
        }
    }

    /**
     * 日志监控消费者 - 处理需要监控的日志
     * 使用Fanout Exchange
     */
    @RabbitListener(queues = RabbitMQConfig.LOG_MONITOR_QUEUE)
    public void handleLogForMonitoring(String logMessage) {
        log.info("日志监控消费者接收到消息: {}, 时间: {}", logMessage, new Date());
        // 实际业务逻辑：检查是否需要告警
        try {
            // 模拟监控检查
            if (logMessage.contains("ERROR") || logMessage.contains("WARN")) {
                log.warn("检测到重要日志，可能需要告警: {}", logMessage);
                // 这里可以触发告警机制
            }
            log.info("日志监控检查完成: {}", logMessage);
        } catch (Exception e) {
            log.error("日志监控检查失败", e);
        }
    }

    /**
     * 普通任务消费者 - 处理普通优先级任务
     * 使用Direct Exchange
     */
    @RabbitListener(queues = RabbitMQConfig.TASK_QUEUE)
    public void handleNormalTask(String taskInfo) {
        log.info("普通任务消费者接收到任务: {}, 时间: {}", taskInfo, new Date());
        // 实际业务逻辑：处理普通优先级任务
        try {
            // 模拟任务处理
            Thread.sleep(1500); // 模拟处理时间
            log.info("普通任务处理完成: {}", taskInfo);
        } catch (Exception e) {
            log.error("普通任务处理失败", e);
        }
    }

    /**
     * 高优先级任务消费者 - 处理高优先级任务
     * 使用Direct Exchange
     */
    @RabbitListener(queues = RabbitMQConfig.HIGH_PRIORITY_TASK_QUEUE)
    public void handleHighPriorityTask(String taskInfo) {
        log.info("高优先级任务消费者接收到任务: {}, 时间: {}", taskInfo, new Date());
        // 实际业务逻辑：处理高优先级任务
        try {
            // 模拟任务处理
            Thread.sleep(1000); // 模拟处理时间
            log.info("高优先级任务处理完成: {}", taskInfo);
        } catch (Exception e) {
            log.error("高优先级任务处理失败", e);
        }
    }

    /**
     * 延迟消息消费者 - 处理延迟后的消息
     * 用于处理经过延迟后到达的消息
     */
    @RabbitListener(queues = RabbitMQConfig.DELAY_TARGET_QUEUE)
    public void handleDelayMessage(String delayMessage) {
        log.info("延迟消息消费者接收到消息: {}, 实际处理时间: {}", delayMessage, new Date());
        // 实际业务逻辑：处理延迟消息（如订单超时取消）
        try {
            // 模拟延迟处理逻辑
            if (delayMessage.contains("order:timeout:cancel")) {
                log.info("执行订单超时取消逻辑: {}", delayMessage);
                // 这里可以实现具体的订单取消逻辑
            }
            log.info("延迟消息处理完成: {}", delayMessage);
        } catch (Exception e) {
            log.error("延迟消息处理失败", e);
        }
    }

    /**
     * 秒杀死信队列消费者 - 处理未能正常处理的秒杀消息
     */
    @RabbitListener(queues = RabbitMQConfig.SECKILL_DLX_QUEUE)
    public void handleSeckillDeadLetter(String deadLetterMessage) {
        log.warn("秒杀死信队列消费者接收到消息: {}, 时间: {}", deadLetterMessage, new Date());
        // 实际业务逻辑：处理异常情况，如补偿、记录日志等
        try {
            log.error("秒杀消息处理异常，需要人工介入或补偿处理: {}", deadLetterMessage);
            // 这里可以实现补偿逻辑
        } catch (Exception e) {
            log.error("秒杀死信消息处理失败", e);
        }
    }
}