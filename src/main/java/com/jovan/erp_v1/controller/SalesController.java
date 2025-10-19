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
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sales")
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.SALES_READ_ACCESS)
public class SalesController {

	private final ISalesService salesService;

	@PreAuthorize(RoleGroups.SALES_FULL_ACCESS)
	@PostMapping("/create/new-sale")
	public ResponseEntity<SalesResponse> createSales(@Valid @RequestBody SalesRequest request) {
		SalesResponse response = salesService.createSales(request);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize(RoleGroups.SALES_FULL_ACCESS)
	@PutMapping("update/{id}")
	public ResponseEntity<SalesResponse> updateSales(@PathVariable Long id, @Valid @RequestBody SalesRequest request) {
		SalesResponse updated = salesService.updateSales(id, request);
		return ResponseEntity.ok(updated);
	}

	@PreAuthorize(RoleGroups.SALES_FULL_ACCESS)
	@DeleteMapping("/delete/sale/{id}")
	public ResponseEntity<Void> deleteSales(@PathVariable Long id) {
		salesService.deleteSales(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize(RoleGroups.SALES_READ_ACCESS)
	@GetMapping("/between-dates")
	public ResponseEntity<List<SalesResponse>> getByCreatedAtBetween(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
		List<SalesResponse> responses = salesService.getByCreatedAtBetween(startDate, endDate);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.SALES_READ_ACCESS)
	@GetMapping("/total-price")
	public ResponseEntity<List<SalesResponse>> getByTotalPrice(@RequestParam("totalPrice") BigDecimal totalPrice) {
		List<SalesResponse> responses = salesService.getByTotalPrice(totalPrice);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.SALES_READ_ACCESS)
	@GetMapping("find-one/{salesId}")
	public ResponseEntity<SalesResponse> findBySalesId(@PathVariable Long salesId) {
		SalesResponse id = salesService.getBySalesId(salesId);
		return ResponseEntity.ok(id);
	}

	@PreAuthorize(RoleGroups.SALES_READ_ACCESS)
	@GetMapping("/sale-by-date")
	public ResponseEntity<List<SalesResponse>> getSalesByDate(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		List<SalesResponse> responses = salesService.getSalesByDate(date);
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.SALES_READ_ACCESS)
	@GetMapping("/get-all-sales")
	public ResponseEntity<List<SalesResponse>> getAllSales() {
		List<SalesResponse> responses = salesService.getAllSales();
		return ResponseEntity.ok(responses);
	}
	
	//nove metode
	@PreAuthorize(RoleGroups.SALES_READ_ACCESS)
	@GetMapping("/buyer/{buyerId}")
	public ResponseEntity<List<SalesResponse>> findByBuyer_Id(@PathVariable Long buyerId){
		List<SalesResponse> responses = salesService.findByBuyer_Id(buyerId);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SALES_READ_ACCESS)
	@GetMapping("/search/by-company-name")
	public ResponseEntity<List<SalesResponse>> findByBuyer_CompanyNameContainingIgnoreCase(@RequestParam("buyerCompanyName") String buyerCompanyName){
		List<SalesResponse> responses = salesService.findByBuyer_CompanyNameContainingIgnoreCase(buyerCompanyName);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SALES_READ_ACCESS)
	@GetMapping("/search/by-pib")
	public ResponseEntity<List<SalesResponse>> findByBuyer_PibContainingIgnoreCase(@RequestParam("buyerPib") String buyerPib){
		List<SalesResponse> responses = salesService.findByBuyer_PibContainingIgnoreCase(buyerPib);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SALES_READ_ACCESS)
	@GetMapping("/search/by-address")
	public ResponseEntity<List<SalesResponse>> findByBuyer_AddressContainingIgnoreCase(@RequestParam("buyerAddress") String buyerAddress){
		List<SalesResponse> responses = salesService.findByBuyer_AddressContainingIgnoreCase(buyerAddress);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SALES_READ_ACCESS)
	@GetMapping("/search/by-contact-person")
	public ResponseEntity<List<SalesResponse>> findByBuyer_ContactPerson(@RequestParam("contactPerson") String contactPerson){
		List<SalesResponse> responses = salesService.findByBuyer_ContactPerson(contactPerson);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SALES_READ_ACCESS)
	@GetMapping("/search/by-email")
	public ResponseEntity<List<SalesResponse>> findByBuyer_EmailContainingIgnoreCase(@RequestParam("buyerEmail") String buyerEmail){
		List<SalesResponse> responses = salesService.findByBuyer_EmailContainingIgnoreCase(buyerEmail);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SALES_READ_ACCESS)
	@GetMapping("/search/by-phone-number")
	public ResponseEntity<List<SalesResponse>> findByBuyer_PhoneNumber(@RequestParam("buyerPhoneNumber") String buyerPhoneNumber){
		List<SalesResponse> responses = salesService.findByBuyer_PhoneNumber(buyerPhoneNumber);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SALES_READ_ACCESS)
	@GetMapping("/search/by-total-price-max")
	public ResponseEntity<List<SalesResponse>> findByTotalPriceGreaterThan(@RequestParam("totalPrice") BigDecimal totalPrice){
		List<SalesResponse> responses = salesService.findByTotalPriceGreaterThan(totalPrice);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SALES_READ_ACCESS)
	@GetMapping("/search/by-total-price-min")
	public ResponseEntity<List<SalesResponse>> findByTotalPriceLessThan(@RequestParam("totalPrice") BigDecimal totalPrice){
		List<SalesResponse> responses = salesService.findByTotalPriceLessThan(totalPrice);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.SALES_READ_ACCESS)
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
