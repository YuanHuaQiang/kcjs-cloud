package com.kcjs.cloud.mysql.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息记录表
 */
@Data
@Entity
@Table(name = "message_log")
public class MessageLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String messageBody;

    private Integer status;

    private Integer retryCount;

    private Long createTime;

    private Long updateTime;

    private Long sendTime;

    private String errorMessage;

    private String messageType;

    private String correlationId;

    private Integer priority;

    private Integer isProcessed;

}