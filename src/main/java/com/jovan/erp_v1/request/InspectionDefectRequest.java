package com.jovan.erp_v1.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InspectionDefectRequest(
		Long id,
		@NotNull @Positive Integer quantityAffected,
		@NotNull Long inspectionId,
		@NotNull Long defectId
		) {

}
