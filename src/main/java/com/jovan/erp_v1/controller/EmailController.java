package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.EmailGroupRequest;
import com.jovan.erp_v1.request.EmailRequest;
import com.jovan.erp_v1.request.CompanyEmailDTO;
import com.jovan.erp_v1.service.IEmailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIM','SUPERADMIN')")
public class EmailController {

	private final IEmailService emailService;

	@PostMapping("/send")
	public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequest emailRequest) {
		emailService.sendEmail(emailRequest.to(), emailRequest.subject(), emailRequest.text());
		return ResponseEntity.ok("Email send successfully");
	}

	/*
	 * @PostMapping("/send-multiple")
	 * public ResponseEntity<String> sendEmailToMultiple(@Valid @RequestBody
	 * EmailGroupRequest request){
	 * emailService.sendEmail(request.toString(), request.subject(),
	 * request.body());
	 * return ResponseEntity.ok("Emails sent successfully");
	 * }
	 */
	@PostMapping("/send-multiple")
	public ResponseEntity<String> sendEmailToMultiple(@Valid @RequestBody EmailGroupRequest request) {
		try {
			emailService.sendEmailToMultiple(request.recipients(), request.subject(), request.body()).join();
			return ResponseEntity.ok("All emails sent successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send all emails.");
		}
	}

	// Kreiranje jednog kompanijskog email-a
	@PostMapping("/create-company-email")
	public ResponseEntity<String> createCompanyEmail(@Valid @RequestBody CompanyEmailDTO employeeEmailDTO) {
		try {
			String email = emailService.createAccountWithCompanyEmail(
					employeeEmailDTO.firstName(),
					employeeEmailDTO.lastName(),
					employeeEmailDTO.types()).join(); // jer metoda vraća CompletableFuture<String>
			return ResponseEntity.ok("Company email created: " + email);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to create company email: " + e.getMessage());
		}
	}

	// Kreiranje više kompanijskih emailova
	@PostMapping("/create-company-emails")
	public ResponseEntity<String> createAllCompanyEmails(@Valid @RequestBody List<CompanyEmailDTO> users) {
		try {
			emailService.createAllCompanyEmails(users).join(); // CompletableFuture<Void>
			return ResponseEntity.ok("All company emails created successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to create company emails: " + e.getMessage());
		}
	}

	@PostMapping("/generate-company-email")
	public ResponseEntity<String> generateCompanyEmail(@RequestParam String firstName, @RequestParam String lastName) {
		try {
			String email = emailService.generateCompanyEmail(firstName, lastName).join();
			return ResponseEntity.ok("Company email generated: " + email);
		} catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
		}
	}

}
