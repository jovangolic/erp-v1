package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.StorageType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StorageRequest(
		Long id,
		@NotBlank(message = "Naziv skladišta je obavezan")
	    String name,

	    @NotBlank(message = "Lokacija je obavezna")
	    String location,

	    @NotNull(message = "Kapacitet je obavezan")
	    @Positive(message = "Kapacitet mora biti pozitivan broj")
	    Double capacity,

	    @NotNull(message = "Tip skladišta je obavezan")
	    StorageType type
		) {
}
