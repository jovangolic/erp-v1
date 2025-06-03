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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.exception.BuyerNotFoundException;
import com.jovan.erp_v1.exception.SalesNotFoundException;
import com.jovan.erp_v1.mapper.SalesMapper;
import com.jovan.erp_v1.model.Buyer;
import com.jovan.erp_v1.repository.BuyerRepository;
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
	private final SalesMapper salesMapper;
	private final BuyerRepository buyerRepository;

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

	@GetMapping("/get-by-buyer")
	public ResponseEntity<List<SalesResponse>> getSalesByBuyer(@RequestParam("buyerId") Long buyerId) {
		Buyer buyer = buyerRepository.findById(buyerId)
				.orElseThrow(() -> new BuyerNotFoundException("Buyer not found with id: " + buyerId));
		List<SalesResponse> responses = salesService.getByBuyer(buyer);
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

}
