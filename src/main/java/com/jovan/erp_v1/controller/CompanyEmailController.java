package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.CompanyEmailDTO;
import com.jovan.erp_v1.response.CompanyEmailResponse;
import com.jovan.erp_v1.service.ICompanyEmailService;

import jakarta.persistence.EntityNotFoundException;
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
    public ResponseEntity<?> createCompanyEmail(@Valid @RequestBody CompanyEmailDTO dto) {
        try {
            CompanyEmailResponse response = companyEmailService.createAccountWithCompanyEmail(dto).join();
            return ResponseEntity.ok(response);
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

    @GetMapping("/company-email/{email}")
    public ResponseEntity<?> getCompanyEmail(@PathVariable String email) {
        try {
            CompanyEmailResponse response = companyEmailService.findOne(email).join();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company email not found: " + email);
        }
    }

    @GetMapping("/company-emails")
    public ResponseEntity<List<CompanyEmailResponse>> getAllCompanyEmails() {
        List<CompanyEmailResponse> emails = companyEmailService.findAll().join();
        if (emails.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(emails);
    }

    @DeleteMapping("/company-email/{email}")
    public ResponseEntity<String> deleteCompanyEmail(@PathVariable String email) {
        try {
            companyEmailService.deleteEmail(email).join();
            return ResponseEntity.ok("Company email deleted: " + email);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company email not found: " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete email: " + e.getMessage());
        }
    }
}
