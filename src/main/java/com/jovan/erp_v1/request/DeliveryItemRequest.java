package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.DeliveryItemStatus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DeliveryItemRequest(
        @NotNull Long id,
        @NotEmpty Long productId,
        @NotNull @Positive BigDecimal quantity,
        @NotNull Long inboundDeliveryId,
        @NotNull Long outboundDeliveryId,
        @NotNull DeliveryItemStatus status,
		@NotNull Boolean confirmed) {
}
