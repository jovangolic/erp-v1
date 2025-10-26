package com.jovan.erp_v1.save_as;

import jakarta.validation.constraints.NotNull;

public record InspectionDefectSaveAsRequest(
		@NotNull Long sourceId,
		@NotNull Integer quantityAffected,
		@NotNull Long inspectionId,
		@NotNull Long defectId
		) {
}
