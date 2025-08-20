package com.jovan.erp_v1.request;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QualityCheckRequest(
		Long id,
		@NotNull LocalDateTime locDate,
		@NotNull Long inspectorId,
		@NotNull ReferenceType referenceType,
		Long referenceId,
		@NotNull QualityCheckType checkType,
		@NotNull QualityCheckStatus status,
		@NotBlank String notes
		) {

}
