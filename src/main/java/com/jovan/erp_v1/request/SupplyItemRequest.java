package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record SupplyItemRequest(
		
		Long id,
		@NotNull(message = "ID nabavke je obavezan")
		Long procurementId,

		@NotNull(message = "ID dobavljača je obavezan")
		Long vendorId,

		@NotNull(message = "Cena je obavezna")
		@DecimalMin(value = "0.0", inclusive = false, message = "Cena mora biti pozitivna")
		BigDecimal cost
		) {
}
