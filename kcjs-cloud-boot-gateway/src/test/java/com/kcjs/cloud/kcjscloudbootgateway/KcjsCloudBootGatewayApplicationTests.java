package com.kcjs.cloud.kcjscloudbootgateway;

import com.kcjs.cloud.gateway.KcjsCloudBootGatewayApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;
import java.util.concurrent.TimeUnit;


@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = KcjsCloudBootGatewayApplication.class)
class KcjsCloudBootGatewayApplicationTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Test
    void redissonTest() throws InterruptedException {
        Set<String> keys = redisTemplate.keys("*");
        log.info("keys: {}", keys);

        RLock lock = redissonClient.getLock("lock:product:1001");

        boolean locked = lock.tryLock(2, 20, TimeUnit.SECONDS);

        if (!locked) {
            log.info("获取锁失败");
            return;
        }

        log.info("获取锁成功");
        Thread.sleep(10000);
        lock.unlock();
        log.info("释放锁成功");
    }

}
