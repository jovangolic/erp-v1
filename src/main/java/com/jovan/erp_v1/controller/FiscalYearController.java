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

import com.jovan.erp_v1.request.FiscalYearRequest;
import com.jovan.erp_v1.response.FiscalYearResponse;
import com.jovan.erp_v1.service.IFiscalYearService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fiscalYears")
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
public class FiscalYearController {

    private final IFiscalYearService fiscalYearService;

    @PostMapping("/create/new-fiscalYear")
    public ResponseEntity<FiscalYearResponse> create(@Valid @RequestBody FiscalYearRequest request) {
        FiscalYearResponse response = fiscalYearService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FiscalYearResponse> update(@PathVariable Long id,
            @Valid @RequestBody FiscalYearRequest request) {
        FiscalYearResponse response = fiscalYearService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fiscalYearService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-one/{id}")
    public ResponseEntity<FiscalYearResponse> findOne(@PathVariable Long id) {
        FiscalYearResponse response = fiscalYearService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<FiscalYearResponse>> findAll() {
        List<FiscalYearResponse> responses = fiscalYearService.findAll();
        return ResponseEntity.ok(responses);
    }

}
