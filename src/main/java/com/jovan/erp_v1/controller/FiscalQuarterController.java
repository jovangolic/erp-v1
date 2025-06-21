package com.jovan.erp_v1.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.request.FiscalQuarterRequest;
import com.jovan.erp_v1.response.FiscalQuarterResponse;
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

    @GetMapping("/by-fiscalYear/{fiscalYearId}")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYear(
            @PathVariable Long fiscalYearId) {
        List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYearId(fiscalYearId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/quarterStatus")
    public ResponseEntity<List<FiscalQuarterResponse>> findByQuarterStatus(
            @RequestParam("status") FiscalQuarterStatus status) {
        List<FiscalQuarterResponse> responses = fiscalQuarterService.findByQuarterStatus(status);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/startDateBetween")
    public ResponseEntity<List<FiscalQuarterResponse>> findByStartDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<FiscalQuarterResponse> responses = fiscalQuarterService.findByStartDateBetween(start, end);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/fiscalYear/{fiscalYearId}/quarters")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYearAndQuarterStatus(
            @PathVariable Long fiscalYearId, @RequestParam("status") FiscalQuarterStatus status) {
        List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYearIdAndQuarterStatus(fiscalYearId,
                status);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/startDateAfter")
    public ResponseEntity<List<FiscalQuarterResponse>> findByStartDateAfter(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<FiscalQuarterResponse> responses = fiscalQuarterService.findByStartDateAfter(date);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/startDateBefore")
    public ResponseEntity<List<FiscalQuarterResponse>> findByStartDateBefore(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<FiscalQuarterResponse> responses = fiscalQuarterService.findByStartDateBefore(date);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/specific-fiscalYear")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYear_Year(@RequestParam("year") Integer year) {
        List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYear_Year(year);
        return ResponseEntity.ok(responses);
    }

}
