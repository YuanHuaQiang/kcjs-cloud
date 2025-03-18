package com.kcjs.cloud.api.account;


import java.math.BigDecimal;

public interface AccountService {

    void decrease(Long userId, BigDecimal amount) ;
}