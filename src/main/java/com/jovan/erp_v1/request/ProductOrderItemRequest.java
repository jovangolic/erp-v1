package com.jovan.erp_v1.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductOrderItemRequest(
		
		@NotNull(message = "ID proizvoda je obavezan")
	    Long productId,

	    @NotNull(message = "Trenutna količina je obavezna")
	    @PositiveOrZero(message = "Količina mora biti 0 ili veća")
	    Double currentQuantity
		) {

}
