package com.jovan.erp_v1.save_as;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BatchSaveAsRequest(
		@NotNull(message = "Izvorni batch je obavezan")
	    Long sourceId,
	    @NotBlank(message = "Code je obavezan")
	    String code,
	    @NotNull(message = "Kolicina je obavezna")
	    Integer quantityProduced,
	    @NotNull(message = "Proizvod je obavezan")
	    Long productId,
	    @NotNull(message = "Datum proizvodnje je obavezan")
	    LocalDate productionDate,
	    @NotNull(message = "Datum isteka je obavezan")
	    LocalDate expiryDate
		) {
}
