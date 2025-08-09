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

import com.jovan.erp_v1.enumeration.TaxType;
import com.jovan.erp_v1.request.TaxRateRequest;
import com.jovan.erp_v1.response.TaxRateResponse;
import com.jovan.erp_v1.service.ITaxRateService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/taxRates")
@PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
public class TaxRateController {

    private final ITaxRateService taxRateService;

    @PreAuthorize(RoleGroups.TAX_RATE_FULL_ACCESS)
    @PostMapping("/create/new-taxRate")
    public ResponseEntity<TaxRateResponse> create(@Valid @RequestBody TaxRateRequest request) {
        TaxRateResponse response = taxRateService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.TAX_RATE_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<TaxRateResponse> update(@PathVariable Long id, @Valid @RequestBody TaxRateRequest request) {
        TaxRateResponse response = taxRateService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.TAX_RATE_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taxRateService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<TaxRateResponse> findOne(@PathVariable Long id) {
        TaxRateResponse response = taxRateService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<TaxRateResponse>> findAll() {
        List<TaxRateResponse> responses = taxRateService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/by-type")
    public ResponseEntity<List<TaxRateResponse>> findByType(@RequestParam("type") TaxType type) {
        List<TaxRateResponse> responses = taxRateService.findByType(type);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/by-taxName")
    public ResponseEntity<List<TaxRateResponse>> findByTaxName(@RequestParam("taxName") String taxName) {
        List<TaxRateResponse> responses = taxRateService.findByTaxName(taxName);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/by-percentage")
    public ResponseEntity<List<TaxRateResponse>> findByPercentage(@RequestParam("percentage") BigDecimal percentage) {
        List<TaxRateResponse> responses = taxRateService.findByPercentage(percentage);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/taxName-percentage")
    public ResponseEntity<List<TaxRateResponse>> findByTaxNameAndPercentage(@RequestParam("taxName") String taxName,
            @RequestParam("percentage") BigDecimal percentage) {
        List<TaxRateResponse> responses = taxRateService.findByTaxNameAndPercentage(taxName, percentage);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/startDateBefore")
    public ResponseEntity<List<TaxRateResponse>> findByStartDateBeforeAndEndDateAfter(
            @RequestParam("date1") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date1,
            @RequestParam("date2") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date2) {
        List<TaxRateResponse> responses = taxRateService.findByStartDateBeforeAndEndDateAfter(date1, date2);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/startDate-lessThan")
    public ResponseEntity<List<TaxRateResponse>> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(
            @RequestParam("date1") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date1,
            @RequestParam("date2") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date2) {
        List<TaxRateResponse> responses = taxRateService.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(date1,
                date2);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/by-overlapping")
    public ResponseEntity<List<TaxRateResponse>> findOverlapping(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<TaxRateResponse> responses = taxRateService.findOverlapping(start, end);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/by-startDate")
    public ResponseEntity<List<TaxRateResponse>> findByStartDate(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start) {
        List<TaxRateResponse> responses = taxRateService.findByStartDate(start);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/by-endDate")
    public ResponseEntity<List<TaxRateResponse>> findByEndDate(
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TaxRateResponse> responses = taxRateService.findByEndDate(endDate);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/active-byType")
    public ResponseEntity<List<TaxRateResponse>> findActiveByType(@RequestParam("type") TaxType type,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TaxRateResponse> responses = taxRateService.findActiveByType(type, date);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/type-and-period")
    public ResponseEntity<List<TaxRateResponse>> findByTypeAndPeriod(@RequestParam("type") TaxType type,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TaxRateResponse> responses = taxRateService.findByTypeAndPeriod(type, startDate, endDate);
        return ResponseEntity.ok(responses);
    }
    
    //nove metode
    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/search/by-type-vat")
    public ResponseEntity<List<TaxRateResponse>> findByVat(){
    	List<TaxRateResponse> responses = taxRateService.findByVat();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/searh/by-type-income-tax")
    public ResponseEntity<List<TaxRateResponse>> findByIncome_Tax(){
    	List<TaxRateResponse> responses = taxRateService.findByIncome_Tax();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/search/by-type-sales-tax")
    public ResponseEntity<List<TaxRateResponse>> findBySales_Tax(){
    	List<TaxRateResponse> responses = taxRateService.findBySales_Tax();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TAX_RATE_READ_ACCESS)
    @GetMapping("/search/by-type-custom")
    public ResponseEntity<List<TaxRateResponse>> findByCustom(){
    	List<TaxRateResponse> responses = taxRateService.findByCustom();
    	return ResponseEntity.ok(responses);
    }
}
