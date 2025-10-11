package com.jovan.erp_v1.save_as;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record CapacityPlanningSaveAsRequest(
		@NotNull Long sourceId,
		@NotNull Long workCenterId,
		@NotNull LocalDate date,
		@NotNull BigDecimal availableCapacity,
		@NotNull BigDecimal plannedLoad
		
		) {
}
