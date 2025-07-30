package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.LedgerType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record LedgerEntryRequest(
        Long id,
        @NotNull @PastOrPresent LocalDateTime entryDate,
        @NotNull @Positive BigDecimal amount,
        @NotBlank @Size(max = 255) String description,
        @NotNull Long accountId,
        @NotNull LedgerType type) {
}
