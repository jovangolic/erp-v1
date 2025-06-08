package com.jovan.erp_v1.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DeliveryItemRequest(
                Long id,
                @NotEmpty Long productId,
                @NotNull @Positive Double quantity,
                @NotNull Long inboundDeliveryId,
                @NotNull Long outboundDeliveryId) {
}
