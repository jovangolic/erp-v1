package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.SecuritySettingRequest;
import com.jovan.erp_v1.response.SecuritySettingResponse;
import com.jovan.erp_v1.service.ISecuritySettingService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/securitySettings")
@PreAuthorize(RoleGroups.SECURITY_SETTING_FULL_ACCESS)
public class SecuritySettingController {

    private final ISecuritySettingService securitySettingService;

    @GetMapping("/{name}")
    public ResponseEntity<SecuritySettingResponse> getSetting(@PathVariable String name) {
        SecuritySettingResponse setting = securitySettingService.getByName(name);
        return ResponseEntity.ok(setting);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SecuritySettingResponse> updateSetting(@PathVariable Long id, @RequestBody SecuritySettingRequest request) {
        SecuritySettingResponse updated = securitySettingService.updateSetting(id,request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<SecuritySettingResponse>> getAllSettings() {
        List<SecuritySettingResponse> settings = securitySettingService.getAllSettings();
        return ResponseEntity.ok(settings);
    }
    
    @PostMapping("/save")
    public ResponseEntity<SecuritySettingResponse> saveSecuritySettings(@Valid @RequestBody SecuritySettingRequest request){
    	SecuritySettingResponse items = securitySettingService.saveSecuritySettings(request);
    	return ResponseEntity.ok(items);
    }
}