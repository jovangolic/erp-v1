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

import com.jovan.erp_v1.enumeration.BalanceSheetStatus;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.request.BalanceSheetRequest;
import com.jovan.erp_v1.request.BalanceSheetSearchRequest;
import com.jovan.erp_v1.response.BalanceSheetResponse;
import com.jovan.erp_v1.save_as.BalanceSheetSaveAsRequest;
import com.jovan.erp_v1.search_request.BalanceSheetGeneralSearchRequest;
import com.jovan.erp_v1.service.IBalanceSheetService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/balanceSheets")
@PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
public class BalanceSheetController {

    private final IBalanceSheetService balanceSheetService;

    @PreAuthorize(RoleGroups.BALANCE_SHEET_FULL_ACCESS)
    @PostMapping("/create/new-balance-sheet")
    public ResponseEntity<BalanceSheetResponse> create(@Valid @RequestBody BalanceSheetRequest request) {
        BalanceSheetResponse response = balanceSheetService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.BALANCE_SHEET_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<BalanceSheetResponse> update(@PathVariable Long id,
            @Valid @RequestBody BalanceSheetRequest request) {
        BalanceSheetResponse response = balanceSheetService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.BALANCE_SHEET_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        balanceSheetService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<BalanceSheetResponse> findOne(@PathVariable Long id) {
        BalanceSheetResponse response = balanceSheetService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<BalanceSheetResponse>> findAll() {
        List<BalanceSheetResponse> response = balanceSheetService.findAll();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/by-date")
    public ResponseEntity<BalanceSheetResponse> findByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        BalanceSheetResponse response = balanceSheetService.findByDate(date);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/date-between")
    public ResponseEntity<List<BalanceSheetResponse>> findByDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByDateBetween(start, end);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/totalAssets")
    public ResponseEntity<List<BalanceSheetResponse>> findByTotalAssets(
            @RequestParam("totalAssets") BigDecimal totalAssets) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByTotalAssets(totalAssets);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/totalLiabilities")
    public ResponseEntity<List<BalanceSheetResponse>> findByTotalLiabilities(
            @RequestParam("totalLiabilities") BigDecimal totalLiabilities) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByTotalLiabilities(totalLiabilities);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/totalEquity")
    public ResponseEntity<List<BalanceSheetResponse>> findByTotalEquity(
            @RequestParam("totalEquity") BigDecimal totalEquity) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByTotalEquity(totalEquity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/fiscalYear/{id}")
    public ResponseEntity<List<BalanceSheetResponse>> findByFiscalYear_Id(@PathVariable Long id) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByFiscalYear_Id(id);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/fiscalYear-year")
    public ResponseEntity<List<BalanceSheetResponse>> findByFiscalYear_Year(@RequestParam("year") Integer year) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByFiscalYear_Year(year);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/fiscalYeay-yearStatus")
    public ResponseEntity<List<BalanceSheetResponse>> findByFiscalYear_YearStatus(FiscalYearStatus yearStatus) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByFiscalYear_YearStatus(yearStatus);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/fiscalYear-quarterStatus")
    public ResponseEntity<List<BalanceSheetResponse>> findByFiscalYear_QuarterStatus(
            @RequestParam("quarterStatus") FiscalQuarterStatus quarterStatus) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByFiscalYear_QuarterStatus(quarterStatus);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/by-statu-dateRange")
    public ResponseEntity<List<BalanceSheetResponse>> findByStatusAndDateRange(
            @RequestParam("status") FiscalYearStatus status,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<BalanceSheetResponse> responses = balanceSheetService.findByStatusAndDateRange(status, start, end);
        return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/totalAssets-greater-than")
    public ResponseEntity<List<BalanceSheetResponse>> findByTotalAssetsGreaterThan(@RequestParam("totalAssets") BigDecimal totalAssets){
    	List<BalanceSheetResponse> responses = balanceSheetService.findByTotalAssetsGreaterThan(totalAssets);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/totalAssets-less-than")
    public ResponseEntity<List<BalanceSheetResponse>> findByTotalAssetsLessThan(@RequestParam("totalAssets") BigDecimal totalAssets){
    	List<BalanceSheetResponse> responses = balanceSheetService.findByTotalAssetsLessThan(totalAssets);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/totalEquity-greater-than")
    public ResponseEntity<List<BalanceSheetResponse>> findByTotalEquityGreaterThan(@RequestParam("totalEquity") BigDecimal totalEquity){
    	List<BalanceSheetResponse> responses = balanceSheetService.findByTotalEquityGreaterThan(totalEquity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/totalEquity-less-than") 
    public ResponseEntity<List<BalanceSheetResponse>> findByTotalEquityLessThan(@RequestParam("totalEquity") BigDecimal totalEquity){
    	List<BalanceSheetResponse> responses = balanceSheetService.findByTotalAssetsLessThan(totalEquity);
    	return ResponseEntity.ok(responses);
    }
    
    //nove metode
    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @PostMapping("/search")
    public ResponseEntity<List<BalanceSheetResponse>> searchBalanceSheets(@RequestBody BalanceSheetSearchRequest request) {
        List<BalanceSheetResponse> result = balanceSheetService.searchBalanceSheets(request);
        return ResponseEntity.ok(result);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/by-total-liabilities-less-than")
    public ResponseEntity<List<BalanceSheetResponse>> findByTotalLiabilitiesLessThan(@RequestParam("totalLiabilities") BigDecimal totalLiabilities){
    	List<BalanceSheetResponse> result = balanceSheetService.findByTotalLiabilitiesLessThan(totalLiabilities);
    	return ResponseEntity.ok(result);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/filter-balance-sheet")
    public ResponseEntity<List<BalanceSheetResponse>> searchBalanceSheets(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("fiscalYearId") Long fiscalYearId,
            @RequestParam("minAssets") BigDecimal minAssets) {
       List<BalanceSheetResponse> result = balanceSheetService.searchBalanceSheets(startDate, endDate, fiscalYearId, minAssets);
       return ResponseEntity.ok(result);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/by-total-liabilities-greater-than")
    public ResponseEntity<List<BalanceSheetResponse>> findByTotalLiabilitiesGreaterThan(@RequestParam("totalLiabilities") BigDecimal totalLiabilities){
    	List<BalanceSheetResponse> result = balanceSheetService.findByTotalLiabilitiesGreaterThan(totalLiabilities);
    	return ResponseEntity.ok(result);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/find-solvent-balance-sheet")
    public ResponseEntity<List<BalanceSheetResponse>> findSolventBalanceSheets(){
    	List<BalanceSheetResponse> result = balanceSheetService.findSolventBalanceSheets();
    	return ResponseEntity.ok(result);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/find-first-by-order-date-desc")
    public ResponseEntity<BalanceSheetResponse> findFirstByOrderByDateDesc(){
    	BalanceSheetResponse item = balanceSheetService.findFirstByOrderByDateDesc();
    	return ResponseEntity.ok(item);
    }
   
    //nove metode
    @PreAuthorize(RoleGroups.BALANCE_SHEET_READ_ACCESS)
    @GetMapping("/track/{id}")
    public ResponseEntity<BalanceSheetResponse> trackBalanceSheet(@PathVariable Long id){
    	BalanceSheetResponse items = balanceSheetService.trackBalanceSheet(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_FULL_ACCESS)
    @PostMapping("/{id}/confirm")
    public ResponseEntity<BalanceSheetResponse> confirmBalanceSheet(@PathVariable Long id){
    	BalanceSheetResponse items = balanceSheetService.confirmBalanceSheet(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_FULL_ACCESS)
    @PostMapping("/{id}/close")
    public ResponseEntity<BalanceSheetResponse> closeBalanceSheet(@PathVariable Long id){
    	BalanceSheetResponse items = balanceSheetService.closeBalanceSheet(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_FULL_ACCESS)
    @PostMapping("/{id}/cancel")
    public ResponseEntity<BalanceSheetResponse> cancelBalanceSheet(@PathVariable Long id){
    	BalanceSheetResponse items = balanceSheetService.cancelBalanceSheet(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_FULL_ACCESS)
    @PostMapping("/{id}/status/{status}")
    public ResponseEntity<BalanceSheetResponse> changeStatus(@PathVariable Long id,@PathVariable BalanceSheetStatus status){
    	BalanceSheetResponse items = balanceSheetService.changeStatus(id, status);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_FULL_ACCESS)
    @PostMapping("/save")
    public ResponseEntity<BalanceSheetResponse> saveBalanceSheet(@Valid @RequestBody BalanceSheetRequest request){
    	BalanceSheetResponse items = balanceSheetService.saveBalanceSheet(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_FULL_ACCESS)
    @PostMapping("/save-as")
    public ResponseEntity<BalanceSheetResponse> saveAs(@Valid @RequestBody BalanceSheetSaveAsRequest request){
    	BalanceSheetResponse items = balanceSheetService.saveAs(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_FULL_ACCESS)
    @PostMapping("/save-all")
    public ResponseEntity<List<BalanceSheetResponse>> saveAll(@RequestBody List<BalanceSheetRequest> requests){
    	List<BalanceSheetResponse> items = balanceSheetService.saveAll(requests);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.BALANCE_SHEET_FULL_ACCESS)
    @PostMapping("/general-search")
    public ResponseEntity<List<BalanceSheetResponse>> generalSearch(@Valid @RequestBody BalanceSheetGeneralSearchRequest req){
    	List<BalanceSheetResponse> items = balanceSheetService.generalSearch(req);
    	return ResponseEntity.ok(items);
    }
}
