package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.UnitOfMeasure;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MaterialRequest(
        Long id,
        @NotBlank String code,
        @NotBlank String name,
        @NotNull UnitOfMeasure unit,
        @NotNull @DecimalMin("0.0") BigDecimal currentStock,
        @NotNull Long storageId,
        @NotNull @DecimalMin("0.0") BigDecimal reorderLevel) {
}
