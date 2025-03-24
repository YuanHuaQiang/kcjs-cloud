package com.kcjs.cloud.provider.service.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DriverWebSocketHandler extends TextWebSocketHandler {

    // 用来存储所有在线司机 session，key 可以是司机ID
    private static final Map<String, WebSocketSession> DRIVER_SESSION_MAP = new ConcurrentHashMap<>();

    // 建立连接时
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String driverId = getDriverId(session);
        DRIVER_SESSION_MAP.put(driverId, session);
        log.info("司机 [{}] 已连接！", driverId);
    }

    // 收到客户端消息
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String driverId = getDriverId(session);
        String payload = message.getPayload();
        log.info("收到司机 [{}] 的消息: {}", driverId, payload);

        // 可以回复，也可以交由别的服务处理
        session.sendMessage(new TextMessage("收到你的消息啦！"));
    }

    // 连接断开
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String driverId = getDriverId(session);
        DRIVER_SESSION_MAP.remove(driverId);
        log.info("司机 [{}] 已断开！", driverId);
    }

    // 发送消息给指定司机
    public void sendMessageToDriver(String driverId, String message) throws Exception {
        WebSocketSession session = DRIVER_SESSION_MAP.get(driverId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
            log.info("消息已发送给司机 [{}]: {}", driverId, message);
        } else {
            log.warn("司机 [{}] 不在线，无法发送消息", driverId);
        }
    }

    // 这里假设前端在 URL 上带 driverId，例如 ws://host/ws/driver?driverId=xxx
    private String getDriverId(WebSocketSession session) {
        return session.getUri().getQuery().replace("driverId=", "");
    }
}