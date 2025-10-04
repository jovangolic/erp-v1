package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.AccountStatus;
import com.jovan.erp_v1.enumeration.AccountType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AccountRequest(
        Long id,
        @Size(min = 1) @NotNull String accountNumber,
        @NotNull String accountName,
        @NotNull AccountType type,
        @NotNull @DecimalMin(value = "0.00", inclusive = true) BigDecimal balance,
        @NotNull AccountStatus status,
		@NotNull Boolean confirmed) {
}
