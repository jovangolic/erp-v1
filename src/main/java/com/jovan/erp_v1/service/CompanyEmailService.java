package com.jovan.erp_v1.service;


import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.jovan.erp_v1.exception.CompanyEmailErrorException;
import com.jovan.erp_v1.model.CompanyEmail;
import com.jovan.erp_v1.model.Role;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.CompanyEmailRepository;
import com.jovan.erp_v1.repository.RoleRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.CompanyEmailDTO;
import com.jovan.erp_v1.response.CompanyEmailResponse;
import com.jovan.erp_v1.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CompanyEmailService implements ICompanyEmailService {

    private final IEmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CompanyEmailRepository companyEmailRepository;

    public CompanyEmailService(
            IEmailService emailService,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            CompanyEmailRepository companyEmailRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyEmailRepository = companyEmailRepository;
    }

    @Override
    public CompletableFuture<String> generateCompanyEmail(String firstName, String lastName) {
        String email = (firstName + "." + lastName + "@firma.rs").toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already exists: " + email);
        }
        return CompletableFuture.completedFuture(email);
    }

    /*
     * @Override
     * public CompletableFuture<String> createAccountWithCompanyEmail(String
     * firstName, String lastName, RoleTypes role) {
     * String email = (firstName + "." + lastName + "@firma.rs").toLowerCase();
     * if (userRepository.existsByEmail(email)) {
     * return CompletableFuture.failedFuture(new
     * IllegalStateException("Email already exists: " + email));
     * }
     * 
     * return emailService.sendEmail(email, "Dobrodošli u ERP",
     * "Vaš nalog je kreiran sa rolom: " + role.name())
     * .thenApply(v -> {
     * // userRepository.save(...) ovde ide logika za kreiranje naloga
     * log.info("Nalog kreiran za: {}", email);
     * return email;
     * });
     * }
     */

    @Override
    public CompletableFuture<CompanyEmailResponse> createAccountWithCompanyEmail(CompanyEmailDTO dto) {
        String email = (dto.firstName() + "." + dto.lastName() + "@firma.rs").toLowerCase();
        if (userRepository.existsByEmail(email)) {
            return CompletableFuture.failedFuture(new IllegalStateException("Email already exists: " + email));
        }
        Role role = roleRepository.findByName("ROLE_" + dto.types().name())
                .orElseThrow(() -> new IllegalArgumentException("Rola ne postoji: " + dto.types().name()));
        String username = (dto.firstName() + "." + dto.lastName()).toLowerCase();
        String randomPassword = RandomUtil.generateRandomString(10);
        User user = new User();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(randomPassword));
        user.setAddress(dto.address());
        user.setPhoneNumber(dto.phoneNumber());
        user.setRoles(Set.of(role));
        User savedUser = userRepository.save(user);
        CompanyEmail companyEmail = new CompanyEmail();
        companyEmail.setEmail(email);
        companyEmail.setFirstName(dto.firstName());
        companyEmail.setLastName(dto.lastName());
        companyEmail.setRole(dto.types());
        companyEmail.setUser(savedUser);
        companyEmailRepository.save(companyEmail);
        return emailService.sendEmail(
                email,
                "Dobrodošli u ERP",
                "Vaš nalog je kreiran sa rolom: " + dto.types().name() +
                        "\nKorisničko ime: " + username +
                        "\nLozinka: " + randomPassword)
                .thenApply(v -> {
                    log.info("Nalog kreiran za: {}", email);
                    return mapToResponse(companyEmail, randomPassword);
                });
    }

    @Override
    public CompletableFuture<Void> createAllCompanyEmails(List<CompanyEmailDTO> users) {
        List<CompletableFuture<Void>> futures = users.stream()
                .map(dto -> createAccountWithCompanyEmail(dto)
                        .thenAccept(email -> log.info("Kreiran email za: {}", email)))
                .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public CompletableFuture<CompanyEmailResponse> findOne(String email) {
        return CompletableFuture.supplyAsync(() -> {
            CompanyEmail companyEmail = companyEmailRepository.findByEmail(email)
                    .orElseThrow(() -> new CompanyEmailErrorException("Company email not found: " + email));
            return mapToResponse(companyEmail, "N/A"); // lozinka se ne čuva u entitetu
        });
    }

    @Override
    public CompletableFuture<List<CompanyEmailResponse>> findAll() {
        return CompletableFuture.supplyAsync(() -> companyEmailRepository.findAll().stream()
                .map(c -> mapToResponse(c, "N/A"))
                .toList());
    }

    @Override
    public CompletableFuture<Void> deleteEmail(String email) {
        return CompletableFuture.runAsync(() -> {
            CompanyEmail companyEmail = companyEmailRepository.findByEmail(email)
                    .orElseThrow(() -> new CompanyEmailErrorException("Company email not found: " + email));
            companyEmailRepository.delete(companyEmail);
        });
    }

    private CompanyEmailResponse mapToResponse(CompanyEmail companyEmail, String rawPassword) {
        return new CompanyEmailResponse(
                companyEmail.getEmail(),
                companyEmail.getFirstName(),
                companyEmail.getLastName(),
                companyEmail.getRole(),
                companyEmail.getCreatedAt(),
                rawPassword);
    }

}
