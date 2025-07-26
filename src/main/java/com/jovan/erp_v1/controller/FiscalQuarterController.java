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
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
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
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYear_Id(
            @PathVariable Long fiscalYearId) {
        List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYear_Id(fiscalYearId);
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

    @GetMapping("/fiscalYear-year-status")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYear_YearStatus(@RequestParam("yearStatus") FiscalYearStatus yearStatus){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYear_YearStatus(yearStatus);
    	return ResponseEntity.ok(responses);
    }
    
    
    @GetMapping("/by-end-date")
    public ResponseEntity<List<FiscalQuarterResponse>> findByEndDate(@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByEndDate(endDate);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/fiscalYear-start-date-range")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYear_StartDateBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYear_StartDateBetween(start, end);
    	return ResponseEntity.ok(responses);
    }
    
    //nove metode
    
    @GetMapping("/quarter-status-q1")
    public ResponseEntity<List<FiscalQuarterResponse>> findByQuarterStatusQ1(){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByQuarterStatusQ1();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/quarter-status-q2")
    public ResponseEntity<List<FiscalQuarterResponse>> findByQuarterStatusQ2(){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByQuarterStatusQ2();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/quarter-status-q3")
    public ResponseEntity<List<FiscalQuarterResponse>> findByQuarterStatusQ3(){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByQuarterStatusQ3();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/quarter-status-q4")
    public ResponseEntity<List<FiscalQuarterResponse>> findByQuarterStatusQ4(){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByQuarterStatusQ4();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/year-status-open")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYearStatusOpen(){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYearStatusOpen();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/year-status-closed")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYearStatusClosed(){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYearStatusClosed();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/year-status-archived")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYearStatusArchived(){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYearStatusArchived();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/year-start-date")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYearStartDate(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYearStartDate(startDate);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/year-start-date-after")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYearStartDateAfter(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYearStartDateAfter(startDate);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/year-start-date-before")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYearStartDateBefore(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYearStartDateBefore(startDate);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/year-end-date")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYearEndDate(@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYearEndDate(endDate);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/year-start-date-range")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYearStartDateBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
    		@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYearStartDateBetween(start, end);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/year-end-date-range")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYearEndDateBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
    		@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYearEndDateBetween(start, end);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/year-quarter-status")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYear_QuarterStatus(@RequestParam("quarterStatus") FiscalQuarterStatus quarterStatus){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYear_QuarterStatus(quarterStatus);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search-active-quarters")
    public ResponseEntity<List<FiscalQuarterResponse>> findActiveQuarters(){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findActiveQuarters();
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/quarters-end-soon")
    public ResponseEntity<List<FiscalQuarterResponse>> findQuartersEndingSoon(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findQuartersEndingSoon(date);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/year-and-quarter-status")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYear_YearAndQuarterStatus(@RequestParam("year") Integer year,@RequestParam("status") FiscalQuarterStatus status){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYear_YearAndQuarterStatus(year, status);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/year-range")
    public ResponseEntity<List<FiscalQuarterResponse>> findByFiscalYearBetweenYears(@RequestParam("start") Integer start,@RequestParam("end")  Integer end){
    	List<FiscalQuarterResponse> responses = fiscalQuarterService.findByFiscalYearBetweenYears(start, end);
    	return ResponseEntity.ok(responses);
    }
}
