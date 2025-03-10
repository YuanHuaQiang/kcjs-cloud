package com.kcjs.cloud.provider.service;

import com.kcjs.cloud.api.RedissonSeckillService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
@DubboService
public class RedissonSeckillServiceImpl implements RedissonSeckillService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    private static final String STOCK_KEY = "seckill:stock:1001";

    @PostConstruct
    public void init() {
        redisTemplate.opsForValue().set(STOCK_KEY, "100");
    }

    @Override
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

            // 使用原子操作减少库存
            redisTemplate.opsForValue().decrement(STOCK_KEY);

            log.info("用户 {} 秒杀成功，剩余库存：{}", userId, stock - 1);
            return "恭喜秒杀成功！";

        } catch (InterruptedException e) {
            log.error("秒杀过程中发生中断", e);
            Thread.currentThread().interrupt(); // 重新设置中断状态
            return "系统异常";
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}