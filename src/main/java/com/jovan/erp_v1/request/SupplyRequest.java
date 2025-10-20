package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SupplyRequest(
		Long id,
		@NotNull(message = "ID skladista je obavezan") Long storageId,
		@NotEmpty(message = "Lista robe je obavezna") List<Long> goodsIds,
		@NotNull(message = "Kolicina je obavezna") @Positive(message = "Kolicina mora biti veca od 0") BigDecimal quantity,
		@NotNull(message = "Datum azuriranja je obavezan") LocalDateTime updates) {
}
