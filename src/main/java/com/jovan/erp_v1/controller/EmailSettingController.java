package com.jovan.erp_v1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.EmailSettingRequest;
import com.jovan.erp_v1.response.EmailSettingResponse;
import com.jovan.erp_v1.service.IEmailSettingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email-setting")
@PreAuthorize("hasRole('SUPERADMIN','ADMIN')")
public class EmailSettingController {

    private final IEmailSettingService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmailSettingResponse> getCurrentSettings() {
        return ResponseEntity.ok(service.getCurrentSettings());
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmailSettingResponse> updateSettings(@Valid @RequestBody EmailSettingRequest request) {
        return ResponseEntity.ok(service.updateSettings(request));
    }
}
