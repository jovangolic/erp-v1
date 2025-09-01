package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.enumeration.PaymentMethod;
import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.TransactionMapper;
import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.model.Role;
import com.jovan.erp_v1.model.Transaction;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.AccountRepository;
import com.jovan.erp_v1.repository.TransactionRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.TransactionRequest;
import com.jovan.erp_v1.response.TransactionResponse;
import com.jovan.erp_v1.util.ApiMessages;
import com.jovan.erp_v1.util.DateValidator;
import com.jovan.erp_v1.util.MoneyUtils;
import com.jovan.erp_v1.util.RoleGroups;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

	private final TransactionRepository transactionRepository;
	private final TransactionMapper transactionMapper;
	private final AccountRepository accountRepository;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public TransactionResponse create(TransactionRequest request) {
		BigDecimal amount = MoneyUtils.normalize(request.amount());
	    validateAmount(amount);
		validateTransactionType(request.transactionType());
		Account sourceAcc = validateSourceAccountId(request.sourceAccountId());
		Account targetAcc = validateTargetAccountId(request.targetAccountId());
		User user = validateUserId(request.userId());
		if (request.transactionType() == TransactionType.DEBIT &&
		        sourceAcc.getBalance().compareTo(amount) < 0) {
		        throw new ValidationException("Insufficient funds in source account");
		}
		sourceAcc.setBalance(MoneyUtils.subtract(sourceAcc.getBalance(), amount));
	    targetAcc.setBalance(MoneyUtils.add(targetAcc.getBalance(), amount));
	    accountRepository.save(sourceAcc);
	    accountRepository.save(targetAcc);
	    Transaction t = transactionMapper.toEntity(request, sourceAcc, targetAcc, user);
	    t.setAmount(amount);
	    t.setTransactionDate(request.transactionDate());
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
		BigDecimal oldAmount = t.getAmount();
	    Account oldSource = t.getSourceAccount();
	    Account oldTarget = t.getTargetAccount();
	    BigDecimal newAmount = MoneyUtils.normalize(request.amount());
	    validateAmount(newAmount);
	    validateTransactionType(request.transactionType());
	    Account newSource = oldSource;
	    if(request.sourceAccountId() != null && !request.sourceAccountId().equals(oldSource.getId())) {
	        newSource = validateSourceAccountId(request.sourceAccountId());
	    }
	    Account newTarget = oldTarget;
	    if(request.targetAccountId() != null && !request.targetAccountId().equals(oldTarget.getId())) {
	        newTarget = validateTargetAccountId(request.targetAccountId());
	    }
	    User user = t.getUser();
	    if(request.userId() != null && !request.userId().equals(user.getId())) {
	        user = validateUserId(request.userId());
	    }
	    oldSource.setBalance(MoneyUtils.add(oldSource.getBalance(), oldAmount));
	    oldTarget.setBalance(MoneyUtils.subtract(oldTarget.getBalance(), oldAmount));
	    if (request.transactionType() == TransactionType.DEBIT &&
	        newSource.getBalance().compareTo(newAmount) < 0) {
	        throw new ValidationException("Insufficient funds in source account");
	    }
	    newSource.setBalance(MoneyUtils.subtract(newSource.getBalance(), newAmount));
	    newTarget.setBalance(MoneyUtils.add(newTarget.getBalance(), newAmount));
	    accountRepository.save(oldSource);
	    accountRepository.save(oldTarget);
	    if (!oldSource.equals(newSource)) accountRepository.save(newSource);
	    if (!oldTarget.equals(newTarget)) accountRepository.save(newTarget);
	    transactionMapper.toEntityUpdate(t, request, newSource, newTarget, user);
	    t.setAmount(newAmount);
	    t.setTransactionDate(request.transactionDate());
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
	
	@Transactional
	@Override
	public TransactionResponse fundTransfer(String sourceAccountNumber, String targetAccountNumber,PaymentMethod paymentMethod, User user, BigDecimal amount) {
		Account source = validateAccountNumber(sourceAccountNumber);
		Account target = validateAccountNumber(targetAccountNumber);
		validateUserRole(user, RoleGroups.TRANSACTION_FULL_ACCESS);
		validatePaymentMethod(paymentMethod);
		validateAmount(amount);
		if (sourceAccountNumber.equals(targetAccountNumber)) {
            throw new ValidationException(ApiMessages.CASH_TRANSFER_SAME_ACCOUNT_ERROR.getMessage());
        }
		if(target == null) {
			throw new ValidationException(ApiMessages.ACCOUNT_NOT_FOUND.getMessage());
		}
		BigDecimal sourceBalance = source.getBalance();
		if(sourceBalance.compareTo(amount) < 0) {
			throw new ValidationException(ApiMessages.BALANCE_INSUFFICIENT_ERROR.getMessage());
		}
		BigDecimal newSourceBalance = sourceBalance.subtract(amount);
		source.setBalance(newSourceBalance);
		accountRepository.save(source);
		BigDecimal targetBalance = target.getBalance();
		BigDecimal newTargetBalance = targetBalance.add(amount);
		target.setBalance(newTargetBalance);
		accountRepository.save(target);
		Transaction t = new Transaction();
		t.setAmount(amount);
		t.setTransactionType(TransactionType.TRANSFER);
		t.setSourceAccount(source);
		t.setTargetAccount(target);
		t.setPaymentMethod(paymentMethod != null ? paymentMethod : PaymentMethod.BANK_TRANSFER);
		Transaction saved = transactionRepository.save(t);
		return new TransactionResponse(saved);
	}

	@Transactional
	@Override
	public TransactionResponse cashWithdrawal(String accountNumber, BigDecimal amount,PaymentMethod paymentMethod, User user) {
		validateUserRole(user, RoleGroups.TRANSACTION_FULL_ACCESS);
		Account account = validateAccountNumber(accountNumber);
		validatePaymentMethod(paymentMethod);
		validateAmount(amount);
		BigDecimal currentBalance = account.getBalance();
		if(currentBalance.compareTo(amount) < 0) {
			throw new ValidationException(ApiMessages.BALANCE_INSUFFICIENT_ERROR.getMessage());
		}
		BigDecimal newBalance = currentBalance.subtract(amount);
		account.setBalance(newBalance);
		accountRepository.save(account);
		Transaction t = new Transaction();
		t.setAmount(amount);
		t.setTransactionType(TransactionType.WITHDRAWAL);
		t.setSourceAccount(account);
		t.setTargetAccount(null);
		t.setUser(user);
		Transaction saved = transactionRepository.save(t);
		return new TransactionResponse(saved);
	}

	@Transactional
	@Override
	public TransactionResponse deposit(String accountNumber, BigDecimal amount, PaymentMethod paymentMethod, User user) {
		validateUserRole(user, RoleGroups.TRANSACTION_FULL_ACCESS);
		validatePaymentMethod(paymentMethod);
	    Account targetAccount = validateAccountNumber(accountNumber);
	    validateAmount(amount);
	    BigDecimal newBalance = targetAccount.getBalance().add(amount);
	    targetAccount.setBalance(newBalance);
	    accountRepository.save(targetAccount);
	    // Kreiranje nove transakcije
	    Transaction transaction = new Transaction();
	    transaction.setAmount(amount);
	    transaction.setTransactionType(TransactionType.DEPOSIT);
	    // Kod depozita: nema izvornog racuna (novac dolazi "spolja"), target je account
	    transaction.setTargetAccount(targetAccount);
	    transaction.setSourceAccount(null);	    
	    // Setovanje korisnika koji je izvrsio transakciju
	    transaction.setUser(user);
	    Transaction savedTransaction = transactionRepository.save(transaction);
	    return new TransactionResponse(savedTransaction);
	}

	@Transactional
	@Override
	public TransactionResponse makePayment(String sourceAccountNumber, String targetAccountNumber, BigDecimal amount, PaymentMethod paymentMethod, User user) {
		Account source = validateAccountNumber(sourceAccountNumber);
	    Account target = validateAccountNumber(targetAccountNumber);
	    validateUserRole(user, RoleGroups.TRANSACTION_FULL_ACCESS);
	    validatePaymentMethod(paymentMethod);
	    validateAmount(amount);
	    if (sourceAccountNumber.equals(targetAccountNumber)) {
	        throw new ValidationException(ApiMessages.CASH_TRANSFER_SAME_ACCOUNT_ERROR.getMessage());
	    }
	    BigDecimal newSourceBalance = source.getBalance().subtract(amount);
	    if(newSourceBalance.compareTo(BigDecimal.ZERO) < 0) {
	        throw new ValidationException(ApiMessages.BALANCE_INSUFFICIENT_ERROR.getMessage());
	    }
	    source.setBalance(newSourceBalance);
	    target.setBalance(target.getBalance().add(amount));
	    accountRepository.save(source);
	    accountRepository.save(target);
	    Transaction t = new Transaction();
	    t.setAmount(amount);
	    t.setTransactionType(TransactionType.PAYMENT);
	    t.setSourceAccount(source);
	    t.setTargetAccount(target);
	    t.setUser(user);
	    t.setPaymentMethod(paymentMethod);
	    return new TransactionResponse(transactionRepository.save(t));
	}

	@Transactional
	@Override
	public TransactionResponse refund(Long originalTransactionId, User initiatingUser) {
		//trazenje originalne transakcije
		Transaction original = transactionRepository.findById(originalTransactionId)
	            .orElseThrow(() -> new ValidationException("Original transaction not found"));
		validateUserRole(initiatingUser, RoleGroups.TRANSACTION_FULL_ACCESS);
		if (original.getTransactionType() == TransactionType.REFUND) {
	        throw new ValidationException("Cannot refund a refund transaction");
	    }
		//provera balansa
		Account source = original.getTargetAccount();
	    Account target = original.getSourceAccount();
	    BigDecimal amount = original.getAmount();
	    //provera da li su source i target nepostojeci
	    if (source == null || target == null) {
	        throw new ValidationException("Source or target account missing in original transaction");
	    }
	    BigDecimal sourceBalance = source.getBalance();
	    if (sourceBalance.compareTo(amount) < 0) {
	        throw new ValidationException(ApiMessages.BALANCE_INSUFFICIENT_ERROR.getMessage());
	    }
	    source.setBalance(sourceBalance.subtract(amount));
	    accountRepository.save(source);
	    BigDecimal targetBalance = target.getBalance();
	    target.setBalance(targetBalance.add(amount));
	    accountRepository.save(target);
	    Transaction refundTransaction = new Transaction();
	    refundTransaction.setAmount(amount);
	    refundTransaction.setTransactionType(TransactionType.REFUND);
	    refundTransaction.setSourceAccount(source);
	    refundTransaction.setTargetAccount(target);
	    refundTransaction.setUser(initiatingUser);
	    // Opcionalno
	    refundTransaction.setPaymentMethod(original.getPaymentMethod());
	    Transaction savedRefund = transactionRepository.save(refundTransaction);
	    return new TransactionResponse(savedRefund);
	}
	
	private void validateUserRole(User user, String roleGroup) {
	    if (user == null) {
	        throw new ValidationException("User must not be null");
	    }
	    Set<String> allowedRoles = Arrays.stream(roleGroup
	                    .replace("hasAnyRole(", "")
	                    .replace(")", "")
	                    .replace("'", "")
	                    .split(","))
	            .map(String::trim)
	            .collect(Collectors.toSet());

	    boolean hasRole = user.getRoles().stream()
	            .map(Role::getName)
	            .anyMatch(allowedRoles::contains);
	    if (!hasRole) {
	        throw new ValidationException("User does not have permission to perform this action");
	    }
	}
	
	private void validatePaymentMethod(PaymentMethod paymentMethod) {
		Optional.ofNullable(paymentMethod)
			.orElseThrow(() -> new ValidationException("PaymentMethod paymentMethod must not be null"));
	}
	
	private Account validateAccountNumber(String accountNumber) {
		Account acc = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new ValidationException("Acount-number not found"));
		if(acc == null) {
			throw new ValidationException(ApiMessages.ACCOUNT_NOT_FOUND.getMessage());
		}
		return acc;
	}
	
	private void validateAmount(BigDecimal amount) {
	    amount = MoneyUtils.normalize(amount);
	    if (!MoneyUtils.isPositive(amount)) {
	        throw new ValidationException("Amount must be greater than 0");
	    }
	    if (!MoneyUtils.isMultipleOf(amount, 100)) {
	        throw new ValidationException("Amount must be in multiples of 100");
	    }
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
