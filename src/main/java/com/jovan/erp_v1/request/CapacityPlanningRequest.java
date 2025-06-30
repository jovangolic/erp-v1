package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record CapacityPlanningRequest(
                Long id,
                @NotNull Long workCenterId,
                @NotNull(message = "Date is required") @FutureOrPresent(message = "Date must be today or in the future") LocalDate date,
                @NotNull(message = "Available capacity is required") @PositiveOrZero(message = "Available capacity must be 0 or greater") BigDecimal availableCapacity,
                @NotNull @Positive BigDecimal plannedLoad
// @NotNull(message = "Remaining capacity is required") @PositiveOrZero(message
// = "Remaining capacity must be 0 or greater") Integer remainingCapacity
) {
}
