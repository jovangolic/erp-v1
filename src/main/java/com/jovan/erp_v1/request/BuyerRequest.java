package com.jovan.erp_v1.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record BuyerRequest(
		
		Long id,
		@NotBlank
		String companyName, 
		String pib,
		@NotBlank
		String address, 	
		String contactPerson, 
		@Email
		@NotBlank
		String email, 
		@NotBlank
		String phoneNumber) {
}
