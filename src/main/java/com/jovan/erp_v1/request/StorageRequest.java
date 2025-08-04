package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.util.List;

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StorageRequest(
		Long id,
		@NotBlank(message = "Naziv skladišta je obavezan") String name,
		@NotBlank(message = "Lokacija je obavezna") String location,
		@NotNull(message = "Kapacitet je obavezan") @Positive(message = "Kapacitet mora biti pozitivan broj") BigDecimal capacity,
		@NotNull(message = "Tip skladišta je obavezan") StorageType type,
		@Valid List<ShelfRequest> shelves,
		@NotNull(message = "Status skladišta je obavezan") StorageStatus status,
		Boolean hasShelvesFor) {
}
