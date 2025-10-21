package com.jovan.erp_v1.save_as;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record IncomeStatementSaveAsRequest(
		@NotNull Long sourceId,
		@NotNull LocalDate periodStart,
		@NotNull LocalDate periodEnd,
		@NotNull BigDecimal totalRevenue,
		@NotNull BigDecimal totalExpenses,
		@NotNull BigDecimal netProfit,
		@NotNull Long fiscalYearId
		) {
}
