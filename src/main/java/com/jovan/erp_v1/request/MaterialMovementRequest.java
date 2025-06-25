package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.MovementType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MaterialMovementRequest(
        Long id,
        @NotNull Long materialId,
        @NotNull LocalDate movementDate,
        @NotNull MovementType type,
        @NotNull @Positive BigDecimal quantity,
        Long fromStorageId,
        Long toStorageId) {
}
