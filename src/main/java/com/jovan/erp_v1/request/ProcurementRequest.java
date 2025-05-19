package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProcurementRequest(
		
		Long id,
		@NotNull(message = "Datum nabavke je obavezan")
	    LocalDateTime date,

	    @NotNull(message = "Ukupna cena je obavezna")
	    @DecimalMin(value = "0.0", inclusive = false, message = "Ukupna cena mora biti pozitivna")
	    BigDecimal totalCost,

	    @NotEmpty(message = "Morate navesti bar jednu prodajnu stavku")
	    List<Long> itemSalesIds,

	    @NotEmpty(message = "Morate navesti bar jednu stavku iz dobavljača")
	    List<Long> supplyItemIds 
		) {

}
