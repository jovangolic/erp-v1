package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.SecuritySettingRequest;
import com.jovan.erp_v1.response.SecuritySettingResponse;
import com.jovan.erp_v1.service.ISecuritySettingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/securitySettings")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
public class SecuritySettingController {

    private final ISecuritySettingService securitySettingService;

    @GetMapping("/{name}")
    public ResponseEntity<SecuritySettingResponse> getSetting(@PathVariable String name) {
        SecuritySettingResponse setting = securitySettingService.getByName(name);
        return ResponseEntity.ok(setting);
    }

    @PutMapping
    public ResponseEntity<Void> updateSetting(@RequestBody SecuritySettingRequest request) {
        securitySettingService.updateSetting(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<SecuritySettingResponse>> getAllSettings() {
        List<SecuritySettingResponse> settings = securitySettingService.getAllSettings();
        return ResponseEntity.ok(settings);
    }
}