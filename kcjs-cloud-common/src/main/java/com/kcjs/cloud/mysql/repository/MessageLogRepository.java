package com.kcjs.cloud.mysql.repository;


import com.kcjs.cloud.mysql.pojo.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageLogRepository extends JpaRepository<MessageLog, Long> {
    List<MessageLog> findByStatusAndMessageType(Integer status, String messageType);

    MessageLog findByCorrelationId(String correlationId);
}