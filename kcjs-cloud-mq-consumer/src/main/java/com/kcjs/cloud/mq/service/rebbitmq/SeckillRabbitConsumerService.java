package com.kcjs.cloud.mq.service.rebbitmq;

import com.kcjs.cloud.api.account.AccountService;
import com.kcjs.cloud.api.order.OrderService;
import com.kcjs.cloud.api.storage.StorageService;
import com.kcjs.cloud.mq.config.RabbitMQConfig;
import com.kcjs.cloud.mysql.pojo.Order;
import com.rabbitmq.client.Channel;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.random.RandomGenerator;

@Service
public class SeckillRabbitConsumerService {

    @DubboReference
    private StorageService storageService;
    @DubboReference
    private OrderService orderService;
    @DubboReference
    private AccountService accountService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 使用mq作为全局事物的入口，需要手动处理对库存的还原
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(queues = RabbitMQConfig.NORMAL_QUEUE)
    @Retryable(maxAttempts = 2,backoff = @Backoff(delay = 10, multiplier = 2))
    @GlobalTransactional(name = "seckill-global-tx", rollbackFor = Exception.class)
    public void consume(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        System.out.println("接收到消息：" + msg);

        String[] split = msg.split(":");
        String userId = split[0];
        String productId = split[1];

        BigDecimal amount = BigDecimal.valueOf(RandomGenerator.getDefault().nextDouble(100, 1000));
        //本应调用其他服务的接口，这里直接调用provider模块
        // 秒杀成功继续业务
        storageService.decrease(Long.valueOf(productId), 1);

        Order order = new Order();
        order.setUserId(Long.valueOf(userId));
        order.setProductId(Long.valueOf(productId));
        order.setCount(1);
        order.setStatus(0);//待支付订单
        order.setAmount(amount);
        order = orderService.saveOrder(order);//若为先创建后支付模式则扣库存和创建订单不需要事物管理


        accountService.decrease(Long.valueOf(userId), amount);

        order.setStatus(1);//若余额支付成功，则修改订单状态为已支付
        orderService.saveOrder(order);

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

        String[] split = msg.split(":");
        String userId = split[0];
        String productId = split[1];

        //库存加回去
        stringRedisTemplate.opsForValue().increment(productId);

        // 这里可以记录日志、报警等
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
