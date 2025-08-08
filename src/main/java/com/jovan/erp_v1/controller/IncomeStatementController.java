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
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.request.IncomeStatementRequest;
import com.jovan.erp_v1.response.IncomeStatementResponse;
import com.jovan.erp_v1.service.IntIncomeStatementService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomeStatements")
@PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
public class IncomeStatementController {

    private final IntIncomeStatementService incomeStatementService;

    @PreAuthorize(RoleGroups.INCOME_STATEMENT_FULL_ACCESS)
    @PostMapping("/create/new-incomeStatement")
    public ResponseEntity<IncomeStatementResponse> create(@Valid @RequestBody IncomeStatementRequest request) {
        IncomeStatementResponse response = incomeStatementService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.INCOME_STATEMENT_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<IncomeStatementResponse> update(@PathVariable Long id,
            @Valid @RequestBody IncomeStatementRequest request) {
        IncomeStatementResponse response = incomeStatementService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.INCOME_STATEMENT_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<IncomeStatementResponse> delete(@PathVariable Long id) {
        incomeStatementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<IncomeStatementResponse> findOne(@PathVariable Long id) {
        IncomeStatementResponse response = incomeStatementService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<IncomeStatementResponse>> findAll() {
        List<IncomeStatementResponse> responses = incomeStatementService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/totalRevenue")
    public ResponseEntity<List<IncomeStatementResponse>> findByTotalRevenue(
            @RequestParam("totalRevenue") BigDecimal totalRevenue) {
        List<IncomeStatementResponse> responses = incomeStatementService.findByTotalRevenue(totalRevenue);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    
    @GetMapping("/totalExpenses")
    public ResponseEntity<List<IncomeStatementResponse>> findByTotalExpenses(
            @RequestParam("totalExpenses") BigDecimal totalExpenses) {
        List<IncomeStatementResponse> responses = incomeStatementService.findByTotalExpenses(totalExpenses);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/netProfit")
    public ResponseEntity<List<IncomeStatementResponse>> findByNetProfit(
            @RequestParam("netProfit") BigDecimal netProfit) {
        List<IncomeStatementResponse> responses = incomeStatementService.findByNetProfit(netProfit);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/by-fiscal-year")
    public ResponseEntity<List<IncomeStatementResponse>> findByFiscalYear_Year(@RequestParam("year") Integer year) {
        List<IncomeStatementResponse> responses = incomeStatementService.findByFiscalYear_Year(year);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/fiscalYear-quarterStatus")
    public ResponseEntity<List<IncomeStatementResponse>> findByFiscalYear_QuarterStatus(
            @RequestParam("quarterStatus") FiscalQuarterStatus quarterStatus) {
        List<IncomeStatementResponse> responses = incomeStatementService.findByFiscalYear_QuarterStatus(quarterStatus);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/period-start-between")
    public ResponseEntity<List<IncomeStatementResponse>> findByPeriodStartBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<IncomeStatementResponse> responses = incomeStatementService.findByPeriodStartBetween(start, end);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/period-end-between")
    public ResponseEntity<List<IncomeStatementResponse>> findByPeriodEndBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<IncomeStatementResponse> responses = incomeStatementService.findByPeriodEndBetween(start, end);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/within-period")
    public ResponseEntity<List<IncomeStatementResponse>> findWithinPeriod(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<IncomeStatementResponse> responses = incomeStatementService.findWithinPeriod(start, end);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/contains-date")
    public ResponseEntity<List<IncomeStatementResponse>> findByDateWithinPeriod(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start) {
        List<IncomeStatementResponse> responses = incomeStatementService.findByDateWithinPeriod(start);
        return ResponseEntity.ok(responses);
    }
    
    //nove metode
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/net-profit/monthly/{year}")
    public ResponseEntity<List<MonthlyNetProfitDTO>> getMonthlyNetProfit(@PathVariable Integer year) {
        return ResponseEntity.ok(incomeStatementService.getMonthlyNetProfitForYear(year));
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/calculate-net-proft-by-year/{fiscalYearId}")
    public ResponseEntity<BigDecimal> calculateTotalNetProfitByFiscalYear(@PathVariable Long fiscalYearId){
    	BigDecimal responses = incomeStatementService.calculateTotalNetProfitByFiscalYear(fiscalYearId);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/find-net-profit-by-year/{fiscalYearId}")
    public ResponseEntity<BigDecimal> findTotalNetProfitByFiscalYear(@PathVariable Long fiscalYearId){
    	BigDecimal responses = incomeStatementService.findTotalNetProfitByFiscalYear(fiscalYearId);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/total-revenue-greater-than")
    public ResponseEntity<List<IncomeStatementResponse>> findByTotalRevenueGreaterThan(@RequestParam("totalRevenue") BigDecimal totalRevenue){
    	List<IncomeStatementResponse> responses = incomeStatementService.findByTotalRevenueGreaterThan(totalRevenue);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/total-expenses-greater-than")
    public ResponseEntity<List<IncomeStatementResponse>> findByTotalExpensesGreaterThan(@RequestParam("totalExpenses") BigDecimal totalExpenses){
    	List<IncomeStatementResponse> responses = incomeStatementService.findByTotalExpensesGreaterThan(totalExpenses);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/net-profit-greater-than")
    public ResponseEntity<List<IncomeStatementResponse>> findByNetProfitGreaterThan(@RequestParam("netProfit") BigDecimal netProfit){
    	List<IncomeStatementResponse> responses = incomeStatementService.findByNetProfitGreaterThan(netProfit);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/total-revenue-less-than")
    public ResponseEntity<List<IncomeStatementResponse>> findByTotalRevenueLessThan(@RequestParam("totalRevenue") BigDecimal totalRevenue){
    	List<IncomeStatementResponse> responses = incomeStatementService.findByTotalRevenueLessThan(totalRevenue);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/total-expenses-less-than")
    public ResponseEntity<List<IncomeStatementResponse>> findByTotalExpensesLessThan(@RequestParam("totalExpenses") BigDecimal totalExpenses){
    	List<IncomeStatementResponse> responses = incomeStatementService.findByTotalExpensesLessThan(totalExpenses);
    	return ResponseEntity.ok(responses);
    }
     
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/net-profit-less-than")
    public ResponseEntity<List<IncomeStatementResponse>> findByNetProfitLessThan(@RequestParam("netProfit") BigDecimal netProfit){
    	List<IncomeStatementResponse> responses = incomeStatementService.findByNetProfitLessThan(netProfit);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search-year-status")
    public ResponseEntity<List<IncomeStatementResponse>> findByFiscalYear_YearStatus(@RequestParam("yearStatus") FiscalYearStatus yearStatus){
    	List<IncomeStatementResponse> responses = incomeStatementService.findByFiscalYear_YearStatus(yearStatus);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search-year-status-and-quarter-status")
    public ResponseEntity<List<IncomeStatementResponse>> findByFiscalYear_QuarterStatusAndYearStatus(@RequestParam("yearStatus") FiscalYearStatus yearStatus,
    		@RequestParam("quarterStatus") FiscalQuarterStatus quarterStatus){
    	List<IncomeStatementResponse> responses = incomeStatementService.findByFiscalYear_QuarterStatusAndYearStatus(yearStatus, quarterStatus);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/total-revenue-date-range")
    public ResponseEntity<BigDecimal> sumTotalRevenueBetweenDates(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
    		@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
    	BigDecimal responses = incomeStatementService.sumTotalRevenueBetweenDates(start, end);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/total-expenses-date-range")
    public ResponseEntity<BigDecimal> sumTotalExpensesBetweenDates(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
    		@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
    	BigDecimal responses = incomeStatementService.sumTotalExpensesBetweenDates(start, end);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/net-profit-date-range")
    public ResponseEntity<BigDecimal> sumNetProfitBetweenDates(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
    		@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
    	BigDecimal responses = incomeStatementService.sumNetProfitBetweenDates(start, end);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/net-profit-quarter-status")
    public ResponseEntity<BigDecimal> sumNetProfitByQuarterStatus(@RequestParam("quarterStatus") FiscalQuarterStatus quarterStatus){
    	BigDecimal responses = incomeStatementService.sumNetProfitByQuarterStatus(quarterStatus);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/quarter-status-min-revenue")
    public ResponseEntity<List<IncomeStatementResponse>> findByQuarterStatusAndMinRevenue(@RequestParam("quarterStatus") FiscalQuarterStatus quarterStatus,
    		@RequestParam("minRevenue") BigDecimal minRevenue){
    	List<IncomeStatementResponse> responses = incomeStatementService.findByQuarterStatusAndMinRevenue(quarterStatus, minRevenue);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/revenue-by-year-status")
    public ResponseEntity<BigDecimal> sumRevenueByFiscalYearStatus(@RequestParam("yearStatus") FiscalYearStatus yearStatus){
    	BigDecimal responses = incomeStatementService.sumRevenueByFiscalYearStatus(yearStatus);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/fiscal-year-start-date")
    public ResponseEntity<List<IncomeStatementResponse>> findByFiscalYear_StartDate(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate){
    	List<IncomeStatementResponse> responses = incomeStatementService.findByFiscalYear_StartDate(startDate);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/fiscal-year-end-date")
    public ResponseEntity<List<IncomeStatementResponse>> findByFiscalYear_EndDate(@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
    	List<IncomeStatementResponse> responses = incomeStatementService.findByFiscalYear_EndDate(endDate);
    	return ResponseEntity.ok(responses);
    }
     
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/sum-total-revenue-date-range")
    public ResponseEntity<BigDecimal> sumTotalRevenue(@RequestParam("start")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
    		@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
    	BigDecimal responses = incomeStatementService.sumTotalRevenue(start, end);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/sum-total-expenses-date-range")
    public ResponseEntity<BigDecimal> sumTotalExpenses(@RequestParam("start")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate start,
    		@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
    	BigDecimal responses = incomeStatementService.sumTotalExpenses(start, end);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/sum-net-profit-date-range")
    public ResponseEntity<BigDecimal> sumNetProfit(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
    		@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
    	BigDecimal responses = incomeStatementService.sumNetProfit(start, end);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INCOME_STATEMENT_READ_ACCESS)
    @GetMapping("/search/net-profit-by-year-status")
    public ResponseEntity<BigDecimal> sumNetProfitByYearStatus(@RequestParam("yearStatus") FiscalYearStatus yearStatus){
    	BigDecimal responses = incomeStatementService.sumNetProfitByYearStatus(yearStatus);
    	return ResponseEntity.ok(responses);
    }
    
}
