package com.kcjs.cloud.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements WebFilter {
    @Value("${jwt.secret}")
    private String SECRET;

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
            Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            System.out.println("当前用户：" + claims.getSubject());

            // 可将用户信息添加到请求头，传递给微服务
            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-User", claims.getSubject()) // 传递用户信息
                    .build();
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}