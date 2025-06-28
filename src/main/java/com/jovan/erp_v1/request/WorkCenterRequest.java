package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WorkCenterRequest(
        Long id,

        @NotBlank String name,

        @NotBlank String location,

        @NotNull @Positive BigDecimal capacity,
        @NotNull Long localStorageId) {
}
