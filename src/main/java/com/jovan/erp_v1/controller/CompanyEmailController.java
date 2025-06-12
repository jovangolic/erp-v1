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

import com.jovan.erp_v1.request.CompanyEmailDTO;
import com.jovan.erp_v1.service.ICompanyEmailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/companyEmail")
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
public class CompanyEmailController {

    private final ICompanyEmailService companyEmailService;

    // Kreiranje jednog kompanijskog email-a
    @PostMapping("/create-company-email")
    public ResponseEntity<String> createCompanyEmail(@Valid @RequestBody CompanyEmailDTO employeeEmailDTO) {
        try {
            String email = companyEmailService.createAccountWithCompanyEmail(
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
            companyEmailService.createAllCompanyEmails(users).join(); // CompletableFuture<Void>
            return ResponseEntity.ok("All company emails created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create company emails: " + e.getMessage());
        }
    }

    @PostMapping("/generate-company-email")
    public ResponseEntity<String> generateCompanyEmail(@RequestParam String firstName, @RequestParam String lastName) {
        try {
            String email = companyEmailService.generateCompanyEmail(firstName, lastName).join();
            return ResponseEntity.ok("Company email generated: " + email);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
}
