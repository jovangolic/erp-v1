package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.MaterialRequestStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record MaterialRequestDTO(
        Long id,
        @NotNull Long requestingWorkCenterId,
        @NotNull Long materialId,
        @NotNull(message = "Quantity is required") @DecimalMin(value = "0.01", inclusive = true, message = "Quantity must be greater than zero") BigDecimal quantity,
        @NotNull(message = "Request date is required") @PastOrPresent(message = "Request date cannot be in the future") LocalDate requestDate,
        @NotNull(message = "Needed by date is required") @FutureOrPresent(message = "Needed by date must be today or in the future") LocalDate neededBy,
        @NotNull MaterialRequestStatus status) {
}
