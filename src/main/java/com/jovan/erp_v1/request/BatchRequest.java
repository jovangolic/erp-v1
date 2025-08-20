package com.jovan.erp_v1.request;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record BatchRequest(
		Long id,
		@NotBlank String code,
		@NotNull Long productId,
		@NotNull(message = "Quantity produced is required")
		@PositiveOrZero(message = "Quantity produced must be zero or greater")
		Integer quantityProduced,
		@NotNull(message = "Production-Date is required") LocalDate productionDate,
		@NotNull LocalDate expiryDate
		) {
}
