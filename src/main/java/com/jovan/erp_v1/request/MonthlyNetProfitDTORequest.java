package com.jovan.erp_v1.request;

import java.math.BigDecimal;

public record MonthlyNetProfitDTORequest(
		Integer month,
		Integer year,
	    BigDecimal netProfit
		) {
}
