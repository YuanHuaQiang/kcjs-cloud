package com.kcjs.cloud.provider.service.account;


import com.kcjs.cloud.api.account.AccountService;
import com.kcjs.cloud.exception.BusinessException;
import com.kcjs.cloud.mysql.pojo.Account;
import com.kcjs.cloud.mysql.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.random.RandomGenerator;

@DubboService
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public void decrease(Long userId, BigDecimal amount) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (amount == null) {
            throw new BusinessException("扣减金额不能为空");
        }
        if (amount.signum() <= 0) {
            throw new BusinessException("扣减金额必须大于0");
        }
        // 统一金额精度到两位小数
        amount = amount.setScale(2, RoundingMode.HALF_UP);

        Account account = accountRepository.findByUserId(userId);
        if (account == null) {
//            throw new BusinessException("账户不存在");
            account = new Account();
            account.setUserId(userId);
            account.setResidue(BigDecimal.valueOf(RandomGenerator.getDefault().nextDouble(1000)));
            account.setTotal(account.getResidue());
            account.setUsed(BigDecimal.ZERO);
            accountRepository.save(account);
        }


        int updated = accountRepository.atomicDecrease(userId, amount);
        if (updated == 0) {
            throw new BusinessException("余额不足");
        }
    }
}
