package com.kcjs.cloud.provider.service.seckill;

import com.kcjs.cloud.api.RedissonSeckillService;
import com.kcjs.cloud.api.storage.StorageService;
import com.kcjs.cloud.api.user.UserInfoService;
import com.kcjs.cloud.exception.BusinessException;
import com.kcjs.cloud.mysql.pojo.MessageLog;
import com.kcjs.cloud.mysql.repository.MessageLogRepository;
import com.kcjs.cloud.provider.config.RabbitMQConfig;
import com.kcjs.cloud.provider.service.user.UserInfoServiceImpl;
import com.kcjs.cloud.result.Result;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@DubboService
@RequiredArgsConstructor
public class RedissonSeckillServiceImpl implements RedissonSeckillService {


    private final StorageService storageService;
    private final UserInfoService userInfoService;

    private final StringRedisTemplate redisTemplate;
    private final RedissonClient redissonClient;
    private final RabbitTemplate rabbitTemplate;
    private final MessageLogRepository messageLogRepository;

    private final ExecutorService seckillExecutor;

    private static final String STOCK_KEY = "seckill:stock:1001";
    private static final String TIME_WINDOW_KEY_PREFIX = "seckill:time-window:";
    private static final long TIME_WINDOW_DURATION = 60; // 时间窗口持续时间，单位秒

    @PostConstruct
    public void init() {
        redisTemplate.opsForValue().set(STOCK_KEY, "2000");
        storageService.save(1001L,2000);
    }

    @Override
    @Transactional
    public Result<String> seckillProduct(String userId, String productId) {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("lua/seckill.lua"));
        script.setResultType(Long.class);

        String stockKey = "seckill:stock:" + productId;
        String userKey = "seckill:users:" + productId;

        try {
            Long result = redisTemplate.execute(
                    script,
                    Arrays.asList(stockKey, userKey),
                    userId, productId
            );

            if (result == null) {
                log.error("秒杀脚本返回null，userId: {}, productId: {}", userId, productId);
                return Result.fail("秒杀失败，请重试");
            }

            if (result == -1L) {
                return Result.fail("您已秒杀过");
            } else if (result == 0L) {
                return Result.fail("库存不足");
            } else if (result != 1L) {  // 捕获意外返回值
                log.error("秒杀脚本执行异常，返回值: {}，userId: {}, productId: {}", result, userId, productId);
                return Result.fail("秒杀失败，请重试");
            }

            // 成功就保存消息到数据库，由定时任务发送
            String bizKey = "seckill:" + userId + ":" + productId;
            MessageLog messageLog = new MessageLog();
            messageLog.setMessageBody(userId + ":" + productId);
            messageLog.setStatus(0); // 0: Pending
            messageLog.setRetryCount(0);
            messageLog.setCreateTime(System.currentTimeMillis());
            messageLog.setUpdateTime(System.currentTimeMillis());
            messageLog.setMessageType("seckill");
            messageLog.setCorrelationId(bizKey);
            messageLog.setPriority(1);
            messageLog.setIsProcessed(0);

            messageLogRepository.save(messageLog);

            return Result.success("恭喜秒杀成功！");
        } catch (Exception e) {
            log.error("执行秒杀Lua脚本异常，userId: {}, productId: {}", userId, productId, e);
            return Result.fail("系统繁忙，请稍后再试");
        }
    }
}
