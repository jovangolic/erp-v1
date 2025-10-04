package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.AccountStatus;
import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.exception.AccountNotFoundErrorException;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.AccountMapper;
import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.repository.AccountRepository;
import com.jovan.erp_v1.repository.specification.AccountSpecification;
import com.jovan.erp_v1.request.AccountRequest;
import com.jovan.erp_v1.response.AccountResponse;
import com.jovan.erp_v1.response.AccountWithTransactionsResponse;
import com.jovan.erp_v1.save_as.AbstractSaveAllService;
import com.jovan.erp_v1.save_as.AbstractSaveAsService;
import com.jovan.erp_v1.save_as.AccountSaveAsRequest;
import com.jovan.erp_v1.search_request.AccountSearchRequest;

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
			throw new ValidationException("ID in path and body do not match");
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
    
    // Vraca jedan racun sa svim transakcijama - za detaljan prikaz
    @Override
	public AccountWithTransactionsResponse findOneWithTransactions(Long id) {
    	Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Account not found with id " + id));
        return accountMapper.toResponseDetailed(acc);
	}

    // Vraca sve racune (bez transakcija) - za listu u UI
    @Override
    public List<AccountResponse> findAll() {
    	List<Account> items = accountRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("List for accounts is empty");
    	}
        return items.stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponse> findByType(AccountType type) {
    	validAccountType(type);
    	List<Account> items = accountRepository.findByType(type);
    	if(items.isEmpty()) {
    		String msg = String.format("No account for type equal to %s is found", type);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
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
    	List<Account> items = accountRepository.findByBalance(balance);
    	if(items.isEmpty()) {
    		String msg = String.format("Account for balance %s is not found",balance);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponse> findByBalanceBetween(BigDecimal min, BigDecimal max) {
    	validateMinAndMax(min, max);
    	List<Account> items = accountRepository.findByBalanceBetween(min, max);
    	if(items.isEmpty()) {
    		String msg = String.format("Account with balance between %s and %s is not found", min,max);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponse> findByBalanceGreaterThan(BigDecimal amount) {
    	validateBigDecimal(amount);
    	List<Account> items = accountRepository.findByBalanceGreaterThan(amount);
    	if(items.isEmpty()) {
    		String msg = String.format("No account with balance greater than %s is found", amount);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponse> findByBalanceLessThan(BigDecimal amount) {
    	validateBigDecimalNonNegative(amount);
    	List<Account> items = accountRepository.findByBalanceLessThan(amount);
    	if(items.isEmpty()) {
    		String msg = String.format("No account with balance less than %s is found", amount);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }
    
    @Transactional
    @Override
	public AccountResponse confirmAccount(Long id) {
    	Account acc = accountRepository.findById(id).orElseThrow(() -> new ValidationException("Account not found with id "+id));
    	acc.setConfirmed(true);
    	acc.setStatus(AccountStatus.CONFIRMED);
    	accountRepository.save(acc);
		return new AccountResponse(acc);
	}

    @Transactional
	@Override
	public AccountResponse cancelAccount(Long id) {
    	Account acc = accountRepository.findById(id).orElseThrow(() -> new ValidationException("Account not found with id "+id));
    	if(acc.getStatus() != AccountStatus.NEW && acc.getStatus() != AccountStatus.CONFIRMED) {
    		throw new ValidationException("Only NEW or CONFIRMED defects can be cancelled");
    	}
    	acc.setStatus(AccountStatus.CANCELLED);
		return new AccountResponse(accountRepository.save(acc));
	}

    @Transactional
	@Override
	public AccountResponse closeAccount(Long id) {
    	Account acc = accountRepository.findById(id).orElseThrow(() -> new ValidationException("Account not found with id "+id));
    	if(acc.getStatus() != AccountStatus.CONFIRMED) {
    		throw new ValidationException("Only CONFIRMED defects can be closed");
    	}
    	acc.setStatus(AccountStatus.CLOSED);
		return new AccountResponse(accountRepository.save(acc));
	}

    @Transactional
	@Override
	public AccountResponse changeStatus(Long id, AccountStatus status) {
    	Account acc = accountRepository.findById(id).orElseThrow(() -> new ValidationException("Account not found with id "+id));
    	validateAccountStatus(status);
    	if(acc.getStatus() == AccountStatus.CLOSED) {
    		throw new ValidationException("Closed accounts cannot change status");
    	}
    	if(status == AccountStatus.CONFIRMED) {
    		if(acc.getStatus() != AccountStatus.NEW) {
    			throw new ValidationException("Only NEW accounts can be confirmed");
    		}
    		acc.setConfirmed(true);
    	}
    	acc.setStatus(status);
		return new AccountResponse(accountRepository.save(acc));
	}

    @Transactional(readOnly = true)
	@Override
	public AccountResponse trackAccountSourceTransactions(Long id) {
    	List<Account> items = accountRepository.trackAccountSourceTransactions(id);
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("Source-account with id " + id + " not found");
    	}
    	Account source = items.get(0);
		return new AccountResponse(source);
	}

    @Transactional(readOnly = true)
	@Override
	public AccountResponse trackAccountTargetTransactions(Long id) {
		List<Account> items = accountRepository.trackAccountTargetTransactions(id);
		if(items.isEmpty()) {
			throw new NoDataFoundException("Target-accont with id " + id +" not found");
		}
		Account target = items.get(0);
		return new AccountResponse(target);
	}

	@Override
	public List<AccountResponse> generalSearch(AccountSearchRequest request) {
		Specification<Account> spec = AccountSpecification.fromRequest(request);
		List<Account> items = accountRepository.findAll(spec);
		if(items.isEmpty()) {
			throw new NoDataFoundException("No Accounts found for given criteria");
		}
		return items.stream().map(accountMapper::toResponse).collect(Collectors.toList());
	}

    @Transactional
	@Override
	public AccountResponse saveAccount(AccountRequest request) {
		Account acc = Account.builder()
				.id(request.id())
				.accountName(request.accountName())
				.accountNumber(request.accountNumber())
				.type(request.type())
				.status(request.status())
				.balance(request.balance())
				.confirmed(request.confirmed())
				.build();
		Account saved = accountRepository.save(acc);
		return new AccountResponse(saved);
	}
    
    private final AbstractSaveAsService<Account, AccountResponse> saveAsHelper = new AbstractSaveAsService<Account, AccountResponse>() {
		
		@Override
		protected AccountResponse toResponse(Account entity) {
			return new AccountResponse(entity);
		}
		
		@Override
		protected JpaRepository<Account, Long> getRepository() {
			return accountRepository;
		}
		
		@Override
		protected Account copyAndOverride(Account source, Map<String, Object> overrides) {
			return Account.builder()
				.accountName((String) overrides.getOrDefault("Account-name", source.getAccountName()))
				.accountNumber((String) overrides.getOrDefault("Account-number", source.getAccountNumber()))
				.type(source.getType())
				.status(source.getStatus())
				.balance(source.getBalance())
				.confirmed(source.getConfirmed())
				.build();
		}
	};
	
	private final AbstractSaveAllService<Account, AccountResponse> saveAllHelper = new AbstractSaveAllService<Account, AccountResponse>() {
		
		@Override
		protected Function<Account, AccountResponse> toResponse() {
			return AccountResponse::new;
		}
		
		@Override
		protected JpaRepository<Account, Long> getRepository() {
			return accountRepository;
		}
	};

    @Transactional
	@Override
	public AccountResponse saveAs(AccountSaveAsRequest request) {
    	Map<String, Object> overrides = new HashMap<>();
    	if(request.accountName() != null) overrides.put("Account-name", request.accountName());
    	if(request.accountNumber() != null) overrides.put("Account-number", request.accountNumber());
		return saveAsHelper.saveAs(request.sourceId(), overrides);
	}

    @Transactional
	@Override
	public List<AccountResponse> saveAll(List<AccountRequest> request) {
		List<Account> items = request.stream()
				.map(req -> Account.builder()
						.accountName(req.accountName())
						.accountNumber(req.accountNumber())
						.balance(req.balance())
						.type(req.type())
						.status(req.status())
						.confirmed(req.confirmed())
						.build())
				.collect(Collectors.toList());
		return saveAllHelper.saveAll(items);
	}
    
    private void validateAccountStatus(AccountStatus status) {
    	Optional.ofNullable(status)
    		.orElseThrow(() -> new ValidationException("AccountStatus status must not be null"));
    }
    
    private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
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
