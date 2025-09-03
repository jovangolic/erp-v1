package com.jovan.erp_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.model.TransactionAudit;

@Repository
public interface TransactionAuditRepository extends JpaRepository<TransactionAudit, Long> {

	List<TransactionAudit> findByUserId(Long userId);
    List<TransactionAudit> findByTransactionId(Long transactionId);
    List<TransactionAudit> findBySourceAccountNumberContainingIgnoreCase(String sourceAccountNumber);
    List<TransactionAudit> findByTargetAccountNumberContainingIgnoreCase(String targetAccountNumber);
    List<TransactionAudit> findByTransactionType(TransactionType transactionType);
    
}
