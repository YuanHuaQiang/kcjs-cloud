package com.kcjs.cloud.mysql.repository;


import com.kcjs.cloud.mysql.pojo.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    // 可以加自定义查询方法
    UserInfo findByUsername(String username);
}