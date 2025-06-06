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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.LogisticsProviderRequest;
import com.jovan.erp_v1.response.LogisticsProviderResponse;
import com.jovan.erp_v1.service.ILogisticsProviderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logistics-providers")
@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
public class LogisticsProviderController {

    private final ILogisticsProviderService logisticsProviderService;

    @PostMapping("/create/new/logistics-provider")
    public ResponseEntity<LogisticsProviderResponse> create(@Valid @RequestBody LogisticsProviderRequest request) {
        LogisticsProviderResponse response = logisticsProviderService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<LogisticsProviderResponse> update(@PathVariable Long id,
            @Valid @RequestBody LogisticsProviderRequest request) {
        LogisticsProviderResponse response = logisticsProviderService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logisticsProviderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-one/{id}")
    public ResponseEntity<LogisticsProviderResponse> findOne(@PathVariable Long id) {
        LogisticsProviderResponse response = logisticsProviderService.findOneById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<LogisticsProviderResponse>> findAll() {
        List<LogisticsProviderResponse> responses = logisticsProviderService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<LogisticsProviderResponse>> findByName(@RequestParam("name") String name) {
        List<LogisticsProviderResponse> responses = logisticsProviderService.findByName(name);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-fragment")
    public ResponseEntity<List<LogisticsProviderResponse>> findByNameContainingIgnoreCase(
            @RequestParam("fragment") String fragment) {
        List<LogisticsProviderResponse> responses = logisticsProviderService.findByNameContainingIgnoreCase(fragment);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<LogisticsProviderResponse>> searchByNameOrWebsite(@RequestParam("query") String query) {
        List<LogisticsProviderResponse> responses = logisticsProviderService.searchByNameOrWebsite(query);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-contactPhone")
    public ResponseEntity<LogisticsProviderResponse> findByContactPhone(
            @RequestParam("contactPhone") String contactPhone) {
        LogisticsProviderResponse response = logisticsProviderService.findByEmail(contactPhone);
        return ResponseEntity.ok(response);
    }

    @GetMapping("by-email")
    public ResponseEntity<LogisticsProviderResponse> findByEmail(@RequestParam("email") String email) {
        LogisticsProviderResponse response = logisticsProviderService.findByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("by-website")
    public ResponseEntity<LogisticsProviderResponse> findByWebsite(@RequestParam("website") String website) {
        LogisticsProviderResponse response = logisticsProviderService.findByWebsite(website);
        return ResponseEntity.ok(response);
    }

}
