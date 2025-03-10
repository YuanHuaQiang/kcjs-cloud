package com.kcjs.cloud.provider.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@DubboService
public class RedissonSeckillService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    private static final String STOCK_KEY = "seckill:stock:1001";

    public String seckillProduct(Long userId) {
        RLock lock = redissonClient.getLock("lock:product:1001");

        try {
            // 尝试加锁，等待2秒，锁自动释放时间10秒
            boolean locked = lock.tryLock(2, 10, TimeUnit.SECONDS);

            if (!locked) {
                return "系统繁忙，请稍后重试";
            }

            String stockStr = redisTemplate.opsForValue().get(STOCK_KEY);
            int stock = stockStr == null ? 0 : Integer.parseInt(stockStr);

            if (stock <= 0) {
                return "库存不足";
            }

            redisTemplate.opsForValue().set(STOCK_KEY, String.valueOf(stock - 1));

            System.out.println("用户 " + userId + " 秒杀成功，剩余库存：" + (stock - 1));
            return "恭喜秒杀成功！";

        } catch (InterruptedException e) {
            e.printStackTrace();
            return "系统异常";
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}