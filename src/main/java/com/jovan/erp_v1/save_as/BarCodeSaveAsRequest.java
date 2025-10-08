package com.jovan.erp_v1.save_as;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BarCodeSaveAsRequest(
		@NotNull Long sourceId,
		@NotBlank String code
		) {
}
