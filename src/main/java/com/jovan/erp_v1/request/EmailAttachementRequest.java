package com.jovan.erp_v1.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailAttachementRequest(
		
		@NotBlank(message = "Email primaoca je obavezan")
		@Email(message = "Email format nije validan")
		String to,

		@NotBlank(message = "Naslov je obavezan")
		String subject,

		@NotBlank(message = "Tekst poruke je obavezan")
		String text,

		String attachmentFilePath // opcioni
		
		) {

}
