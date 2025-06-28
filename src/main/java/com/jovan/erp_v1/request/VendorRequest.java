package com.jovan.erp_v1.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VendorRequest(
		Long id,
		@NotBlank String name,
		@Email String email,
		@NotBlank String phoneNumber,
		@NotBlank String address) {
}
