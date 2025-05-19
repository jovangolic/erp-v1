package com.jovan.erp_v1.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
		
		/*@NotBlank(message = "Email je obavezan")
	    @Email(message = "Email format nije validan")
	    String email,
	    
		@NotBlank(message = "Username je obavezan")
	    String username,*/
		
		@NotBlank(message = "Email ili korisničko ime je obavezno")
	    String identifier,
	    
	    @NotBlank(message = "Lozinka je obavezna")
	    String password) {
}
