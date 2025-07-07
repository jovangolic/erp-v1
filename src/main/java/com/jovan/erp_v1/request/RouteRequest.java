package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public record RouteRequest(
        Long id,
        @NotNull String origin,
        @NotNull String destination,
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false, message = "Distance must be greater than 0")
        @Digits(integer = 10, fraction = 2, message = "Distance can have up to 10 digits and 2 decimal places")
        BigDecimal distanceKm
) {}
