package com.jovan.erp_v1.search_request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;

import jakarta.validation.constraints.NotNull;

public record BalanceSheetSearchRequest(
		@NotNull Long id,
		@NotNull Long idFrom,
		@NotNull Long idTo,
		@NotNull Long fiscalYearId,
		@NotNull Long fiscalYearIdFrom,
		@NotNull Long fiscalYearIdTo,
		@NotNull LocalDate date,
		@NotNull LocalDate dateBefore,
		@NotNull LocalDate dateAfter,
		@NotNull LocalDate dateFrom,
		@NotNull LocalDate dateTo,
		@NotNull BigDecimal totalAssets,
		@NotNull BigDecimal totalAssetsFrom,
		@NotNull BigDecimal totalAssetsTo,
		@NotNull BigDecimal totalAssetsLessThan,
		@NotNull BigDecimal totalAssetsGreater,
		@NotNull BigDecimal totalLiabilities,
		@NotNull BigDecimal totalLiabilitiesFrom,
		@NotNull BigDecimal totalLiabilitiesTo,
		@NotNull BigDecimal totalLiabilitiesLess,
		@NotNull BigDecimal totalLiabilitiesGreater,
		@NotNull BigDecimal totalEquity,
		@NotNull BigDecimal totalEquityFrom,
		@NotNull BigDecimal totalEquityTo,
		@NotNull BigDecimal totalEquityLess,
		@NotNull BigDecimal totalEquityGreater,
		@NotNull Integer year,
		@NotNull Integer yearFrom,
		@NotNull Integer yearTo,
		@NotNull FiscalYearStatus yearStatus,
		@NotNull FiscalQuarterStatus quarterStatus,
		@NotNull LocalDate startDate,
		@NotNull LocalDate startDateAfter,
		@NotNull LocalDate startDateBefore,
		@NotNull LocalDate endDate,
		@NotNull LocalDate endDateAfter,
		@NotNull LocalDate endDateBefore,
		@NotNull Boolean confirmed
		) {
}
