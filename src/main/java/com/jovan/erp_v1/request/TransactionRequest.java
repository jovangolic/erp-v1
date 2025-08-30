package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.TransactionType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record TransactionRequest(
		Long id,
		@NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
        BigDecimal amount,
        
        @NotNull(message = "Transaction date is required")
        @FutureOrPresent(message = "Transaction date must be in the present or future")
        LocalDateTime transactionDate,
        
        @NotNull(message = "Transaction type is required")
        TransactionType transactionType,
        
        @NotNull(message = "Source account ID is required")
        Long sourceAccountId,
        
        @NotNull(message = "Target account ID is required")
        Long targetAccountId,
        
        @NotNull(message = "User ID is required")
        Long userId
		) {
}
