package com.jovan.erp_v1.save_as;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DriverSaveAsRequest(
		@NotNull Long sourceId,
		@NotBlank String firstName,
		@NotBlank String lastName,
		@NotBlank String phone
		) {

}
