package com.kcjs.cloud.consumer.controller.seckill;

import com.kcjs.cloud.api.RedissonSeckillService;
import com.kcjs.cloud.exception.BusinessException;
import com.kcjs.cloud.result.Result;
import com.kcjs.cloud.api.account.AccountService;
import com.kcjs.cloud.api.order.OrderService;
import com.kcjs.cloud.api.storage.StorageService;
import com.kcjs.cloud.mysql.pojo.Order;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/redisson/seckill")
public class RedissonSeckillController {

    @DubboReference
    private RedissonSeckillService redissonSeckillService;


    @GetMapping("/buy")
    public Result<String> seckill(@RequestParam Long userId) {
        return  redissonSeckillService.seckillProduct(userId);
    }
}