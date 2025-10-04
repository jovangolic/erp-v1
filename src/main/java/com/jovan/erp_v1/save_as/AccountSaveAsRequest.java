package com.jovan.erp_v1.save_as;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountSaveAsRequest(
		@NotNull Long sourceId,
		@NotBlank String accountNumber,
		@NotBlank String accountName	
		) {
}
