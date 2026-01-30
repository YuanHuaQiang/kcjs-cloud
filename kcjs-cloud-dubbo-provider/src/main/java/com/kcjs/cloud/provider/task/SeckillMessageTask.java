package com.kcjs.cloud.provider.task;

import com.kcjs.cloud.mysql.pojo.MessageLog;
import com.kcjs.cloud.mysql.repository.MessageLogRepository;
import com.kcjs.cloud.provider.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeckillMessageTask {

    private final MessageLogRepository messageLogRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0/5 * * * * ?")
    public void sendSeckillMessage() {
        List<MessageLog> messageLogs = messageLogRepository.findByStatusAndMessageType(0, "seckill");
        if (messageLogs.isEmpty()) {
            return;
        }

        log.info("Found {} pending seckill messages", messageLogs.size());

        for (MessageLog messageLog : messageLogs) {
            try {
                if (messageLog.getRetryCount() >= 3) {
                    messageLog.setStatus(2); // Failed
                    messageLog.setErrorMessage("Max retry count reached");
                    messageLog.setUpdateTime(System.currentTimeMillis());
                    messageLogRepository.save(messageLog);
                    continue;
                }

                messageLog.setRetryCount(messageLog.getRetryCount() + 1);
                messageLog.setUpdateTime(System.currentTimeMillis());
                messageLogRepository.save(messageLog); // Update retry count

                CorrelationData correlationData = new CorrelationData(messageLog.getCorrelationId());
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.NORMAL_EXCHANGE,
                        "seckill.order",
                        messageLog.getMessageBody(),
                        correlationData
                );

            } catch (Exception e) {
                log.error("Failed to send message: {}", messageLog.getId(), e);
                messageLog.setErrorMessage(e.getMessage());
                messageLogRepository.save(messageLog);
            }
        }
    }
}
