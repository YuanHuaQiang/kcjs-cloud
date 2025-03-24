package com.kcjs.cloud.provider.config;

import com.kcjs.cloud.provider.service.ws.DriverWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final DriverWebSocketHandler driverWebSocketHandler;

    public WebSocketConfig(DriverWebSocketHandler driverWebSocketHandler) {
        this.driverWebSocketHandler = driverWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(driverWebSocketHandler, "/ws/driver")
                .setAllowedOrigins("*"); // 允许跨域，实际使用注意安全
    }
}