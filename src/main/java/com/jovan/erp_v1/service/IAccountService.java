package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import com.jovan.erp_v1.enumeration.AccountStatus;
import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.request.AccountRequest;
import com.jovan.erp_v1.response.AccountResponse;
import com.jovan.erp_v1.response.AccountWithTransactionsResponse;
import com.jovan.erp_v1.save_as.AccountSaveAsRequest;
import com.jovan.erp_v1.search_request.AccountSearchRequest;

public interface IAccountService {

    AccountResponse create(AccountRequest request);
    AccountResponse update(Long id, AccountRequest request);
    void delete(Long id);
    //Light verzije
    AccountResponse findOne(Long id); //bez transakcija
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
    
    // Detailed verzije
    AccountWithTransactionsResponse findOneWithTransactions(Long id);
    
    //nove metode
    AccountResponse confirmAccount(Long id);
    AccountResponse cancelAccount(Long id);
    AccountResponse closeAccount(Long id);
    AccountResponse changeStatus(Long id, AccountStatus status);
    AccountResponse trackAccountSourceTransactions( Long id);
    AccountResponse trackAccountTargetTransactions( Long id);
    List<AccountResponse> generalSearch(AccountSearchRequest request);
    AccountResponse saveAccount(AccountRequest request);
    AccountResponse saveAs(AccountSaveAsRequest request);
    List<AccountResponse> saveAll(List<AccountRequest> request);
}
