package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.ItemSalesStatus;

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
	    @NotNull @DecimalMin(value = "0.01", message = "Quantity must be greater than zero")
	    BigDecimal quantity,
	    @NotNull @DecimalMin(value = "0", message = "Unit price must not be negative")
	    BigDecimal unitPrice,
	    @NotNull ItemSalesStatus status,
	    @NotNull Boolean confirmed
		) {
}
