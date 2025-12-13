package com.kcjs.cloud.api.user;



import com.kcjs.cloud.mysql.pojo.UserInfo;

import java.util.List;

public interface UserInfoService {

    boolean mightContainUserId(Long userId);

    UserInfo getUserById(Long id);

    List<UserInfo> listUsers();

    boolean addUser(UserInfo userInfoDTO);
}