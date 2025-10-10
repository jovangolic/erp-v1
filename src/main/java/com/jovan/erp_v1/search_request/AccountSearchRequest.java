package com.jovan.erp_v1.search_request;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.AccountStatus;
import com.jovan.erp_v1.enumeration.AccountType;

public record AccountSearchRequest(
		Long id,
		Long accountIdFrom,
		Long accountIdTo,
		String accountNumber,
		String accountName,
		AccountType type,
		AccountStatus status,
		BigDecimal balance,
		BigDecimal balanceFrom,
		BigDecimal balanceTo,
		Boolean confirmed
		) {

}
