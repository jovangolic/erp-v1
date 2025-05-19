package com.jovan.erp_v1.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;

public interface IEmailService {

	@Async
	public CompletableFuture<Void> sendEmail(String to, String subject, String text);
	
}
