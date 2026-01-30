package com.kcjs.cloud.mq.service.seckill.impl;

import com.kcjs.cloud.api.account.AccountService;
import com.kcjs.cloud.api.order.OrderService;
import com.kcjs.cloud.api.storage.StorageService;
import com.kcjs.cloud.mq.service.seckill.SeckillConsumerService;
import com.kcjs.cloud.mysql.pojo.Order;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;

@Slf4j
@Service
public class SeckillConsumerServiceImpl implements SeckillConsumerService {


    @DubboReference
    private StorageService storageService;
    @DubboReference
    private OrderService orderService;
    @DubboReference
    private AccountService accountService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 处理消息以执行秒杀操作和后续业务流程
     *
     * @param msg 消息字符串，格式为"userId:productId"，用于识别用户和产品
     */
    @Override
    @GlobalTransactional(name = "seckill-global-tx", rollbackFor = Exception.class)
    public void consumer(String msg) {
        log.info("开始处理秒杀消息: {}", msg);
        if (msg == null || !msg.contains(":")) {
            log.error("消息格式错误: {}", msg);
            throw new IllegalArgumentException("消息格式错误: " + msg);
        }

        String[] split = msg.split(":");
        String userId = split[0];
        String productId = split[1];

        // 幂等性校验 Key
        String idempotencyKey = "seckill:consumed:" + userId + ":" + productId;

        // 1. 检查是否已经消费成功
        String status = stringRedisTemplate.opsForValue().get(idempotencyKey);
        if ("success".equals(status)) {
            log.info("消息已消费，跳过: {}", msg);
            return;
        }

        // 2. 尝试获取锁，防止并发消费
        Boolean isNew = stringRedisTemplate.opsForValue().setIfAbsent(idempotencyKey, "processing", 10, TimeUnit.MINUTES);
        if (!Boolean.TRUE.equals(isNew)) {
            // 如果是 processing，说明正在处理中
            throw new IllegalStateException("消息正在处理中或重复消费: " + msg);
        }

        try {
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

            // 3. 标记消费成功
            stringRedisTemplate.opsForValue().set(idempotencyKey, "success", 24, TimeUnit.HOURS);
            log.info("秒杀业务处理完成: {}", msg);
        } catch (Exception e) {
            log.error("秒杀业务处理失败: {}", msg, e);
            // 删除 Key，允许重试
            stringRedisTemplate.delete(idempotencyKey);
            throw e;
        }
    }
}
