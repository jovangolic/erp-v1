package com.jovan.erp_v1.request;

import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.enumeration.TransferStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record StockTransferRequest(
        Long id,
        @NotNull LocalDate transferDate,
        @NotNull Long fromStorageId,
        @NotNull Long toStorageId,
        @NotNull TransferStatus status,
        @Valid List<StockTransferItemRequest> itemRequest) {
}
