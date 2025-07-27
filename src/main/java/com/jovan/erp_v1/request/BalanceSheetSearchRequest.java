package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BalanceSheetSearchRequest(
		LocalDate startDate,
	    LocalDate endDate,
	    Long fiscalYearId,
	    BigDecimal minAssets,
	    BigDecimal minEquity,
	    BigDecimal minLiabilities,
	    Boolean onlySolvent
		) {
}
