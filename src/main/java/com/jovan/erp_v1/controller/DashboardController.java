package com.jovan.erp_v1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.response.DashboardResponse;
import com.jovan.erp_v1.service.IDashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
@CrossOrigin("http://localhost:5173")
@Tag(name = "Dashboard", description = "API za dashboard podatke")
@PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
public class DashboardController {

    private final IDashboardService dashboardService;

    @Operation(summary = "Vraća podatke za dashboard")
    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboardData() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }

}
