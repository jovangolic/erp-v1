package com.jovan.erp_v1.request;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.ShiftType;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record ShiftPlanningRequest(
        Long id,
        @NotNull Long workCenterId,
        @NotNull Long userId,
        @NotNull @FutureOrPresent LocalDate date,
        @NotNull ShiftType shiftType,
        @NotNull Boolean assigned) {
}
