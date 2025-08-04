package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jovan.erp_v1.request.SalesRequest;
import com.jovan.erp_v1.response.SalesResponse;
import com.jovan.erp_v1.service.ISalesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sales")
@CrossOrigin("http://localhost:5173")
public class SalesController {

	private final ISalesService salesService;

	@PostMapping("/create/new-sale")
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	public ResponseEntity<SalesResponse> createSales(@Valid @RequestBody SalesRequest request) {
		SalesResponse response = salesService.createSales(request);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PutMapping("update/{id}")
	public ResponseEntity<SalesResponse> updateSales(@PathVariable Long id, @Valid @RequestBody SalesRequest request) {
		SalesResponse updated = salesService.updateSales(id, request);
		return ResponseEntity.ok(updated);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@DeleteMapping("/delete/sale/{id}")
	public ResponseEntity<Void> deleteSales(@PathVariable Long id) {
		salesService.deleteSales(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/between-dates")
	public ResponseEntity<List<SalesResponse>> getByCreatedAtBetween(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
		List<SalesResponse> responses = salesService.getByCreatedAtBetween(startDate, endDate);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/sale/total-price")
	public ResponseEntity<List<SalesResponse>> getByTotalPrice(@RequestParam("totalPrice") BigDecimal totalPrice) {
		List<SalesResponse> responses = salesService.getByTotalPrice(totalPrice);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("sale/{salesId}")
	public ResponseEntity<SalesResponse> findBySalesId(@PathVariable Long salesId) {
		SalesResponse id = salesService.getBySalesId(salesId);
		return ResponseEntity.ok(id);
	}

	@GetMapping("/sale-by-date")
	public ResponseEntity<List<SalesResponse>> getSalesByDate(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		List<SalesResponse> responses = salesService.getSalesByDate(date);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/get-all-sales")
	public ResponseEntity<List<SalesResponse>> getAllSales() {
		List<SalesResponse> responses = salesService.getAllSales();
		return ResponseEntity.ok(responses);
	}
	
	//nove metode

	@GetMapping("/buyer/{buyerId}")
	public ResponseEntity<List<SalesResponse>> findByBuyer_Id(Long buyerId){
		List<SalesResponse> responses = salesService.findByBuyer_Id(buyerId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/by-company-name")
	public ResponseEntity<List<SalesResponse>> findByBuyer_CompanyNameContainingIgnoreCase(String buyerCompanyName){
		List<SalesResponse> responses = salesService.findByBuyer_CompanyNameContainingIgnoreCase(buyerCompanyName);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/by-pib")
	public ResponseEntity<List<SalesResponse>> findByBuyer_PibContainingIgnoreCase(String buyerPib){
		List<SalesResponse> responses = salesService.findByBuyer_PibContainingIgnoreCase(buyerPib);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/by-address")
	public ResponseEntity<List<SalesResponse>> findByBuyer_AddressContainingIgnoreCase(String buyerAddress){
		List<SalesResponse> responses = salesService.findByBuyer_AddressContainingIgnoreCase(buyerAddress);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/by-contact-person")
	public ResponseEntity<List<SalesResponse>> findByBuyer_ContactPerson(String contactPerson){
		List<SalesResponse> responses = salesService.findByBuyer_ContactPerson(contactPerson);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/by-email")
	public ResponseEntity<List<SalesResponse>> findByBuyer_EmailContainingIgnoreCase(String buyerEmail){
		List<SalesResponse> responses = salesService.findByBuyer_EmailContainingIgnoreCase(buyerEmail);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/by-phone-number")
	public ResponseEntity<List<SalesResponse>> findByBuyer_PhoneNumber(String buyerPhoneNumber){
		List<SalesResponse> responses = salesService.findByBuyer_PhoneNumber(buyerPhoneNumber);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/by-total-price-max")
	public ResponseEntity<List<SalesResponse>> findByTotalPriceGreaterThan(BigDecimal totalPrice){
		List<SalesResponse> responses = salesService.findByTotalPriceGreaterThan(totalPrice);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/by-total-price-min")
	public ResponseEntity<List<SalesResponse>> findByTotalPriceLessThan(BigDecimal totalPrice){
		List<SalesResponse> responses = salesService.findByTotalPriceLessThan(totalPrice);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<SalesResponse>> searchSales(
	    @RequestParam(required = false) Long buyerId,
	    @RequestParam(required = false) String companyName,
	    @RequestParam(required = false) String pib,
	    @RequestParam(required = false) String email,
	    @RequestParam(required = false) String phoneNumber,
	    @RequestParam(required = false) String address,
	    @RequestParam(required = false) String contactPerson,
	    @RequestParam(required = false) BigDecimal minTotalPrice,
	    @RequestParam(required = false) BigDecimal maxTotalPrice
	)
	{
		List<SalesResponse> responses = salesService.searchSales(buyerId, companyName, pib, email, phoneNumber, address, contactPerson, minTotalPrice, maxTotalPrice);
		return ResponseEntity.ok(responses);
	}
}
