package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.Unit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record QualityStandardRequest(
		Long id,
		@NotNull Long productId,
		@NotBlank String description,
		@NotNull(message = "Min-value is required")
        @PositiveOrZero(message = "Min-value must be 0 or greater")
        BigDecimal minValue,

        @NotNull(message = "Max-value is required")
        @Positive(message = "Max-value must be greater than 0")
        BigDecimal maxValue,
		@NotNull Unit unit
		) {

}
