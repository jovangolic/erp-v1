package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.enumeration.AccountType;
import java.math.BigDecimal;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    List<Account> findByType(AccountType type);

    Optional<Account> findByAccountName(String accountName);
    Optional<Account> findByAccountNameIgnoreCase(String accountName);

    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNameAndAccountNumber(String accountName, String accountNumber);
    List<Account> findByBalance(BigDecimal balance);
    List<Account> findByBalanceBetween(BigDecimal min, BigDecimal max);
    List<Account> findByBalanceGreaterThan(BigDecimal amount);
    List<Account> findByBalanceLessThan(BigDecimal amount);
    
    boolean existsByAccountNumber(String accountNumber);
    
    //nove metode
    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.sourceTransactions WHERE a.id = :id")
    List<Account> trackAccountSourceTransactions(@Param("id") Long id);
    
    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.targetTransactions WHERE a.id = :id")
    List<Account> trackAccountTargetTransactions(@Param("id") Long id);
}
