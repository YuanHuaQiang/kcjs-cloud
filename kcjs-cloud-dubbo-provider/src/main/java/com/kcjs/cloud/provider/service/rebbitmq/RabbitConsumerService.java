package com.kcjs.cloud.provider.service.rebbitmq;

import com.kcjs.cloud.provider.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Service
public class RabbitConsumerService {

    @RabbitListener(queues = RabbitMQConfig.NORMAL_QUEUE)
    @Retryable(maxAttempts = 2,backoff = @Backoff(delay = 10, multiplier = 2))
    public void consume(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println("接收到消息：" + msg);
        Random random = new Random();
        int i = random.nextInt(101);
        if (i > 50){
           throw new RuntimeException(String.valueOf(i));
        }

        // 正常处理
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 重试完了还是失败进入
     */
    @Recover
    public void recover(Exception e, Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println("重试失败："+msg);
        // 重试次数到了，进入死信
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * TTL后未消费/重试次数用完
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(queues = RabbitMQConfig.DLX_QUEUE)
    public void dlxConsume(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println("死信队列收到消息：" + msg);

        // 这里可以记录日志、报警等
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
