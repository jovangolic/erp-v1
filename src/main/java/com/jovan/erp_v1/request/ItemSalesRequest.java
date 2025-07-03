package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemSalesRequest(
		Long id,
		@NotNull(message = "ID robe je obavezan")
	    Long goodsId,

	    @NotNull(message = "ID prodaje je obavezan")
	    Long salesId,

	    Long procurementId,
	    Long salesOrderId,

	    @NotNull(message = "Količina je obavezna")
	    @Positive(message = "Količina mora biti pozitivna")
	    BigDecimal quantity,

	    @NotNull(message = "Jedinična cena je obavezna")
	    @DecimalMin(value = "0.0", inclusive = false, message = "Cena mora biti pozitivna")
	    BigDecimal unitPrice
		) {
}
