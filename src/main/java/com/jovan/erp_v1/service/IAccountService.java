package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.request.AccountRequest;
import com.jovan.erp_v1.response.AccountResponse;

public interface IAccountService {

    AccountResponse create(AccountRequest request);
    AccountResponse update(Long id, AccountRequest request);
    void delete(Long id);
    AccountResponse findOne(Long id);
    List<AccountResponse> findAll();
    List<AccountResponse> findByType(AccountType type);
    AccountResponse findByAccountName(String accountName);
    AccountResponse findByAccountNameIgnoreCase(String accountName);
    AccountResponse findByAccountNumber(String accountNumber);
    AccountResponse findByAccountNameAndAccountNumber(String accountName, String accountNumber);
    List<AccountResponse> findByBalance(BigDecimal balance);
    List<AccountResponse> findByBalanceBetween(BigDecimal min, BigDecimal max);
    List<AccountResponse> findByBalanceGreaterThan(BigDecimal amount);
    List<AccountResponse> findByBalanceLessThan(BigDecimal amount);
}
