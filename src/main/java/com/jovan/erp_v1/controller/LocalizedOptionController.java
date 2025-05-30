package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.LocalizedOptionRequest;
import com.jovan.erp_v1.response.LocalizedOptionResponse;
import com.jovan.erp_v1.service.ILocalizedOptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/localizedOption")
public class LocalizedOptionController {

    private final ILocalizedOptionService localizedOptionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<LocalizedOptionResponse> create(@RequestBody LocalizedOptionRequest request) {
        return ResponseEntity.ok(localizedOptionService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<LocalizedOptionResponse>> getAll() {
        return ResponseEntity.ok(localizedOptionService.getAll());
    }
}
