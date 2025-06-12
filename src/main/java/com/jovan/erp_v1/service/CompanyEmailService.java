package com.jovan.erp_v1.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.enumeration.RoleTypes;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.CompanyEmailDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CompanyEmailService implements ICompanyEmailService {

    private final IEmailService emailService;
    private final UserRepository userRepository;

    public CompanyEmailService(IEmailService emailService, UserRepository userRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @Override
    public CompletableFuture<String> generateCompanyEmail(String firstName, String lastName) {
        String email = (firstName + "." + lastName + "@firma.rs").toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already exists: " + email);
        }
        return CompletableFuture.completedFuture(email);
    }

    @Override
    public CompletableFuture<String> createAccountWithCompanyEmail(String firstName, String lastName, RoleTypes role) {
        String email = (firstName + "." + lastName + "@firma.rs").toLowerCase();
        if (userRepository.existsByEmail(email)) {
            return CompletableFuture.failedFuture(new IllegalStateException("Email already exists: " + email));
        }

        return emailService.sendEmail(email, "Dobrodošli u ERP", "Vaš nalog je kreiran sa rolom: " + role.name())
                .thenApply(v -> {
                    // userRepository.save(...) ovde ide logika za kreiranje naloga
                    log.info("Nalog kreiran za: {}", email);
                    return email;
                });
    }

    @Override
    public CompletableFuture<Void> createAllCompanyEmails(List<CompanyEmailDTO> users) {
        List<CompletableFuture<Void>> futures = users.stream()
                .map(dto -> createAccountWithCompanyEmail(dto.firstName(), dto.lastName(), dto.types())
                        .thenAccept(email -> log.info("Kreiran email za: {}", email)))
                .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }
}
