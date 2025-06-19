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

import com.jovan.erp_v1.request.FiscalQuarterRequest;
import com.jovan.erp_v1.request.FiscalQuarterResponse;
import com.jovan.erp_v1.service.IFiscalQuarterService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fiscalQuarters")
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
public class FiscalQuarterController {

    private final IFiscalQuarterService fiscalQuarterService;

    @PostMapping("/create/new-fiscalQuarter")
    public ResponseEntity<FiscalQuarterResponse> create(@Valid @RequestBody FiscalQuarterRequest request) {
        FiscalQuarterResponse response = fiscalQuarterService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FiscalQuarterResponse> update(@PathVariable Long id,
            @RequestBody FiscalQuarterRequest request) {
        FiscalQuarterResponse response = fiscalQuarterService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fiscalQuarterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-one/{id}")
    public ResponseEntity<FiscalQuarterResponse> findOne(@PathVariable Long id) {
        FiscalQuarterResponse response = fiscalQuarterService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<FiscalQuarterResponse>> findAll() {
        List<FiscalQuarterResponse> responses = fiscalQuarterService.findAll();
        return ResponseEntity.ok(responses);
    }

}
