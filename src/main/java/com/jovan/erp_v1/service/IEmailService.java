package com.jovan.erp_v1.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;

import com.jovan.erp_v1.enumeration.RoleTypes;
import com.jovan.erp_v1.request.CompanyEmailDTO;

public interface IEmailService {

	@Async
	public CompletableFuture<Void> sendEmail(String to, String subject, String text);

	// void sendEmail(String to, String subject, String body);
	// void sendEmailToMultiple(List<String> recipients, String subject, String
	// body);
	@Async
	public CompletableFuture<Void> sendEmailToMultiple(List<String> recipients, String subject, String body);

	@Async
	CompletableFuture<String> generateCompanyEmail(String firstName, String lastName);

	@Async
	CompletableFuture<String> createAccountWithCompanyEmail(String firstName, String lastName, RoleTypes role);

	@Async
	CompletableFuture<Void> createAllCompanyEmails(List<CompanyEmailDTO> users);
}
