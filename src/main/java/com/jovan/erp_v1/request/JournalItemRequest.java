package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record JournalItemRequest(
        Long id,
        @NotNull Long accountId,
        @NotNull @DecimalMin(value = "0.00", inclusive = true) BigDecimal debit,
        @NotNull @DecimalMin(value = "0.00", inclusive = true) BigDecimal credit) {
}
