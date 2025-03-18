package com.kcjs.cloud.provider.service.rebbitmq;

import com.kcjs.cloud.api.account.AccountService;
import com.kcjs.cloud.api.order.OrderService;
import com.kcjs.cloud.api.storage.StorageService;
import com.kcjs.cloud.mysql.pojo.Order;
import com.kcjs.cloud.provider.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Service
public class RabbitConsumerService {

    @DubboReference
    private StorageService storageService;
    @DubboReference
    private OrderService orderService;
    @DubboReference
    private AccountService accountService;

    @RabbitListener(queues = RabbitMQConfig.NORMAL_QUEUE)
    @Retryable(maxAttempts = 2,backoff = @Backoff(delay = 10, multiplier = 2))
    @GlobalTransactional(name = "seckill-global-tx", rollbackFor = Exception.class)
    public void consume(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println("接收到消息：" + msg);

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        //productId
        String s = routingKey.split(":")[2];

        // 秒杀成功继续业务
        storageService.decrease(1001L, 1);

        Order order = new Order();
        order.setUserId(Long.valueOf(msg));
        order.setProductId(Long.valueOf(s));
        order.setCount(1);
        order.setStatus(0);
        order.setAmount(new BigDecimal("100"));
        orderService.createOrder(order);

        accountService.decrease(Long.valueOf(s), new BigDecimal("100"));

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
