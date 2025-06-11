package com.jovan.erp_v1.service;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.repository.EmailSettingRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService implements IEmailService {

    private final JavaMailSender mailSender;
    private final EmailSettingRepository emailSettingRepository;

    public EmailService(JavaMailSender mailSender, EmailSettingRepository emailSettingRepository) {
        this.mailSender = mailSender;
        this.emailSettingRepository = emailSettingRepository;
    }

    @Override
    @Async
    public CompletableFuture<Void> sendEmail(String to, String subject, String text) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // true za HTML sadržaj
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
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            File attachmentFile = new File(attachmentFilePath);
            helper.addAttachment(attachmentFile.getName(), attachmentFile);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}", to, e);
        }
    }

    @Override
    public void sendEmailToMultiple(List<String> recipients, String subject, String body) {
        recipients.forEach(to -> sendEmail(to, subject, body));
    }
}
