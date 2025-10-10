package com.jovan.erp_v1.search_request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;

public record BalanceSheetGeneralSearchRequest(
		Long id,
		Long idFrom,
		Long idTo,
		Long fiscalYearId,
		Long fiscalYearIdFrom,
		Long fiscalYearIdTo,
		LocalDate date,
		LocalDate dateBefore,
		LocalDate dateAfter,
		LocalDate dateFrom,
		LocalDate dateTo,
		BigDecimal totalAssets,
		BigDecimal totalAssetsFrom,
		BigDecimal totalAssetsTo,
		BigDecimal totalLiabilities,
		BigDecimal totalLiabilitiesFrom,
		BigDecimal totalLiabilitiesTo,
		BigDecimal totalEquity,
		BigDecimal totalEquityFrom,
		BigDecimal totalEquityTo,
		Integer year,
		Integer yearFrom,
		Integer yearTo,
		FiscalYearStatus yearStatus,
		FiscalQuarterStatus quarterStatus,
		LocalDate startDate,
		LocalDate startDateAfter,
		LocalDate startDateBefore,
		LocalDate endDate,
		LocalDate endDateAfter,
		LocalDate endDateBefore,
		Boolean confirmed
		) {
}
