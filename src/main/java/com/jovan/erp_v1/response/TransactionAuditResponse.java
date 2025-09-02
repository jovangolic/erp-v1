package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.TransactionType;
import com.jovan.erp_v1.model.TransactionAudit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionAuditResponse {

	private Long transactionId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private Long userId;
    private LocalDateTime transactionDate;
    private LocalDateTime deletedAt;
    private String deletedBy;

    public TransactionAuditResponse(TransactionAudit audit) {
        this.transactionId = audit.getTransactionId();
        this.amount = audit.getAmount();
        this.transactionType = audit.getTransactionType();
        this.sourceAccountNumber = audit.getSourceAccountNumber();
        this.targetAccountNumber = audit.getTargetAccountNumber();
        this.userId = audit.getUserId();
        this.transactionDate = audit.getTransactionDate();
        this.deletedAt = audit.getDeletedAt();
        this.deletedBy = audit.getDeletedBy();
    }
}
