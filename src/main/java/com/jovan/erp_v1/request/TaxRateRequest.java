package com.jovan.erp_v1.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.TaxType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TaxRateRequest(
        Long id,
        @NotNull String taxName,
        @NotNull @Positive BigDecimal percentage,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        TaxType type) {
}
