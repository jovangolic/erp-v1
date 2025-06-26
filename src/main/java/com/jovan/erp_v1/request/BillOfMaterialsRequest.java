package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record BillOfMaterialsRequest(
        Long id,
        @NotNull Long parentProductId,
        @NotNull Long componentId,
        @NotNull @DecimalMin(value = "0.0", inclusive = false, message = "Quantity must be greater than zero") BigDecimal quantity) {
}
