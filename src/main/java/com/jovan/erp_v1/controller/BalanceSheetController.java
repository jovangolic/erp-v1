package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
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
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.request.BalanceSheetRequest;
import com.jovan.erp_v1.response.BalanceSheetResponse;
import com.jovan.erp_v1.service.IBalanceSheetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/balanceSheets")
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
public class BalanceSheetController {

    private final IBalanceSheetService balanceSheetService;

    @PostMapping("/create/new-balance-sheet")
    public ResponseEntity<BalanceSheetResponse> create(@Valid @RequestBody BalanceSheetRequest request) {
        BalanceSheetResponse response = balanceSheetService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BalanceSheetResponse> update(@PathVariable Long id,
            @Valid @RequestBody BalanceSheetRequest request) {
        BalanceSheetResponse response = balanceSheetService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        balanceSheetService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-one/{id}")
    public ResponseEntity<BalanceSheetResponse> findOne(@PathVariable Long id) {
        BalanceSheetResponse response = balanceSheetService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<BalanceSheetResponse>> findAll() {
        List<BalanceSheetResponse> response = balanceSheetService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-date")
    public ResponseEntity<BalanceSheetResponse> findByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        BalanceSheetResponse response = balanceSheetService.findByDate(date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-between")
    public ResponseEntity<List<BalanceSheetResponse>> findByDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByDateBetween(start, end);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/totalAssets")
    public ResponseEntity<List<BalanceSheetResponse>> findByTotalAssets(
            @RequestParam("totalAssets") BigDecimal totalAssets) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByTotalAssets(totalAssets);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/totalLiabilities")
    public ResponseEntity<List<BalanceSheetResponse>> findByTotalLiabilities(
            @RequestParam("totalLiabilities") BigDecimal totalLiabilities) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByTotalLiabilities(totalLiabilities);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/totalEquity")
    public ResponseEntity<List<BalanceSheetResponse>> findByTotalEquity(
            @RequestParam("totalEquity") BigDecimal totalEquity) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByTotalEquity(totalEquity);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/fiscalYear/{id}")
    public ResponseEntity<List<BalanceSheetResponse>> findByFiscalYear_Id(@PathVariable Long id) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByFiscalYear_Id(id);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/fiscalYear-year")
    public ResponseEntity<List<BalanceSheetResponse>> findByFiscalYear_Year(@RequestParam("year") Integer year) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByFiscalYear_Year(year);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/fiscalYeay-yearStatus")
    public ResponseEntity<List<BalanceSheetResponse>> findByFiscalYear_YearStatus(FiscalYearStatus yearStatus) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByFiscalYear_YearStatus(yearStatus);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/fiscalYear-quarterStatus")
    public ResponseEntity<List<BalanceSheetResponse>> findByFiscalYear_QuarterStatus(
            @RequestParam("quarterStatus") FiscalQuarterStatus quarterStatus) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByFiscalYear_QuarterStatus(quarterStatus);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-statu-dateRange")
    public ResponseEntity<List<BalanceSheetResponse>> findByStatusAndDateRange(
            @RequestParam("status") FiscalYearStatus status,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByStatusAndDateRange(status, start, end);
        return ResponseEntity.ok(responses);
    }
    
    
    @GetMapping("/totalAssets-greater-than")
    public ResponseEntity<List<BalanceSheetResponse>> findByTotalAssetsGreaterThan(@RequestParam("totalAssets") BigDecimal totalAssets){
    	List<BalanceSheetResponse> responses = balanceSheetService.findByTotalAssetsGreaterThan(totalAssets);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/totalAssets-less-than")
    public ResponseEntity<List<BalanceSheetResponse>> findByTotalAssetsLessThan(@RequestParam("totalAssets") BigDecimal totalAssets){
    	List<BalanceSheetResponse> responses = balanceSheetService.findByTotalAssetsLessThan(totalAssets);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/totalEquity-greater-than")
    public ResponseEntity<List<BalanceSheetResponse>> findByTotalEquityGreaterThan(@RequestParam("totalEquity") BigDecimal totalEquity){
    	List<BalanceSheetResponse> responses = balanceSheetService.findByTotalEquityGreaterThan(totalEquity);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/totalEquity-less-than") 
    public ResponseEntity<List<BalanceSheetResponse>> findByTotalEquityLessThan(@RequestParam("totalEquity") BigDecimal totalEquity){
    	List<BalanceSheetResponse> responses = balanceSheetService.findByTotalAssetsLessThan(totalEquity);
    	return ResponseEntity.ok(responses);
    }

}
