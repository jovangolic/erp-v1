package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record IncomeStatementRequest(
        Long id,
        @NotNull LocalDate periodStart,
        @NotNull LocalDate periodEnd,
        @NotNull BigDecimal totalRevenue,
        @NotNull BigDecimal totalExpenses,
        @NotNull BigDecimal netProfit,
        @NotNull Long fiscalYearId

) {
}
