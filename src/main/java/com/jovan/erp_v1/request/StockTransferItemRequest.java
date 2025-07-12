package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockTransferItemRequest(
        Long id,
        @NotNull Long productId,
        @NotNull @Positive BigDecimal quantity,
        Long stockTransferId) {
}
