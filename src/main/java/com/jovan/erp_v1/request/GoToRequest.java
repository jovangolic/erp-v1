package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.GoToCategory;
import com.jovan.erp_v1.enumeration.GoToType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GoToRequest(
		Long id,
		@NotBlank String label,
		@NotBlank String description,
		@NotNull GoToCategory category,
		@NotNull GoToType type,
		@NotBlank String path,
		@NotBlank String icon,
		@NotNull Boolean active,
		@NotBlank String roles
		) {

}
