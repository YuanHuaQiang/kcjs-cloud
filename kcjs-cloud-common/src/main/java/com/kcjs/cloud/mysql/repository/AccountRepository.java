package com.kcjs.cloud.mysql.repository;


import com.kcjs.cloud.mysql.pojo.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUserId(Long userId);
}