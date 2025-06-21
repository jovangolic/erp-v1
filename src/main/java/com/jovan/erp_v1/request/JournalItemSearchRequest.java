package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;

public record JournalItemSearchRequest(
        @DecimalMin("0.00") BigDecimal debit,
        @DecimalMin("0.00") BigDecimal credit,
        Long accountId,
        LocalDateTime fromDate,
        LocalDateTime toDate) {
}
