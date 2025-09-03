package com.jovan.erp_v1.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.TransactionAuditMapper;
import com.jovan.erp_v1.model.Account;
import com.jovan.erp_v1.model.TransactionAudit;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.AccountRepository;
import com.jovan.erp_v1.repository.TransactionAuditRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.response.TransactionAuditResponse;
import com.jovan.erp_v1.util.ApiMessages;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionAuditService implements ITransactionAuditService {

	private final TransactionAuditRepository transactionAuditRepository;
	private final TransactionAuditMapper mapper;
	private final UserRepository userRepository;
	private final AccountRepository accountRepository;
	
	@Override
	public List<TransactionAuditResponse> findByUserId(Long userId) {
		validateUserId(userId);
		List<TransactionAudit> items = transactionAuditRepository.findByUserId(userId);
		if(items.isEmpty()) {
			String msg = String.format("No TransactionAudit for user-id %d, found", userId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(mapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<TransactionAuditResponse> findByTransactionId(Long transactionId) {
		if(transactionId == null) {
			throw new ValidationException("Transaction-ID must not be null");
		}
		List<TransactionAudit> items = transactionAuditRepository.findByTransactionId(transactionId);
		if(items.isEmpty()) {
			String msg = String.format("No TransactionAudit for transaction-id %d, found", transactionId);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(mapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<TransactionAuditResponse> findBySourceAccountNumberContainingIgnoreCase(String sourceAccountNumber) {
		validateAccountNumber(sourceAccountNumber);
		List<TransactionAudit> items = transactionAuditRepository.findBySourceAccountNumberContainingIgnoreCase(sourceAccountNumber);
		if(items.isEmpty()) {
			String msg = String.format("No TransactionAudit found for source-account-number %s", sourceAccountNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(mapper::toResponse).collect(Collectors.toList());
	}
	
	
	@Override
	public List<TransactionAuditResponse> findByTargetAccountNumberContainingIgnoreCase(String targetAccountNumber) {
		validateAccountNumber(targetAccountNumber);
		List<TransactionAudit> items = transactionAuditRepository.findByTargetAccountNumberContainingIgnoreCase(targetAccountNumber);
		if(items.isEmpty()) {
			String msg = String.format("No TransactionAudit found for target-account-number %s", targetAccountNumber);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(mapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<TransactionAuditResponse> findByTransactionType(TransactionType transactionType) {
		validateTransactionType(transactionType);
		List<TransactionAudit> items = transactionAuditRepository.findByTransactionType(transactionType);
		if(items.isEmpty()) {
			String msg = String.format("No TransactionAudit found for transaction-type %s", transactionType);
			throw new NoDataFoundException(msg);
		}
		return items.stream().map(mapper::toResponse).collect(Collectors.toList());
	}
	
	@Override
	public List<TransactionAuditResponse> findAll() {
		List<TransactionAudit> items = transactionAuditRepository.findAll();
		if(items.isEmpty()) {
			throw new NoDataFoundException("TransactionAudit list is empty");
		}
		return items.stream().map(mapper::toResponse).collect(Collectors.toList());
	}
	
	private void validateTransactionType(TransactionType transactionType) {
		Optional.ofNullable(transactionType)
			.orElseThrow(() -> new ValidationException("TransactionType transactionType must not be null"));
	}
	
	private User validateUserId(Long userId) {
    	if(userId == null) {
    		throw new ValidationException("User ID must not be null");
    	}
    	return userRepository.findById(userId).orElseThrow(() -> new ValidationException("User not found with id "+userId));
    }
	
	private Account validateAccountNumber(String accountNumber) {
		Account acc = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new ValidationException("Acount-number not found"));
		if(acc == null) {
			throw new ValidationException(ApiMessages.ACCOUNT_NOT_FOUND.getMessage());
		}
		return acc;
	}
	
}
