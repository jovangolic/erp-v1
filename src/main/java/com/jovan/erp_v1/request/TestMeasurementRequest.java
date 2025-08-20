package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record TestMeasurementRequest(
		Long id,
		@NotNull
		Long inspectionId,
		@NotNull
		Long qualityStandardId,
		@NotNull(message = "Measured value is required") @PositiveOrZero(message = "Measured value must be 0 or greater") BigDecimal measuredValue,
		Boolean withinSpec
		) {
}
