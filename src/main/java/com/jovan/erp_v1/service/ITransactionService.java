package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.request.TransactionRequest;
import com.jovan.erp_v1.response.TransactionResponse;

public interface ITransactionService {

	TransactionResponse create(TransactionRequest request);
	TransactionResponse update(Long id, TransactionRequest request);
	void delete(Long id);
	TransactionResponse findOne(Long id);
	List<TransactionResponse> findAll();
	
	List<TransactionResponse> findByAmount(BigDecimal amount);
	List<TransactionResponse> findByAmountGreaterThan(BigDecimal amount);
	List<TransactionResponse> findByAmountLessThan(BigDecimal amount);
	List<TransactionResponse> findByAmountBetween(BigDecimal amountMin, BigDecimal amountMax);
	List<TransactionResponse> findByTransactionDate(LocalDateTime transactionDate);
	List<TransactionResponse> findByTransactionDateAfter(LocalDateTime transactionDate);
	List<TransactionResponse> findByTransactionDateBefore(LocalDateTime transactionDate);
	List<TransactionResponse> findByTransactionDateBetween(LocalDateTime transactionDateStart, LocalDateTime transactionDateEnd);
	List<TransactionResponse> findByTransactionType(TransactionType transactionType);
	List<TransactionResponse> findByTransactionTypeAndTransactionDate(TransactionType transactionType, LocalDateTime transactionDate);
	List<TransactionResponse> findByTransactionTypeAndTransactionDateAfter(TransactionType transactionType,LocalDateTime transactionDate);
	List<TransactionResponse> findByTransactionTypeAndTransactionDateBefore(TransactionType transactionType,LocalDateTime transactionDate);
	List<TransactionResponse> findByTransactionTypeAndTransactionDateBetween(TransactionType transactionType,LocalDateTime transactionDateStart, LocalDateTime transactionDateEnd);
	List<TransactionResponse> findByUserFirstNameContainingIgnoreCaseAndUserLastNameContainingIgnoreCase(String firstName, String lastName);
	List<TransactionResponse> findByUserEmailLikeIgnoreCase(String userEmail);
	List<TransactionResponse> findBySourceAccountId(Long sourceAccountId);
	List<TransactionResponse> findBySourceAccountAccountNumberContainingIgnoreCase(String accountNumber);
	List<TransactionResponse> findBySourceAccountAccountName(String accountName);
	List<TransactionResponse> findBySourceAccountType(AccountType type);
	List<TransactionResponse> findBySourceAccountAccountNumberContainingIgnoreCaseAndSourceAccountAccountNameContainingIgnoreCase(String accountNumber, String accountName);
	List<TransactionResponse> findByTargetAccountId(Long targetAccountId);
	List<TransactionResponse> findByTargetAccountAccountNumberContainingIgnoreCase(String accountNumber);
	List<TransactionResponse> findByTargetAccountAccountName(String accountName);
	List<TransactionResponse> findByTargetAccountType(AccountType type);
	List<TransactionResponse> findByTargetAccountAccountNumberContainingIgnoreCaseAndTargetAccountAccountNameContainingIgnoreCase(String accountNumber, String accountName);
	
	List<TransactionResponse> findByAmountBetweenAndTransactionDateBetween(BigDecimal min, BigDecimal max,LocalDateTime start, LocalDateTime end);
	List<TransactionResponse> findByUserIdAndTransactionDateBetween(Long userId, LocalDateTime start, LocalDateTime end);
	List<TransactionResponse> findBySourceAccountIdAndTransactionTypeAndTransactionDateBetween(
		    Long accountId,TransactionType transactionType,LocalDateTime start,LocalDateTime end);
	BigDecimal sumOfOutgoingTransactions( Long accountId);
	BigDecimal sumOfIncomingTransactions( Long accountId);
	
	boolean existsBySourceAccount_AccountNumberContainingIgnoreCase(String accountNumber);
	boolean existsBySourceAccount_AccountNameContainingIgnoreCase(String accountName);
	boolean existsByTargetAccount_AccountNumberContainingIgnoreCase(String accountNumber);
	boolean existsByTargetAccount_AccountNameContainingIgnoreCase(String accountName);
	boolean existsBySourceAccountIdAndTargetAccountId(Long sourceId, Long targetId);
}
