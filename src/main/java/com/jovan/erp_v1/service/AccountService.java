package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.exception.AccountNotFoundErrorException;
import com.jovan.erp_v1.mapper.AccountMapper;
import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.repository.AccountRepository;
import com.jovan.erp_v1.request.AccountRequest;
import com.jovan.erp_v1.response.AccountResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional
    @Override
    public AccountResponse create(AccountRequest request) {
        Account acc = accountMapper.toEntity(request);
        Account saved = accountRepository.save(acc);
        return accountMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public AccountResponse update(Long id, AccountRequest request) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundErrorException("Account not found  with id: " + id));
        accountMapper.updateEntity(acc, request);
        return accountMapper.toResponse(accountRepository.save(acc));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new AccountNotFoundErrorException("Account not found " + id);
        }
        accountRepository.deleteById(id);
    }

    @Override
    public AccountResponse findOne(Long id) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundErrorException("Account not found  with id: " + id));
        return new AccountResponse(acc);
    }

    @Override
    public List<AccountResponse> findAll() {
        return accountRepository.findAll().stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponse> findByType(AccountType type) {
        return accountRepository.findByType(type).stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponse findByAccountName(String accountName) {
        Account acc = accountRepository.findByAccountName(accountName)
                .orElseThrow(() -> new AccountNotFoundErrorException("Account name not found"));
        return new AccountResponse(acc);
    }

    @Override
    public AccountResponse findByAccountNameIgnoreCase(String accountName) {
        Account acc = accountRepository.findByAccountNameIgnoreCase(accountName)
                .orElseThrow(() -> new AccountNotFoundErrorException("Account name not found"));
        return new AccountResponse(acc);
    }

    @Override
    public AccountResponse findByAccountNumber(String accountNumber) {
        Account acc = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundErrorException("Account number not found"));
        return new AccountResponse(acc);
    }

    @Override
    public AccountResponse findByAccountNameAndAccountNumber(String accountName, String accountNumber) {
        Account acc = accountRepository.findByAccountNameAndAccountNumber(accountName, accountNumber)
                .orElseThrow(() -> new AccountNotFoundErrorException("Name and number for account not found"));
        return new AccountResponse(acc);
    }

    @Override
    public List<AccountResponse> findByBalance(BigDecimal balance) {
        return accountRepository.findByBalance(balance).stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponse> findByBalanceBetween(BigDecimal min, BigDecimal max) {
        return accountRepository.findByBalanceBetween(min, max).stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponse> findByBalanceGreaterThan(BigDecimal amount) {
        return accountRepository.findByBalanceGreaterThan(amount).stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponse> findByBalanceLessThan(BigDecimal amount) {
        return accountRepository.findByBalanceLessThan(amount).stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }
}
