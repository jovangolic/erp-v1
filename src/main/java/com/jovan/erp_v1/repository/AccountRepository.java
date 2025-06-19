package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.enumeration.AccountType;
import java.math.BigDecimal;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByType(AccountType type);

    // Pronalazi račun po nazivu
    Optional<Account> findByAccountName(String accountName);

    // Case-insensitive pretraga po nazivu računa
    Optional<Account> findByAccountNameIgnoreCase(String accountName);

    // Pronalazi račun po broju računa
    Optional<Account> findByAccountNumber(String accountNumber);

    // Kombinovana pretraga po nazivu i broju računa
    Optional<Account> findByAccountNameAndAccountNumber(String accountName, String accountNumber);

    // Pronalazi račune sa tačno određenim stanjem
    List<Account> findByBalance(BigDecimal balance);

    // Pronalazi račune sa balansom unutar opsega
    List<Account> findByBalanceBetween(BigDecimal min, BigDecimal max);

    // Pronalazi sve račune sa balansom većim od zadatog
    List<Account> findByBalanceGreaterThan(BigDecimal amount);

    // Pronalazi sve račune sa balansom manjim od zadatog
    List<Account> findByBalanceLessThan(BigDecimal amount);
}
