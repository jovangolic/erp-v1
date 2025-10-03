package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public record VehicleLocationRequest(
		@NotNull Long id,
		@NotNull Long vehicleId,
		@DecimalMin(value = "-90.0", inclusive = true, message = "Latitude must be >= -90")
        @DecimalMax(value = "90.0", inclusive = true, message = "Latitude must be <= 90")
        @Digits(integer = 3, fraction = 6, message = "Latitude can have up to 6 decimal places")
        BigDecimal latitude,

        @DecimalMin(value = "-180.0", inclusive = true, message = "Longitude must be >= -180")
        @DecimalMax(value = "180.0", inclusive = true, message = "Longitude must be <= 180")
        @Digits(integer = 3, fraction = 6, message = "Longitude can have up to 6 decimal places")
        BigDecimal longitude
		) {

}
