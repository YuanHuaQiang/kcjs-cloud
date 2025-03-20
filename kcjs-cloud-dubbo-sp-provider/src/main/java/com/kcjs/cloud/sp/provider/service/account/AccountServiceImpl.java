package com.kcjs.cloud.sp.provider.service.account;


import com.kcjs.cloud.api.account.AccountService;
import com.kcjs.cloud.exception.BusinessException;
import com.kcjs.cloud.mysql.pojo.Account;
import com.kcjs.cloud.mysql.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.random.RandomGenerator;

@DubboService
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public void decrease(Long userId, BigDecimal amount) {
        Account account = accountRepository.findByUserId(userId);
        if (account == null) {
//            throw new RuntimeException("账户不存在");
            account = new Account();
            account.setUserId(userId);
            account.setResidue(BigDecimal.valueOf(RandomGenerator.getDefault().nextDouble(1000)));
            account.setTotal(account.getResidue());
            account.setUsed(BigDecimal.ZERO);
            accountRepository.save(account);
        }


        if (account.getResidue().compareTo(amount) < 0) {
            throw new BusinessException("余额不足");
        }

        account.setUsed(account.getUsed().add(amount));
        account.setResidue(account.getResidue().subtract(amount));

        accountRepository.save(account);
    }
}