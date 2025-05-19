package com.jovan.erp_v1.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.jovan.erp_v1.request.EmailRequest;
import com.jovan.erp_v1.service.IEmailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

	private final IEmailService emailService;
	
	@PostMapping("/send")
	public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequest emailRequest){
		emailService.sendEmail(emailRequest.to(), emailRequest.subject(), emailRequest.text());
		return  ResponseEntity.ok("Email send successfully");
	}
	
	
}
