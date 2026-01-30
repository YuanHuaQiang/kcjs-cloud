package com.kcjs.cloud.mysql.repository;


import com.kcjs.cloud.mysql.pojo.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUserId(Long userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Account a set a.used = a.used + :amount, a.residue = a.residue - :amount " +
            "where a.userId = :userId and a.residue >= :amount")
    int atomicDecrease(@Param("userId") Long userId, @Param("amount") java.math.BigDecimal amount);
}
