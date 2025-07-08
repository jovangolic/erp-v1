package com.jovan.erp_v1.request;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.ProductionOrderStatus;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductionOrderRequest(
			Long id,
                @NotBlank(message = "Order number is required") String orderNumber,

                @NotNull(message = "Product ID is required") Long productId,

                @NotNull(message = "Planned quantity is required") @Min(value = 1, message = "Planned quantity must be at least 1") Integer quantityPlanned,

                @Min(value = 0, message = "Produced quantity cannot be negative") Integer quantityProduced,

                @NotNull(message = "Start date is required") @FutureOrPresent(message = "Start date cannot be in the past") LocalDate startDate,

                @NotNull(message = "End date is required") LocalDate endDate,

                @NotNull(message = "Status is required") ProductionOrderStatus status,

                @NotNull(message = "Work center ID is required") Long workCenterId) {
}
