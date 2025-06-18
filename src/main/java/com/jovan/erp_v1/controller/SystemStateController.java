package com.jovan.erp_v1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.SystemStateRequest;
import com.jovan.erp_v1.response.SystemStateResponse;
import com.jovan.erp_v1.service.ISystemStateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system-states")
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
public class SystemStateController {

    private final ISystemStateService service;

    @GetMapping("/current-state")
    public ResponseEntity<SystemStateResponse> getCurrentState() {
        return ResponseEntity.ok(service.getCurrentState());
    }

    @PutMapping
    public ResponseEntity<Void> updateState(@Valid @RequestBody SystemStateRequest request) {
        service.updateState(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/restart")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> restartSystem() {
        service.updateRestartTime();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/maintenance-mode")
    public ResponseEntity<Void> toggleMaintenance(@RequestParam boolean enabled) {
        service.setMaintenanceMode(enabled);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/registration-enabled")
    public ResponseEntity<Void> toggleRegistrationEnabled(@RequestParam boolean enabled) {
        service.setRegistrationEnabled(enabled);
        return ResponseEntity.ok().build();
    }
}
