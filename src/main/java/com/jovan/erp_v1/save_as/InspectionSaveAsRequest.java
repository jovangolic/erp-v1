package com.jovan.erp_v1.save_as;

import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InspectionSaveAsRequest(
		@NotNull Long sourceId,
		@NotBlank String code,
		@NotNull InspectionType type,
		@NotNull Long batchId,
		@NotNull Long productId,
		@NotNull Long inspectorId,
		@NotNull Integer quantityInspected,
		@NotNull Integer quantityAccepted,
		@NotNull Integer quantityRejected,
		@NotBlank String notes,
		@NotNull InspectionResult result,
		@NotNull Long qualityCheckId
		) {
}
