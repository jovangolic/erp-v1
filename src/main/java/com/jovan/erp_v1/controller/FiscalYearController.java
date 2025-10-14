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
import com.jovan.erp_v1.enumeration.FiscalYearTypeStatus;
import com.jovan.erp_v1.request.FiscalYearRequest;
import com.jovan.erp_v1.response.FiscalYearResponse;
import com.jovan.erp_v1.save_as.FiscalYearSaveAsRequest;
import com.jovan.erp_v1.search_request.FiscalYearSearchRequest;
import com.jovan.erp_v1.service.IFiscalYearService;
import com.jovan.erp_v1.statistics.fiscal_year.FiscalYearMonthlyStatDTO;
import com.jovan.erp_v1.statistics.fiscal_year.FiscalYearQuarterStatDTO;
import com.jovan.erp_v1.statistics.fiscal_year.FiscalYearStatusStatDTO;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fiscalYears")
@PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
public class FiscalYearController {

    private final IFiscalYearService fiscalYearService;

    @PreAuthorize(RoleGroups.FISCAL_YEAR_FULL_ACCESS)
    @PostMapping("/create/new-fiscalYear")
    public ResponseEntity<FiscalYearResponse> create(@Valid @RequestBody FiscalYearRequest request) {
        FiscalYearResponse response = fiscalYearService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.FISCAL_YEAR_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<FiscalYearResponse> update(@PathVariable Long id,
            @Valid @RequestBody FiscalYearRequest request) {
        FiscalYearResponse response = fiscalYearService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.FISCAL_YEAR_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fiscalYearService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<FiscalYearResponse> findOne(@PathVariable Long id) {
        FiscalYearResponse response = fiscalYearService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
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

    @PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
    @GetMapping("/date-range")
    public ResponseEntity<List<FiscalYearResponse>> findBetweenStartAndEndDates(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<FiscalYearResponse> responses = fiscalYearService.findBetweenStartAndEndDates(start, end);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
    @GetMapping("/by-year")
    public ResponseEntity<FiscalYearResponse> findByYear(@RequestParam("year") int year) {
        FiscalYearResponse response = fiscalYearService.findByYear(year);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
    @GetMapping("/by-status-and-year")
    public ResponseEntity<FiscalYearResponse> findByYearStatusAndYear(@RequestParam("status") FiscalYearStatus status,
            @RequestParam("year") Integer year) {
        FiscalYearResponse response = fiscalYearService.findByYearStatusAndYear(status, year);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
    @GetMapping("/by-status-order")
    public ResponseEntity<FiscalYearResponse> findFirstByYearStatusOrderByStartDateDesc(
            @RequestParam("status") FiscalYearStatus status) {
        FiscalYearResponse response = fiscalYearService.findFirstByYearStatusOrderByStartDateDesc(status);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
    @GetMapping("/startDate-after")
    public ResponseEntity<List<FiscalYearResponse>> findByStartDateAfter(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<FiscalYearResponse> responses = fiscalYearService.findByStartDateAfter(date);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
    @GetMapping("/endDate-before")
    public ResponseEntity<List<FiscalYearResponse>> findByEndDateBefore(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<FiscalYearResponse> responses = fiscalYearService.findByEndDateBefore(date);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
    @GetMapping("/by-yearStatus")
    public ResponseEntity<List<FiscalYearResponse>> findByYearStatus(
            @RequestParam("yearStatus") FiscalYearStatus yearStatus) {
        List<FiscalYearResponse> responses = fiscalYearService.findByYearStatus(yearStatus);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
    @GetMapping("/by-quarterStatus")
    public ResponseEntity<List<FiscalYearResponse>> findByQuarterStatus(
            @RequestParam("quarterStatus") FiscalQuarterStatus quarterStatus) {
        List<FiscalYearResponse> responses = fiscalYearService.findByQuarterStatus(quarterStatus);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
    @GetMapping("/quarterStatus-less-than")
    public ResponseEntity<List<FiscalYearResponse>> findByQuarterLessThan(
            @RequestParam("quarterStatus") FiscalQuarterStatus quarterStatus) {
        List<FiscalYearResponse> responses = fiscalYearService.findByQuarterLessThan(quarterStatus);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
    @GetMapping("/quarterStatus-greater-than")
    public ResponseEntity<List<FiscalYearResponse>> findByQuarterGreaterThan(
            @RequestParam("quarterStatus") FiscalQuarterStatus quarterStatus) {
        List<FiscalYearResponse> responses = fiscalYearService.findByQuarterGreaterThan(quarterStatus);
        return ResponseEntity.ok(responses);
    }

    //nove metode
    
    @PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
    @GetMapping("/count/by-year-and-month")
    public ResponseEntity<List<FiscalYearMonthlyStatDTO>> countFiscalYearsByYearAndMonth(){
    	List<FiscalYearMonthlyStatDTO> items = fiscalYearService.countFiscalYearsByYearAndMonth();
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
    @GetMapping("/count/by-status")
    public ResponseEntity<List<FiscalYearStatusStatDTO>> countByFiscalYearStatus(){
    	List<FiscalYearStatusStatDTO> items = fiscalYearService.countByFiscalYearStatus();
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
    @GetMapping("/count/by-quarter-status")
    public ResponseEntity<List<FiscalYearQuarterStatDTO>> countByFiscalYearQuarterStatus(){
    	List<FiscalYearQuarterStatDTO> items = fiscalYearService.countByFiscalYearQuarterStatus();
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.FISCAL_YEAR_READ_ACCESS)
    @GetMapping("/track/{id}")
    public ResponseEntity<FiscalYearResponse> trackFiscalYear(@PathVariable Long id){
    	FiscalYearResponse items = fiscalYearService.trackFiscalYear(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.FISCAL_YEAR_FULL_ACCESS)
    @PostMapping("/{id}/confirm")
    public ResponseEntity<FiscalYearResponse> confirmFiscalYear(@PathVariable Long id){
    	FiscalYearResponse items = fiscalYearService.confirmFiscalYear(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.FISCAL_YEAR_FULL_ACCESS)
    @PostMapping("/{id}/cancel")
    public ResponseEntity<FiscalYearResponse> cancelFiscalYear(@PathVariable Long id){
    	FiscalYearResponse items = fiscalYearService.cancelFiscalYear(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.FISCAL_YEAR_FULL_ACCESS)
    @PostMapping("/{id}/close")
    public ResponseEntity<FiscalYearResponse> closeFiscalYear(@PathVariable Long id){
    	FiscalYearResponse items = fiscalYearService.closeFiscalYear(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.FISCAL_YEAR_FULL_ACCESS)
    @PostMapping("/{id}/status/{status}")
    public ResponseEntity<FiscalYearResponse> changeStatus(@PathVariable Long id,@PathVariable  FiscalYearTypeStatus status){
    	FiscalYearResponse items = fiscalYearService.changeStatus(id, status);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.FISCAL_YEAR_FULL_ACCESS)
    @PostMapping("/save")
    public ResponseEntity<FiscalYearResponse> saveFiscalYear(@Valid @RequestBody FiscalYearRequest request){
    	FiscalYearResponse items = fiscalYearService.saveFiscalYear(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.FISCAL_YEAR_FULL_ACCESS)
    @PostMapping("/save-as")
    public ResponseEntity<FiscalYearResponse> saveAs(@Valid @RequestBody FiscalYearSaveAsRequest req){
    	FiscalYearResponse items = fiscalYearService.saveAs(req);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.FISCAL_YEAR_FULL_ACCESS)
    @PostMapping("/save-all")
    public ResponseEntity<List<FiscalYearResponse>> saveAll(@Valid @RequestBody List<FiscalYearRequest> request){
    	List<FiscalYearResponse> items = fiscalYearService.saveAll(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.FISCAL_YEAR_FULL_ACCESS)
    @PostMapping("/general-search")
    public ResponseEntity<List<FiscalYearResponse>> generalSearch(@RequestBody FiscalYearSearchRequest req){
    	List<FiscalYearResponse> items = fiscalYearService.generalSearch(req);
    	return ResponseEntity.ok(items);
    }
}
