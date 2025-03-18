package com.kcjs.cloud.provider.service.seckill;

import com.kcjs.cloud.api.RedissonSeckillService;
import com.kcjs.cloud.api.storage.StorageService;
import com.kcjs.cloud.exception.BusinessException;
import com.kcjs.cloud.provider.config.RabbitMQConfig;
import com.kcjs.cloud.result.Result;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@DubboService
@RequiredArgsConstructor
public class RedissonSeckillServiceImpl implements RedissonSeckillService {


    private final StorageService storageService;

    private final StringRedisTemplate redisTemplate;
    private final RedissonClient redissonClient;
    private final RabbitTemplate rabbitTemplate;

    private static final String STOCK_KEY = "seckill:stock:1001";
    private static final String TIME_WINDOW_KEY_PREFIX = "seckill:time-window:";
    private static final long TIME_WINDOW_DURATION = 60; // 时间窗口持续时间，单位秒

    @PostConstruct
    public void init() {
        redisTemplate.opsForValue().set(STOCK_KEY, "10000");
        storageService.save(1001L,10000);
    }

    @Override
    @Transactional
    public Result<String> seckillProduct(Long userId) {
        RLock lock = redissonClient.getLock("lock:product:1001");

        try {
            // 尝试加锁，等待2秒，锁自动释放时间10秒
            boolean locked = lock.tryLock(2, 10, TimeUnit.SECONDS);

            if (!locked) {
                throw new BusinessException("系统繁忙，请稍后重试");
            }

            // 检查时间窗口
            String timeWindowKey = TIME_WINDOW_KEY_PREFIX + userId;
            if (Boolean.TRUE.equals(redisTemplate.hasKey(timeWindowKey))) {
                log.info("用户 {} 在时间窗口内已秒杀过", userId);
                throw new BusinessException("您已秒杀过，请稍后再试");
            }

            String stockStr = redisTemplate.opsForValue().get(STOCK_KEY);
            int stock = stockStr == null ? 0 : Integer.parseInt(stockStr);

            if (stock <= 0) {
                log.info("用户 {} 库存不足", userId);
                throw new BusinessException("库存不足");
            }

            // 使用原子操作减少库存
            redisTemplate.opsForValue().decrement(STOCK_KEY);
            rabbitTemplate.convertAndSend(RabbitMQConfig.NORMAL_EXCHANGE, STOCK_KEY, userId+"");

            // 设置时间窗口
            redisTemplate.opsForValue().set(timeWindowKey, "1");
            redisTemplate.expire(timeWindowKey, TIME_WINDOW_DURATION, TimeUnit.SECONDS);

            log.info("用户 {} 秒杀成功，剩余库存：{}", userId, stock - 1);
            return Result.success("恭喜秒杀成功！");

        } catch (InterruptedException e) {
            log.error("秒杀过程中发生中断", e);
            Thread.currentThread().interrupt(); // 重新设置中断状态
            throw new BusinessException("系统异常");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
