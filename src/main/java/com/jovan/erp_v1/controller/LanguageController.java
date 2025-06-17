package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.LanguageCodeType;
import com.jovan.erp_v1.enumeration.LanguageNameType;
import com.jovan.erp_v1.request.LanguageRequest;
import com.jovan.erp_v1.response.LanguageResponse;
import com.jovan.erp_v1.service.ILanguageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/language")
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
public class LanguageController {

    private final ILanguageService languageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<LanguageResponse> create(@Valid @RequestBody LanguageRequest request) {
        LanguageResponse response = languageService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<LanguageResponse> update(@PathVariable Long id, @Valid @RequestBody LanguageRequest request) {
        LanguageResponse response = languageService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        languageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-one/{id}")
    public ResponseEntity<LanguageResponse> findOne(@PathVariable Long id) {
        LanguageResponse response = languageService.findOneById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<LanguageResponse>> getALL() {
        List<LanguageResponse> responses = languageService.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-code/{code}")
    public ResponseEntity<LanguageResponse> findByCode(@PathVariable LanguageCodeType code) {
        LanguageResponse response = languageService.findByCodeType(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-name/{nameType}")
    public ResponseEntity<LanguageResponse> findByNameType(@PathVariable LanguageNameType nameType) {
        LanguageResponse response = languageService.findByNameType(nameType);
        return ResponseEntity.ok(response);
    }
}
