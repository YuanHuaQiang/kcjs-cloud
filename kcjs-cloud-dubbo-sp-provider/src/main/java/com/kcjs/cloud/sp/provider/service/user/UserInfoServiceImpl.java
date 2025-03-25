package com.kcjs.cloud.sp.provider.service.user;


import com.kcjs.cloud.api.user.UserInfoService;
import com.kcjs.cloud.exception.BusinessException;
import com.kcjs.cloud.mysql.pojo.UserInfo;
import com.kcjs.cloud.mysql.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;
import java.util.Optional;

@DubboService
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository userInfoRepository;

    @Override
    public UserInfo getUserById(Long id) {
        Optional<UserInfo> optional = userInfoRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            // 可以抛一个明确的异常，或者返回 null、封装成业务响应
            throw new BusinessException("用户ID不存在");
        }
    }

    @Override
    public List<UserInfo> listUsers() {
        return userInfoRepository.findAll();
    }

    @Override
    public boolean addUser(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
        return true;
    }
}