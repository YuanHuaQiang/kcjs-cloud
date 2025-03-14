package com.kcjs.cloud.gateway.controller;

import com.kcjs.cloud.gateway.utils.JwtUtil;
import com.kcjs.cloud.oracle.pojo.LoginRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    // 模拟用户数据库
    private static final String MOCK_USERNAME = "admin";
    private static final String MOCK_PASSWORD = "123456";

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        // 校验用户名和密码
        if (MOCK_USERNAME.equals(request.getUsername()) && MOCK_PASSWORD.equals(request.getPassword())) {
            // 生成 JWT
            try {
                return jwtUtil.generateToken(request.getUsername());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "用户名或密码错误！";
    }
}