package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DeliveryItemInboundRequest(
        Long id,
        @NotEmpty Long productId,
        @NotNull @Positive BigDecimal quantity,
        @NotNull Long inboundDeliveryId) {
}
