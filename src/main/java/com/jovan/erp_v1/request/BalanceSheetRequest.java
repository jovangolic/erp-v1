package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BalanceSheetRequest(
                Long id,
                @NotNull LocalDate date,
                @NotNull @Positive BigDecimal totalAssets,
                @NotNull @Positive BigDecimal totalLiabilities,
                @NotNull @Positive BigDecimal totalEquity,
                Long fiscalYearId) {
}
