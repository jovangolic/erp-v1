package com.jovan.erp_v1.service;

import java.util.List;
import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.response.TransactionAuditResponse;

public interface ITransactionAuditService {

	List<TransactionAuditResponse> findByUserId(Long userId);
    List<TransactionAuditResponse> findByTransactionId(Long transactionId);
    List<TransactionAuditResponse> findBySourceAccountNumberContainingIgnoreCase(String sourceAccountNumber);
    List<TransactionAuditResponse> findByTargetAccountNumberContainingIgnoreCase(String targetAccountNumber);
    List<TransactionAuditResponse> findByTransactionType(TransactionType transactionType);
    List<TransactionAuditResponse> findAll();
	
}
