package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.SeverityLevel;
import jakarta.validation.constraints.NotBlank;

public record DefectRequest(
		Long id,
		@NotBlank
		String code,
		@NotBlank
		String name,
		@NotBlank
		String description,
		@NotBlank
		SeverityLevel severity
		) {
}
