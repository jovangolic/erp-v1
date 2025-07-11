package com.jovan.erp_v1.controller;

import java.util.List;
import java.time.LocalDate;

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
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
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

    /*
     * @GetMapping("/by-fiscalYear-status")
     * public ResponseEntity<List<FiscalYearResponse>>
     * findByStatus(@RequestParam("status") FiscalYearStatus status) {
     * List<FiscalYearResponse> responses = fiscalYearService.findByStatus(status);
     * return ResponseEntity.ok(responses);
     * }
     */

    @GetMapping("/date-range")
    public ResponseEntity<List<FiscalYearResponse>> findBetweenStartAndEndDates(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<FiscalYearResponse> responses = fiscalYearService.findBetweenStartAndEndDates(start, end);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-year")
    public ResponseEntity<FiscalYearResponse> findByYear(@RequestParam("year") int year) {
        FiscalYearResponse response = fiscalYearService.findByYear(year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-status-and-year")
    public ResponseEntity<FiscalYearResponse> findByYearStatusAndYear(@RequestParam("status") FiscalYearStatus status,
            @RequestParam("year") Integer year) {
        FiscalYearResponse response = fiscalYearService.findByYearStatusAndYear(status, year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-status-order")
    public ResponseEntity<FiscalYearResponse> findFirstByYearStatusOrderByStartDateDesc(
            @RequestParam("status") FiscalYearStatus status) {
        FiscalYearResponse response = fiscalYearService.findFirstByYearStatusOrderByStartDateDesc(status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/startDate-after")
    public ResponseEntity<List<FiscalYearResponse>> findByStartDateAfter(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<FiscalYearResponse> responses = fiscalYearService.findByStartDateAfter(date);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/endDate-before")
    public ResponseEntity<List<FiscalYearResponse>> findByEndDateBefore(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<FiscalYearResponse> responses = fiscalYearService.findByEndDateBefore(date);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-yearStatus")
    public ResponseEntity<List<FiscalYearResponse>> findByYearStatus(
            @RequestParam("yearStatus") FiscalYearStatus yearStatus) {
        List<FiscalYearResponse> responses = fiscalYearService.findByYearStatus(yearStatus);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-quarterStatus")
    public ResponseEntity<List<FiscalYearResponse>> findByQuarterStatus(
            @RequestParam("quarterStatus") FiscalQuarterStatus quarterStatus) {
        List<FiscalYearResponse> responses = fiscalYearService.findByQuarterStatus(quarterStatus);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/quarterStatus-less-than")
    public ResponseEntity<List<FiscalYearResponse>> findByQuarterLessThan(
            @RequestParam("quarterStatus") FiscalQuarterStatus quarterStatus) {
        List<FiscalYearResponse> responses = fiscalYearService.findByQuarterLessThan(quarterStatus);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/quarterStatus-greater-than")
    public ResponseEntity<List<FiscalYearResponse>> findByQuarterGreaterThan(
            @RequestParam("quarterStatus") FiscalQuarterStatus quarterStatus) {
        List<FiscalYearResponse> responses = fiscalYearService.findByQuarterGreaterThan(quarterStatus);
        return ResponseEntity.ok(responses);
    }

}
