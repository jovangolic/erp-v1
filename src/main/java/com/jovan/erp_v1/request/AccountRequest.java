package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.AccountType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AccountRequest(
        Long id,
        @Size(min = 1) @NotNull String accountNumber,
        @NotNull String accountName,
        @NotNull AccountType type,
        @NotNull @Positive BigDecimal balance) {
}
