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
    	if (accountRepository.existsByAccountNumber(request.accountNumber())) {
    	    throw new IllegalArgumentException("Račun sa datim brojem već postoji.");
    	}
    	validateDoubleString(request.accountName(), request.accountNumber());
    	validAccountType(request.type());
    	validateBigDecimal(request.balance());
        Account acc = accountMapper.toEntity(request);
        Account saved = accountRepository.save(acc);
        return accountMapper.toResponse(saved);
    }

    @Transactional
    @Override
    public AccountResponse update(Long id, AccountRequest request) {
    	if (!request.id().equals(id)) {
			throw new IllegalArgumentException("ID in path and body do not match");
		}
    	Account existing = accountRepository.findById(request.id())
    	        .orElseThrow(() -> new AccountNotFoundErrorException("Nalog nije pronađen"));
    	    // Ako korisnik pokušava da postavi broj koji već koristi drugi nalog
    	if (!existing.getAccountNumber().equals(request.accountNumber()) &&
    	      accountRepository.existsByAccountNumber(request.accountNumber())) {
    	      throw new IllegalArgumentException("Račun sa datim brojem već postoji.");
    	}

    	validateDoubleString(request.accountName(), request.accountNumber());
    	validAccountType(request.type());
    	validateBigDecimal(request.balance());
    	Account updated = accountMapper.toEntity(request);
    	updated.setId(existing.getId()); // da se ne bi kreirao novi entitet
    	Account saved = accountRepository.save(updated);
    	return accountMapper.toResponse(saved);
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
    	validAccountType(type);
        return accountRepository.findByType(type).stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponse findByAccountName(String accountName) {
    	validateString(accountName);
        Account acc = accountRepository.findByAccountName(accountName)
                .orElseThrow(() -> new AccountNotFoundErrorException("Account name not found"));
        return new AccountResponse(acc);
    }

    @Override
    public AccountResponse findByAccountNameIgnoreCase(String accountName) {
    	validateString(accountName);
        Account acc = accountRepository.findByAccountNameIgnoreCase(accountName)
                .orElseThrow(() -> new AccountNotFoundErrorException("Account name not found"));
        return new AccountResponse(acc);
    }

    @Override
    public AccountResponse findByAccountNumber(String accountNumber) {
    	validateString(accountNumber);
        Account acc = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundErrorException("Account number not found"));
        return new AccountResponse(acc);
    }

    @Override
    public AccountResponse findByAccountNameAndAccountNumber(String accountName, String accountNumber) {
    	validateDoubleString(accountName, accountNumber);
        Account acc = accountRepository.findByAccountNameAndAccountNumber(accountName, accountNumber)
                .orElseThrow(() -> new AccountNotFoundErrorException("Name and number for account not found"));
        return new AccountResponse(acc);
    }

    @Override
    public List<AccountResponse> findByBalance(BigDecimal balance) {
    	validateBigDecimal(balance);
        return accountRepository.findByBalance(balance).stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponse> findByBalanceBetween(BigDecimal min, BigDecimal max) {
    	validateMinAndMax(min, max);
        return accountRepository.findByBalanceBetween(min, max).stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponse> findByBalanceGreaterThan(BigDecimal amount) {
    	validateBigDecimal(amount);
        return accountRepository.findByBalanceGreaterThan(amount).stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponse> findByBalanceLessThan(BigDecimal amount) {
    	validateBigDecimal(amount);
        return accountRepository.findByBalanceLessThan(amount).stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }
    
    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Balance mora biti pozitivan broj.");
        }
    }
    
    private void validateDoubleString(String s1, String s2) {
        if (s1 == null || s1.trim().isEmpty() || s2 == null || s2.trim().isEmpty()) {
            throw new IllegalArgumentException("Oba stringa moraju biti ne-null i ne-prazna");
        }
    }
    
    private void validateMinAndMax(BigDecimal min, BigDecimal max) {
        if (min == null || min.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Minimalna vrednost mora biti veća od nule.");
        }
        if (max == null || max.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Maksimalna vrednost mora biti veća od nule.");
        }
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Minimalna vrednost ne sme biti veća od maksimalne.");
        }
    }

    private void validateString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Tekstualni karakter ne sme biti null ili prazan");
        }
    }
    
    private void validAccountType(AccountType type) {
    	if(type == null) {
    		throw new IllegalArgumentException("Tip za AccountType ne sme biti null");
    	}
    }
    
}
