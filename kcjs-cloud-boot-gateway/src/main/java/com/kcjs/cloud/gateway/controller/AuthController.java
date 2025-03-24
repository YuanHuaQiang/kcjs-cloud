package com.kcjs.cloud.gateway.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.kcjs.cloud.gateway.utils.JwtUtil;
import com.kcjs.cloud.oracle.pojo.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    // 模拟用户数据库
    private static final String MOCK_USERNAME = "admin";
    private static final String MOCK_PASSWORD = "123456";

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;


    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        // 校验用户名和密码
        if (MOCK_USERNAME.equals(request.getUsername()) && MOCK_PASSWORD.equals(request.getPassword())) {
            // 生成 JWT
            try {
                //生成uuid 保存到用户表中
                // 若须限制登录，将表中原有uuid数据从redis删除
                // 若有必要可将用户登录uuid 单独存表，强退或修改密码时删除所有缓存
                String uuid = UUID.randomUUID().toString().replace("-", "");

                String redisKey = "login_tokens:" + uuid;
                redisTemplate.opsForValue().set(redisKey, request, 30, TimeUnit.MINUTES);

                return jwtUtil.generateToken(uuid);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "用户名或密码错误！";
    }
}