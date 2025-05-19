package com.jovan.erp_v1.request;

import java.util.Collection;
import java.util.Set;

import com.jovan.erp_v1.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserRequest(
		
		Long id,	
		@NotBlank(message = "Ime je obavezno")
		String firstName,

		@NotBlank(message = "Prezime je obavezno")
		String lastName,

		@NotBlank(message = "Email je obavezan")
		@Email(message = "Email format nije validan")
		String email,

		@NotBlank(message = "Korisničko ime je obavezno")
		String username,

		@NotBlank(message = "Lozinka je obavezna")
		@Size(min = 6, message = "Lozinka mora imati najmanje 6 karaktera")
		String password,

		@NotBlank(message = "Broj telefona je obavezan")
		String phoneNumber,

		@NotEmpty
		@NotBlank(message = "Adresa je obavezna")
		String address,

		@NotEmpty(message = "Korisnik mora imati najmanje jednu ulogu")
		Set<Long> roleIds
	    //Collection<Role> roles
		) {
}
