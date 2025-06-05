package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.LocalizedOptionRequest;
import com.jovan.erp_v1.response.LocalizedOptionResponse;
import com.jovan.erp_v1.service.ILocalizedOptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/localizedOption")
public class LocalizedOptionController {

    private final ILocalizedOptionService localizedOptionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<LocalizedOptionResponse> create(@Valid @RequestBody LocalizedOptionRequest request) {
        return ResponseEntity.ok(localizedOptionService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<LocalizedOptionResponse>> getAll() {
        return ResponseEntity.ok(localizedOptionService.getAll());
    }

    @GetMapping("/option/{optionId}/translations")
    public ResponseEntity<List<LocalizedOptionResponse>> getTranslationsForOption(@PathVariable Long optionId) {
        List<LocalizedOptionResponse> translations = localizedOptionService.getTranslationsForOption(optionId);
        return ResponseEntity.ok(translations);
    }

    // Dodavanje novog prevoda za konkretnu opciju
    @PostMapping("/option/{optionId}/translations")
    public ResponseEntity<LocalizedOptionResponse> addTranslationForOption(
            @PathVariable Long optionId,
            @Valid @RequestBody LocalizedOptionRequest request) {
        LocalizedOptionResponse createdTranslation = localizedOptionService.addTranslationForOption(optionId, request);
        return ResponseEntity.ok(createdTranslation);
    }
}
