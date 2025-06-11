package com.jovan.erp_v1.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;

public interface IEmailService {

	@Async
	public CompletableFuture<Void> sendEmail(String to, String subject, String text);

	// void sendEmail(String to, String subject, String body);
	void sendEmailToMultiple(List<String> recipients, String subject, String body);

}
