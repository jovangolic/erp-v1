package com.jovan.erp_v1.service;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService implements IEmailService {

	private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public CompletableFuture<Void> sendEmail(String to, String subject, String text) {
        val future = new CompletableFuture<Void>();

        try {
            val message = mailSender.createMimeMessage();
            val helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            // From address is automatically set by Spring Boot based on your properties
            helper.setSubject(subject);
            helper.setText(text, true); // Set the second parameter to true to send HTML content
            mailSender.send(message);

            log.info("Sent email to {}", to);
            future.complete(null);

        } catch (MessagingException | MailException e) {
            log.error("Failed to send email to {}", to, e);
            future.completeExceptionally(e);
        }

        return future;
    }
    
    public void sendEmailWithAttachment(String to, String subject, String text, String attachmentFilePath) {
        try {
            val message = mailSender.createMimeMessage();
            val helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // Set the second parameter to true to send HTML content

            // Add an attachment to the email
            val attachmentFile = new File(attachmentFilePath);
            helper.addAttachment(attachmentFile.getName(), attachmentFile);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}", to, e);
        }
    }
	
}
