package com.kcjs.cloud.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

@Configuration
public class RequestLoggingFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Bean
    @Order(-1)
    public GlobalFilter logRequestFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            logger.info("Request method: {}, Request URI: {}", request.getMethod(), request.getURI());
            logger.info("Request headers: {}", request.getHeaders());

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("Response status: {}", exchange.getResponse().getStatusCode());
            }));
        };
    }
}
