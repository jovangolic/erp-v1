package com.jovan.erp_v1.save_as;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record BalanceSheetSaveAsRequest(
		@NotNull Long sourceId,
		@NotNull BigDecimal totalAssets,
		@NotNull BigDecimal totalLiabilities,
		@NotNull BigDecimal totalEquity,
		@NotNull Long fiscalYearId
		) {
}
