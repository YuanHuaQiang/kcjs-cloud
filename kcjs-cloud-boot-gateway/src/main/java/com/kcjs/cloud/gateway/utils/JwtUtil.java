package com.kcjs.cloud.gateway.utils;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET;



    // 生成 JWT
    public  String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 小时过期
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    // 解析 JWT
    public  String parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private static String getSigningKey() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        return Encoders.BASE64.encode(key.getEncoded());
    }


}