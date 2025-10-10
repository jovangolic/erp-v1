package com.jovan.erp_v1.save_as;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BuyerSaveAsRequest(
		@NotNull Long sourceId,
		@NotBlank String companyName,
		@NotBlank String pib,
		@NotBlank String address,
		@NotBlank String contactPerson,
		@NotBlank String email,
		@NotBlank String phoneNumber
		) {
}
