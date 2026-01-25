package com.kcjs.cloud.mq.service.consumers;

import com.kcjs.cloud.mq.config.RabbitMQConfig;
import com.kcjs.cloud.mq.service.seckill.SeckillConsumerService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class SeckillRabbitConsumers {

    @Autowired
    private SeckillConsumerService seckillConsumerService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 监听RabbitMQ中的消息并进行消费
     * 该方法标注了@RabbitListener注解，用于指定监听的队列
     * 同时使用了@Retryable注解，配置了重试策略，以处理消费失败的情况
     *
     * @param message 消息对象，包含消息体和属性等信息
     * @param channel AMQP通道，用于执行消息的确认操作
     * @throws Exception 如果消息消费过程中发生异常
     */
    @RabbitListener(queues = RabbitMQConfig.SECKILL_QUEUE)
    @Retryable(maxAttempts = 2,backoff = @Backoff(delay = 2000, multiplier = 2))
    public void consume(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println("接收到消息：" + msg);

        seckillConsumerService.consumer(msg);
        // 正常处理
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 重试完了还是失败进入
     * Recover需要与Retryable 参数一致
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
    @RabbitListener(queues = RabbitMQConfig.SECKILL_DLX_QUEUE)
    public void dlxConsume(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println("死信队列收到消息：" + msg);

        String[] split = msg.split(":");
        String userId = split[0];
        String productId = split[1];

        //库存加回去
        stringRedisTemplate.opsForValue().increment("seckill:stock:"+productId);

        // 这里可以记录日志、报警等
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
