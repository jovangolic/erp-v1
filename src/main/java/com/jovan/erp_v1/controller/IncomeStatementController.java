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

import com.jovan.erp_v1.dto.MonthlyNetProfitDTO;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.request.IncomeStatementRequest;
import com.jovan.erp_v1.response.IncomeStatementResponse;
import com.jovan.erp_v1.service.IntIncomeStatementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomeStatements")
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
public class IncomeStatementController {

    private final IntIncomeStatementService incomeStatementService;

    @PostMapping("/create/new-incomeStatement")
    public ResponseEntity<IncomeStatementResponse> create(@Valid @RequestBody IncomeStatementRequest request) {
        IncomeStatementResponse response = incomeStatementService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<IncomeStatementResponse> update(@PathVariable Long id,
            @Valid @RequestBody IncomeStatementRequest request) {
        IncomeStatementResponse response = incomeStatementService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<IncomeStatementResponse> delete(@PathVariable Long id) {
        incomeStatementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-one/{id}")
    public ResponseEntity<IncomeStatementResponse> findOne(@PathVariable Long id) {
        IncomeStatementResponse response = incomeStatementService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<IncomeStatementResponse>> findAll() {
        List<IncomeStatementResponse> responses = incomeStatementService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/totalRevenue")
    public ResponseEntity<List<IncomeStatementResponse>> findByTotalRevenue(
            @RequestParam("totalRevenue") BigDecimal totalRevenue) {
        List<IncomeStatementResponse> responses = incomeStatementService.findByTotalRevenue(totalRevenue);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/totalExpenses")
    public ResponseEntity<List<IncomeStatementResponse>> findByTotalExpenses(
            @RequestParam("totalExpenses") BigDecimal totalExpenses) {
        List<IncomeStatementResponse> responses = incomeStatementService.findByTotalExpenses(totalExpenses);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/netProfit")
    public ResponseEntity<List<IncomeStatementResponse>> findByNetProfit(
            @RequestParam("netProfit") BigDecimal netProfit) {
        List<IncomeStatementResponse> responses = incomeStatementService.findByNetProfit(netProfit);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-fiscal-year")
    public ResponseEntity<List<IncomeStatementResponse>> findByFiscalYear_Year(@RequestParam("year") Integer year) {
        List<IncomeStatementResponse> responses = incomeStatementService.findByFiscalYear_Year(year);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/fiscalYear-quarterStatus")
    public ResponseEntity<List<IncomeStatementResponse>> findByFiscalYear_QuarterStatus(
            @RequestParam("quarterStatus") FiscalQuarterStatus quarterStatus) {
        List<IncomeStatementResponse> responses = incomeStatementService.findByFiscalYear_QuarterStatus(quarterStatus);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/period-start-between")
    public ResponseEntity<List<IncomeStatementResponse>> findByPeriodStartBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<IncomeStatementResponse> responses = incomeStatementService.findByPeriodStartBetween(start, end);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/period-end-between")
    public ResponseEntity<List<IncomeStatementResponse>> findByPeriodEndBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<IncomeStatementResponse> responses = incomeStatementService.findByPeriodEndBetween(start, end);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/within-period")
    public ResponseEntity<List<IncomeStatementResponse>> findWithinPeriod(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<IncomeStatementResponse> responses = incomeStatementService.findWithinPeriod(start, end);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/contains-date")
    public ResponseEntity<List<IncomeStatementResponse>> findByDateWithinPeriod(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start) {
        List<IncomeStatementResponse> responses = incomeStatementService.findByDateWithinPeriod(start);
        return ResponseEntity.ok(responses);
    }
    
    //nove metode
    
    @GetMapping("/net-profit/monthly/{year}")
    public ResponseEntity<List<MonthlyNetProfitDTO>> getMonthlyNetProfit(@PathVariable Integer year) {
        return ResponseEntity.ok(incomeStatementService.getMonthlyNetProfitForYear(year));
    }
}
