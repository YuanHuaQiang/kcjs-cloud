package com.kcjs.cloud.api.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoDTO implements Serializable {

    private Long id;

    private String username;

    private String email;

    private Integer age;
}