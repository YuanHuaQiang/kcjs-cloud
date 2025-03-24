package com.kcjs.cloud.gateway.filter;

import com.kcjs.cloud.gateway.utils.JwtUtil;
import com.kcjs.cloud.oracle.pojo.LoginRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements WebFilter {
    @Value("${jwt.secret}")
    private String SECRET;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtUtil jwtUtil;



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 允许登录请求直接通过
        if (request.getURI().getPath().equals("/auth/login")) {
            return chain.filter(exchange);
        }

        // 获取 Authorization 头部
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            // 解析 JWT
            String token = authHeader.substring(7);
            String uuid = jwtUtil.parseToken(token);
            String redisKey = "login_tokens:" + uuid;

            // 2. 获取用户信息
            LoginRequest loginUser = (LoginRequest) redisTemplate.opsForValue().get(redisKey);

            if (loginUser != null) {
                // 3. 刷新有效期（滑动过期）
                redisTemplate.expire(redisKey, 30, TimeUnit.MINUTES);
            }else{
                return unauthorizedResponse(exchange, "Token 已过期");
            }

            // 可将用户信息添加到请求头，传递给微服务
            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-User", loginUser.getUsername())
                    .build();
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (ExpiredJwtException e) {
            return unauthorizedResponse(exchange, "Token 已过期");
        } catch (SignatureException e) {
            return unauthorizedResponse(exchange, "Token 签名无效");
        } catch (Exception e) {
            return unauthorizedResponse(exchange, "Token 无效");
        }
    }


    /**
     * 友好返回提示信息
     * @param exchange
     * @param message
     * @return
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 自定义返回体
        String body = String.format("{\"code\":401, \"message\":\"%s\"}", message);

        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}