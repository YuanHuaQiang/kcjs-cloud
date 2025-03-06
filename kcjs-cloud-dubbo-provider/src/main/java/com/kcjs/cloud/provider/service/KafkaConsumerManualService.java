package com.kcjs.cloud.provider.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerManualService {
    
    @KafkaListener(topics = "test-topic", groupId = "my-group")
    public void listen(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            System.out.println("收到消息：" + record.value());
            // TODO: 处理消息逻辑

            // 处理成功后手动提交 Offset
            ack.acknowledge();
        } catch (Exception e) {
            System.err.println("消费失败，稍后重试");
        }
    }
}