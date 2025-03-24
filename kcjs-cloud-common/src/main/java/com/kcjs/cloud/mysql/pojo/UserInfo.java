package com.kcjs.cloud.mysql.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_info")
public class UserInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false, length = 50)
    private String username;

    @Column( length = 100)
    private String email;

    private Integer age;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createTime;

    @Column( updatable = false)
    private LocalDateTime updateTime;
}