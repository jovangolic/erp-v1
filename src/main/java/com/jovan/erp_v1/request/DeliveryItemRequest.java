package com.jovan.erp_v1.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record DeliveryItemRequest(
        @NotEmpty Long productId,
        @NotBlank Double quantity,
        @NotBlank Long inboundDeliveryId,
        @NotBlank Long outboundDeliveryId) {
}
