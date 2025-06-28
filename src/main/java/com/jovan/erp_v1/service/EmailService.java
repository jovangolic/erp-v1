package com.jovan.erp_v1.service;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.RoleTypes;
import com.jovan.erp_v1.repository.EmailSettingRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.CompanyEmailDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService implements IEmailService {

    private final JavaMailSender mailSender;
    private final EmailSettingRepository emailSettingRepository;
    private final UserRepository userRepository;

    public EmailService(JavaMailSender mailSender, EmailSettingRepository emailSettingRepository,
            UserRepository userRepository) {
        this.mailSender = mailSender;
        this.emailSettingRepository = emailSettingRepository;
        this.userRepository = userRepository;
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
    @Async
    public CompletableFuture<Void> sendEmailToMultiple(List<String> recipients, String subject, String body) {
        List<CompletableFuture<Void>> futures = recipients.stream()
                .map(to -> sendEmail(to, subject, body))
                .collect(Collectors.toList());
        return CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> log.info("All emails sent to recipients."));
    }

    @Override
    @Async
    public CompletableFuture<String> generateCompanyEmail(String firstName, String lastName) {
        String email = (firstName + "." + lastName + "@firma.rs").toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already exists: " + email);
        }
        sendEmail(email, "Dobrodošli u ERP", "Vaš nalog je kreiran...");
        return CompletableFuture.completedFuture(email);
    }

    /*
     * @Override
     * 
     * @Async
     * public CompletableFuture<Void> createAccountWithCompanyEmail(String
     * firstName, String lastName, String role) {
     * return generateCompanyEmail(firstName, lastName)
     * .thenAccept(companyEmail -> {
     * // Logika za kreiranje korisnika u bazi sa tim emailom i ulogom
     * // primer: userRepository.save(...)
     * log.info("Kreiran nalog za {} {} sa emailom {}", firstName, lastName,
     * companyEmail);
     * });
     * }
     */

    @Override
    @Async
    public CompletableFuture<Void> createAllCompanyEmails(List<CompanyEmailDTO> users) {
        List<CompletableFuture<Void>> futures = users.stream()
                .map(dto -> createAccountWithCompanyEmail(dto.firstName(), dto.lastName(), dto.types())
                        .thenAccept(email -> {
                            // Možeš ovde logovati email ako želiš
                        }))
                .collect(Collectors.toList());
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    // Ova metoda NEĆE biti asinhrona i može biti privatna
    @Override
    @Async
    public CompletableFuture<String> createAccountWithCompanyEmail(String firstName, String lastName, RoleTypes role) {
        String email = (firstName + "." + lastName + "@firma.rs").toLowerCase();
        if (userRepository.existsByEmail(email)) {
            return CompletableFuture.failedFuture(new IllegalStateException("Email already exists: " + email));
        }
        // Snimanje korisnika u bazu, npr. userRepository.save(...)
        return sendEmail(email, "Dobrodošli u ERP", "Vaš nalog je kreiran sa rolom: " + role.name())
                .thenApply(v -> email); // vraća email nakon što se završi slanje
    }

    public void sendSimpleMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("torbicadraganjove910@gmail.com"); // možeš iz `MailProperties`
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
