package com.jovan.erp_v1.request;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SupplyRequest(
		
		Long id,
		@NotNull(message = "ID skladišta je obavezan")
		Long storageId,

		@NotEmpty(message = "Lista robe je obavezna")
		List<Long> goodsIds,

		@NotNull(message = "Količina je obavezna")
		@Positive(message = "Količina mora biti veća od 0")
		Integer quantity,

		@NotNull(message = "Datum ažuriranja je obavezan")
		LocalDateTime updates
		) {
}
