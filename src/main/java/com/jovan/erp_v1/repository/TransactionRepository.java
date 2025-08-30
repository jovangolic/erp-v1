package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.AccountType;
import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByAmount(BigDecimal amount);
	List<Transaction> findByAmountGreaterThan(BigDecimal amount);
	List<Transaction> findByAmountLessThan(BigDecimal amount);
	List<Transaction> findByAmountBetween(BigDecimal amountMin, BigDecimal amountMax);
	List<Transaction> findByTransactionDate(LocalDateTime transactionDate);
	List<Transaction> findByTransactionDateAfter(LocalDateTime transactionDate);
	List<Transaction> findByTransactionDateBefore(LocalDateTime transactionDate);
	List<Transaction> findByTransactionDateBetween(LocalDateTime transactionDateStart, LocalDateTime transactionDateEnd);
	List<Transaction> findByTransactionType(TransactionType transactionType);
	List<Transaction> findByTransactionTypeAndTransactionDate(TransactionType transactionType, LocalDateTime transactionDate);
	List<Transaction> findByTransactionTypeAndTransactionDateAfter(TransactionType transactionType,LocalDateTime transactionDate);
	List<Transaction> findByTransactionTypeAndTransactionDateBefore(TransactionType transactionType,LocalDateTime transactionDate);
	List<Transaction> findByTransactionTypeAndTransactionDateBetween(TransactionType transactionType,LocalDateTime transactionDateStart, LocalDateTime transactionDateEnd);
	List<Transaction> findByUserFirstNameContainingIgnoreCaseAndUserLastNameContainingIgnoreCase(String firstName, String lastName);
	List<Transaction> findByUserEmailLikeIgnoreCase(String userEmail);
	List<Transaction> findBySourceAccountId(Long sourceAccountId);
	List<Transaction> findBySourceAccountAccountNumberContainingIgnoreCase(String accountNumber);
	List<Transaction> findBySourceAccountAccountName(String accountName);
	List<Transaction> findBySourceAccountType(AccountType type);
	List<Transaction> findBySourceAccountAccountNumberContainingIgnoreCaseAndSourceAccountAccountNameContainingIgnoreCase(String accountNumber, String accountName);
	List<Transaction> findByTargetAccountId(Long sourceAccountId);
	List<Transaction> findByTargetAccountAccountNumberContainingIgnoreCase(String accountNumber);
	List<Transaction> findByTargetAccountAccountName(String accountName);
	List<Transaction> findByTargetAccountType(AccountType type);
	List<Transaction> findByTargetAccountAccountNumberContainingIgnoreCaseAndTargetAccountAccountNameContainingIgnoreCase(String accountNumber, String accountName);
	
	List<Transaction> findByAmountBetweenAndTransactionDateBetween(BigDecimal min, BigDecimal max,LocalDateTime start, LocalDateTime end);
	List<Transaction> findByUserIdAndTransactionDateBetween(Long userId, LocalDateTime start, LocalDateTime end);
	List<Transaction> findBySourceAccountIdAndTransactionTypeAndTransactionDateBetween(
		    Long accountId,TransactionType transactionType,LocalDateTime start,LocalDateTime end);
	@Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.sourceAccount.id = :accountId")
	BigDecimal sumOfOutgoingTransactions(@Param("accountId") Long accountId);
	@Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.targetAccount.id = :accountId")
	BigDecimal sumOfIncomingTransactions(@Param("accountId") Long accountId);
	
	boolean existsBySourceAccount_AccountNumberContainingIgnoreCase(String accountNumber);
	boolean existsBySourceAccount_AccountNameContainingIgnoreCase(String accountName);
	boolean existsByTargetAccount_AccountNumberContainingIgnoreCase(String accountNumber);
	boolean existsByTargetAccount_AccountNameContainingIgnoreCase(String accountName);
	boolean existsBySourceAccountIdAndTargetAccountId(Long sourceId, Long targetId);
}
