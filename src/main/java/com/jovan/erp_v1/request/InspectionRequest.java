package com.jovan.erp_v1.request;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionStatus;
import com.jovan.erp_v1.enumeration.InspectionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record InspectionRequest(
		Long id,
		@NotBlank(message = "Code must not be blank")
        String code,
        @NotNull(message = "Type is required")
        InspectionType type,
        @NotNull(message = "Date is required")
        LocalDateTime date,
        @NotNull(message = "Batch ID is required")
        Long batchId,
        @NotNull(message = "Product ID is required")
        Long productId,
        @NotNull(message = "Inspector ID is required")
        Long inspectorId,
        @NotNull(message = "Quantity inspected is required")
        @Positive(message = "Quantity inspected must be greater than 0")
        Integer quantityInspected,
        @NotNull(message = "Quantity accepted is required")
        @PositiveOrZero(message = "Quantity accepted must be zero or greater")
        Integer quantityAccepted,
        @NotNull(message = "Quantity rejected is required")
        @PositiveOrZero(message = "Quantity rejected must be zero or greater")
        Integer quantityRejected,
        String notes,
        @NotNull(message = "Result is required")
        InspectionResult result,
        @NotNull(message = "Quality check ID is required")
        Long qualityCheckId,
        @NotNull InspectionStatus satus,
        @NotNull Boolean confirmed
		) {
}
