package com.kcjs.cloud.consumer.controller;

import com.kcjs.cloud.provider.service.RedissonSeckillService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redisson/seckill")
public class RedissonSeckillController {

    @DubboReference
    private RedissonSeckillService redissonSeckillService;

    @GetMapping("/buy")
    public String seckill(@RequestParam Long userId) {
        return redissonSeckillService.seckillProduct(userId);
    }
}