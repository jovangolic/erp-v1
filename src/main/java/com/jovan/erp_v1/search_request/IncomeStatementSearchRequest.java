package com.jovan.erp_v1.search_request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.enumeration.IncomeStatementStatus;

public record IncomeStatementSearchRequest(
		Long id,
		Long idFrom,
		Long idTo,
		LocalDate periodStart,
		LocalDate periodStartBefore,
		LocalDate periodStartAfter,
		LocalDate periodEnd,
		LocalDate periodEndBefore,
		LocalDate periodEndAfter,
		BigDecimal totalRevenue,
		BigDecimal totalRevenueMin,
		BigDecimal totalRevenueMax,
		BigDecimal totalExpenses,
		BigDecimal totalExpensesMin,
		BigDecimal totalExpensesMax,
		BigDecimal netProfit,
		BigDecimal netProfitMin,
		BigDecimal netProfitMax,
		Long fiscalYearId,
		Long fiscalYearIdFrom,
		Long fiscalYearIdTo,
		Integer year,
		Integer yearTo,
		Integer yearFrom,
		LocalDate startDate,
		LocalDate startDateBefore,
		LocalDate startDateAfter,
		LocalDate endDate,
		LocalDate endDateBefore,
		LocalDate endDateAfter,
		FiscalYearStatus yearStatus,
		FiscalQuarterStatus quarterStatus,
		Boolean confirmed,
		IncomeStatementStatus status
		) {
}
