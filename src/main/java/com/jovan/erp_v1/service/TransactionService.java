package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.TransactionMapper;
import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.model.Transaction;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.AccountRepository;
import com.jovan.erp_v1.repository.TransactionRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.TransactionRequest;
import com.jovan.erp_v1.response.TransactionResponse;
import com.jovan.erp_v1.util.DateValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

	private final TransactionRepository transactionRepository;
	private TransactionMapper transactionMapper;
	private final AccountRepository accountRepository;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public TransactionResponse create(TransactionRequest request) {
		validateBigDecimal(request.amount());
		validateTransactionType(request.transactionType());
		Account sourceAcc = validateSourceAccountId(request.sourceAccountId());
		Account targetAcc = validateTargetAccountId(request.targetAccountId());
		User user = validateUserId(request.userId());
		Transaction t = transactionMapper.toEntity(request, sourceAcc, targetAcc, user);
		Transaction saved = transactionRepository.save(t);
		return new TransactionResponse(saved);
	}

	@Transactional
	@Override
	public TransactionResponse update(Long id, TransactionRequest request) {
		if (!request.id().equals(id)) {
			throw new ValidationException("ID in path and body do not match");
		}
		Transaction t = transactionRepository.findById(id).orElseThrow(() -> new ValidationException("Transaction not found with id "+id));
		validateBigDecimal(request.amount());
		validateTransactionType(request.transactionType());
		Account sourceAcc = t.getSourceAccount();
		if(request.sourceAccountId() != null && (sourceAcc.getId() == null || !request.sourceAccountId().equals(sourceAcc.getId()))) {
			sourceAcc = validateSourceAccountId(request.sourceAccountId());
		}
		Account targetAcc = t.getTargetAccount();
		if(request.targetAccountId() != null && (targetAcc.getId() == null || !request.targetAccountId().equals(targetAcc.getId()))) {
			targetAcc = validateTargetAccountId(request.targetAccountId());
		}
		User user = t.getUser();
		if(request.userId() != null && (user.getId() == null || !request.userId().equals(user.getId()))) {
			user = validateUserId(request.userId());
		}
		transactionMapper.toEntityUpdate(t, request, sourceAcc, targetAcc, user);
		Transaction saved = transactionRepository.save(t);
		return new TransactionResponse(saved);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		if(!transactionRepository.existsById(id)) {
			throw new ValidationException("Transaction not found with id "+id);
		}
		transactionRepository.deleteById(id);
	}

	@Override
	public TransactionResponse findOne(Long id) {
		Transaction t = transactionRepository.findById(id).orElseThrow(() -> new ValidationException("Transaction not found with id "+id));
		return new TransactionResponse(t);
	}

	@Override
	public List<TransactionResponse> findAll() {
		List<Transaction> items = transactionRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("Transaction list is empty");
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByAmount(BigDecimal amount) {
		validateBigDecimal(amount);
		List<Transaction> items = transactionRepository.findByAmount(amount);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for amount %s found", amount);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByAmountGreaterThan(BigDecimal amount) {
		validateBigDecimal(amount);
		List<Transaction> items = transactionRepository.findByAmountGreaterThan(amount);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for amount greater than %s, found", amount);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByAmountLessThan(BigDecimal amount) {
		validateBigDecimalNonNegative(amount);
		List<Transaction> items = transactionRepository.findByAmountLessThan(amount);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for amount less than %s, found", amount);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByAmountBetween(BigDecimal amountMin, BigDecimal amountMax) {
		validateMinAndMax(amountMin, amountMax);
		List<Transaction> items = transactionRepository.findByAmountBetween(amountMin, amountMax);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for amount between %s and %s, found", amountMin,amountMax);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByTransactionDate(LocalDateTime transactionDate) {
		DateValidator.validateNotInPast(transactionDate, "Transaction-date");
		List<Transaction> items = transactionRepository.findByTransactionDate(transactionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Transction for transaction date %s, found", transactionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByTransactionDateAfter(LocalDateTime transactionDate) {
		DateValidator.validateNotInFuture(transactionDate, "Transaction-date after");
		List<Transaction> items = transactionRepository.findByTransactionDateAfter(transactionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Transaction for transaction date after %s, found", transactionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByTransactionDateBefore(LocalDateTime transactionDate) {
		DateValidator.validateNotInFuture(transactionDate, "Transaction-date before");
		List<Transaction> items = transactionRepository.findByTransactionDateBefore(transactionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Transaction for transaction date before %s, found", transactionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByTransactionDateBetween(LocalDateTime transactionDateStart,
			LocalDateTime transactionDateEnd) {
		DateValidator.validateRange(transactionDateStart, transactionDateEnd);
		List<Transaction> items = transactionRepository.findByTransactionDateBetween(transactionDateStart, transactionDateEnd);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Transaction for transaction date between %s and %s, found", 
					transactionDateStart.format(formatter), transactionDateEnd.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByTransactionType(TransactionType transactionType) {
		validateTransactionType(transactionType);
		List<Transaction> items = transactionRepository.findByTransactionType(transactionType);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for transaction-type %s, found", transactionType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByTransactionTypeAndTransactionDate(TransactionType transactionType,
			LocalDateTime transactionDate) {
		validateTransactionType(transactionType);
		DateValidator.validateNotNull(transactionDate, "Transaction-date");
		List<Transaction> items = transactionRepository.findByTransactionTypeAndTransactionDate(transactionType, transactionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Transaction for type %s and transaction-date %s, found", 
					transactionType,transactionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByTransactionTypeAndTransactionDateAfter(TransactionType transactionType,
			LocalDateTime transactionDate) {
		validateTransactionType(transactionType);
		DateValidator.validateNotInPast(transactionDate, "Transaction-date after");
		List<Transaction> items = transactionRepository.findByTransactionTypeAndTransactionDateAfter(transactionType, transactionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Transaction for type %s and transaction date after %s, found",
					transactionType,transactionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByTransactionTypeAndTransactionDateBefore(TransactionType transactionType,
			LocalDateTime transactionDate) {
		validateTransactionType(transactionType);
		DateValidator.validateNotInFuture(transactionDate, "Tranasction-date before");
		List<Transaction> items = transactionRepository.findByTransactionTypeAndTransactionDateBefore(transactionType, transactionDate);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Transaction for type %s and transaction date before %s, found",
					transactionType, transactionDate.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByTransactionTypeAndTransactionDateBetween(TransactionType transactionType,
			LocalDateTime transactionDateStart, LocalDateTime transactionDateEnd) {
		validateTransactionType(transactionType);
		DateValidator.validateRange(transactionDateStart, transactionDateEnd);
		List<Transaction> items = transactionRepository.findByTransactionTypeAndTransactionDateBetween(transactionType, transactionDateStart, transactionDateEnd);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Transaction for type %s and transaction date between %s and %s, found", 
					transactionType, transactionDateStart.format(formatter), transactionDateEnd.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByUserFirstNameContainingIgnoreCaseAndUserLastNameContainingIgnoreCase(
			String firstName, String lastName) {
		validateString(firstName);
		validateString(lastName);
		List<Transaction> items = transactionRepository.findByUserFirstNameContainingIgnoreCaseAndUserLastNameContainingIgnoreCase(firstName, lastName);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for user's first-name %s and last-name %s, found",
					firstName,lastName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByUserEmailLikeIgnoreCase(String userEmail) {
		validateString(userEmail);
		List<Transaction> items = transactionRepository.findByUserEmailLikeIgnoreCase(userEmail);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for user's email %s, found", userEmail);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findBySourceAccountId(Long sourceAccountId) {
		validateAccountId(sourceAccountId);
		List<Transaction> items = transactionRepository.findBySourceAccountId(sourceAccountId);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for source-account id %d, found", sourceAccountId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findBySourceAccountAccountNumberContainingIgnoreCase(String accountNumber) {
		validateString(accountNumber);
		List<Transaction> items = transactionRepository.findBySourceAccountAccountNumberContainingIgnoreCase(accountNumber);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for source-account number %s, found", accountNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findBySourceAccountAccountName(String accountName) {
		validateString(accountName);
		List<Transaction> items = transactionRepository.findBySourceAccountAccountName(accountName);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for source-account name %s, found", accountName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findBySourceAccountType(AccountType type) {
		validateAccountType(type);
		List<Transaction> items = transactionRepository.findBySourceAccountType(type);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for source-account type %s, found", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findBySourceAccountAccountNumberContainingIgnoreCaseAndSourceAccountAccountNameContainingIgnoreCase(
			String accountNumber, String accountName) {
		validateString(accountNumber);
		validateString(accountName);
		List<Transaction> items = transactionRepository.findBySourceAccountAccountNumberContainingIgnoreCaseAndSourceAccountAccountNameContainingIgnoreCase(accountNumber, accountName);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for source-account number %s and source-account name %s, found",
					accountNumber,accountName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByTargetAccountId(Long targetAccountId) {
		validateTargetAccountId(targetAccountId);
		List<Transaction> items = transactionRepository.findByTargetAccountId(targetAccountId);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for target-account id %d, found", targetAccountId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByTargetAccountAccountNumberContainingIgnoreCase(String accountNumber) {
		validateString(accountNumber);
		List<Transaction> items = transactionRepository.findByTargetAccountAccountNumberContainingIgnoreCase(accountNumber);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for target-account number %s, found", accountNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByTargetAccountAccountName(String accountName) {
		validateString(accountName);
		List<Transaction> items = transactionRepository.findByTargetAccountAccountName(accountName);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for target-account name %s, found", accountName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByTargetAccountType(AccountType type) {
		validateAccountType(type);
		List<Transaction> items = transactionRepository.findByTargetAccountType(type);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for target account type %s, found", type);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByTargetAccountAccountNumberContainingIgnoreCaseAndTargetAccountAccountNameContainingIgnoreCase(
			String accountNumber, String accountName) {
		validateString(accountNumber);
		validateString(accountName);
		List<Transaction> items = transactionRepository.findByTargetAccountAccountNumberContainingIgnoreCaseAndTargetAccountAccountNameContainingIgnoreCase(accountNumber, accountName);
		if(items.isEmpty()) {
			String msg = String.format("No Transaction for target-account number %s and target-account name %s, found",
					accountNumber,accountName);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByAmountBetweenAndTransactionDateBetween(BigDecimal min, BigDecimal max,
			LocalDateTime start, LocalDateTime end) {
		validateMinAndMax(min, max);
		DateValidator.validateRange(start, end);
		List<Transaction> items = transactionRepository.findByAmountBetweenAndTransactionDateBetween(min, max, start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Transaction for amount between %s and %s and for transaction date between %s and %s, found", 
					min,max,start.format(formatter),end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findByUserIdAndTransactionDateBetween(Long userId, LocalDateTime start,
			LocalDateTime end) {
		validateUserId(userId);
		DateValidator.validateRange(start, end);
		List<Transaction> items = transactionRepository.findByUserIdAndTransactionDateBetween(userId, start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Tranasaction for user-id %d and transaction date between %s and %s, found", 
					userId, start.format(formatter), end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionResponse> findBySourceAccountIdAndTransactionTypeAndTransactionDateBetween(Long accountId,
			TransactionType transactionType, LocalDateTime start, LocalDateTime end) {
		validateAccountId(accountId);
		validateTransactionType(transactionType);
		DateValidator.validateRange(start, end);
		List<Transaction> items = transactionRepository.findBySourceAccountIdAndTransactionTypeAndTransactionDateBetween(accountId, transactionType, start, end);
		if(items.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No Transaction for account-id %d, transaction-type %s, and transaction date between %s and %s, found",
					accountId,transactionType,start.format(formatter), end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public BigDecimal sumOfOutgoingTransactions(Long accountId) {
		validateAccountId(accountId);
		return transactionRepository.sumOfOutgoingTransactions(accountId);
	}

	@Override
	public BigDecimal sumOfIncomingTransactions(Long accountId) {
		validateAccountId(accountId);
		return transactionRepository.sumOfIncomingTransactions(accountId);
	}

	@Override
	public boolean existsBySourceAccount_AccountNumberContainingIgnoreCase(String accountNumber) {
		validateString(accountNumber);
		return transactionRepository.existsBySourceAccount_AccountNumberContainingIgnoreCase(accountNumber);
	}

	@Override
	public boolean existsBySourceAccount_AccountNameContainingIgnoreCase(String accountName) {
		validateString(accountName);
		return transactionRepository.existsBySourceAccount_AccountNameContainingIgnoreCase(accountName);
	}

	@Override
	public boolean existsByTargetAccount_AccountNumberContainingIgnoreCase(String accountNumber) {
		validateString(accountNumber);
		return transactionRepository.existsByTargetAccount_AccountNumberContainingIgnoreCase(accountNumber);
	}

	@Override
	public boolean existsByTargetAccount_AccountNameContainingIgnoreCase(String accountName) {
		validateString(accountName);
		return transactionRepository.existsByTargetAccount_AccountNameContainingIgnoreCase(accountName);
	}

	@Override
	public boolean existsBySourceAccountIdAndTargetAccountId(Long sourceId, Long targetId) {
		validateSourceAccountId(sourceId);
		validateTargetAccountId(targetId);
		return transactionRepository.existsBySourceAccountIdAndTargetAccountId(sourceId, targetId);
	}
	
	
	private void validateTransactionType(TransactionType transactionType) {
		Optional.ofNullable(transactionType)
			.orElseThrow(() -> new ValidationException("TransactionType transactionType must not be null"));
	}
	
	private void validateAccountType(AccountType type) {
		Optional.ofNullable(type)
			.orElseThrow(() -> new ValidationException("AccountType type must not be null"));
	}
	
	private void validateString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Tekstualni karakter ne sme biti null ili prazan");
        }
    }

    private void validateMinAndMax(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("Min i Max ne smeju biti null");
        }

        if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Min mora biti >= 0, a Max mora biti > 0");
        }

        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Min ne može biti veći od Max");
        }
    }
    
    private void validateBigDecimal(BigDecimal num) {
        if (num == null || num.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Mora biti pozitivan broj");
        }
    }
    
    private Account validateSourceAccountId(Long sourceAccId) {
    	if(sourceAccId == null) {
    		throw new ValidationException("SourceAccount ID must not be null");
    	}
    	return accountRepository.findById(sourceAccId).orElseThrow(() -> new ValidationException("SourceAccount not found with id "+sourceAccId));
    }
    
    private Account validateTargetAccountId(Long targetAccId) {
    	if(targetAccId == null) {
    		throw new ValidationException("TargetAccount ID must not be null");
    	}
    	return accountRepository.findById(targetAccId).orElseThrow(() -> new ValidationException("TargetAccount not found with id "+targetAccId));
    }
    
    private User validateUserId(Long userId) {
    	if(userId == null) {
    		throw new ValidationException("User ID must not be null");
    	}
    	return userRepository.findById(userId).orElseThrow(() -> new ValidationException("User not found with id "+userId));
    }
    
    private Account validateAccountId(Long accId) {
    	if(accId == null) {
    		throw new ValidationException("Account id must not be null");
    	}
    	return accountRepository.findById(accId).orElseThrow(() -> new ValidationException("Account not found with id "+accId));
    }
    
    private void validateBigDecimalNonNegative(BigDecimal num) {
		if (num == null || num.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Number must be zero or positive");
		}
		if (num.scale() > 2) {
			throw new ValidationException("Cost must have at most two decimal places.");
		}
	}
}
