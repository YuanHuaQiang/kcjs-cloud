package com.kcjs.cloud.mysql.repository;


import com.kcjs.cloud.mysql.pojo.Account;
import com.kcjs.cloud.mysql.pojo.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageLogRepository extends JpaRepository<MessageLog, Long> {
}