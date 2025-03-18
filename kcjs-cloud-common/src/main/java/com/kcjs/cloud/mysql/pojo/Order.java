package com.kcjs.cloud.mysql.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long productId;

    private Integer count;

    private BigDecimal amount;

    private Integer status;

    @Column(name = "create_time", insertable = false, updatable = false)
    private LocalDateTime createTime;
}