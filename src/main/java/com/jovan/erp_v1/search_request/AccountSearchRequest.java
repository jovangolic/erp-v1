package com.jovan.erp_v1.search_request;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.AccountStatus;
import com.jovan.erp_v1.enumeration.AccountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AccountSearchRequest(
		@NotNull Long id,
		@NotNull Long accountIdFrom,
		@NotNull Long accountIdTo,
		@NotBlank String accountNumber,
		@NotBlank String accountName,
		@NotNull AccountType type,
		@NotNull AccountStatus status,
		@NotNull @Positive BigDecimal balance,
		@NotNull @Positive BigDecimal balanceFrom,
		@NotNull @Positive BigDecimal balanceTo,
		@NotNull Boolean confirmed
		) {

}
