package com.kcjs.cloud.consumer.controller.user;


import com.kcjs.cloud.api.user.UserInfoService;
import com.kcjs.cloud.mysql.pojo.UserInfo;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserInfoController {

    @DubboReference
    private final UserInfoService userInfoService;

    @GetMapping("/{id}")
    public UserInfo getUser(@PathVariable Long id) {
        return userInfoService.getUserById(id);
    }

    @GetMapping("/list")
    public List<UserInfo> listUsers() {
        return userInfoService.listUsers();
    }

    @PostMapping("/add")
    public boolean addUser(@RequestBody UserInfo dto) {
        return userInfoService.addUser(dto);
    }
}