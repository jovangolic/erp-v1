package com.jovan.erp_v1.controller;

import java.util.List;

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

import com.jovan.erp_v1.repository.BuyerRepository;
import com.jovan.erp_v1.request.BuyerRequest;
import com.jovan.erp_v1.response.BuyerResponse;
import com.jovan.erp_v1.service.IBuyerService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/buyers")
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.BUYER_READ_ACCESS)
public class BuyerController {

	private final BuyerRepository buyerRepository;
	private final IBuyerService buyerService;

	@PreAuthorize(RoleGroups.BUYER_FULL_ACCESS)
	@PostMapping("/create/new-buyer")
	public ResponseEntity<BuyerResponse> createBuyer(@Valid @RequestBody BuyerRequest request) {
		BuyerResponse response = buyerService.createBuyer(request);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize(RoleGroups.BUYER_FULL_ACCESS)
	@PutMapping("/update/{pib}")
	public ResponseEntity<BuyerResponse> updateBuyer(@PathVariable String pib,
			@Valid @RequestBody BuyerRequest request) {
		BuyerResponse updated = buyerService.updateBuyer(pib, request);
		return ResponseEntity.ok(updated);
	}

	@PreAuthorize(RoleGroups.BUYER_FULL_ACCESS)
	@GetMapping("/buyer/{id}")
	public ResponseEntity<BuyerResponse> getBuyerById(@PathVariable Long id) {
		BuyerResponse response = buyerService.getBuyerById(id);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize(RoleGroups.BUYER_READ_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteBuyer(@PathVariable Long id) {
		buyerService.deleteBuyer(id);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize(RoleGroups.BUYER_READ_ACCESS)
	@GetMapping("/get-all")
	public ResponseEntity<List<BuyerResponse>> getAllBuyers() {
		List<BuyerResponse> responses = buyerService.getAllBuyers();
		return ResponseEntity.ok(responses);
	}

	@PreAuthorize(RoleGroups.BUYER_READ_ACCESS)
	@GetMapping("exists-by-pib")
	public ResponseEntity<Boolean> existsByPib(@RequestParam("pib") String pib) {
		boolean exists = buyerRepository.existsByPib(pib);
		return ResponseEntity.ok(exists);
	}

	@PreAuthorize(RoleGroups.BUYER_READ_ACCESS)
	@GetMapping("/search")
	public ResponseEntity<List<BuyerResponse>> searchBuyer(@RequestParam String keyword) {
		List<BuyerResponse> results = buyerService.searchBuyer(keyword);
		return ResponseEntity.ok(results);
	}

	@PreAuthorize(RoleGroups.BUYER_READ_ACCESS)
	@GetMapping("/by-pib/{pib}")
	public ResponseEntity<BuyerResponse> getBuyerByPid(@PathVariable String pib){
		BuyerResponse response = buyerService.getBuyerByPid(pib);
		return ResponseEntity.ok(response);
	}
	
	//nove metode
	@PreAuthorize(RoleGroups.BUYER_READ_ACCESS)
	@GetMapping("/by-address")
	public ResponseEntity<List<BuyerResponse>> findByAddressContainingIgnoreCase(@RequestParam("address") String address){
		List<BuyerResponse> results = buyerService.findByAddressContainingIgnoreCase(address);
		return ResponseEntity.ok(results);
	}
	
	@PreAuthorize(RoleGroups.BUYER_READ_ACCESS)
	@GetMapping("/by-contact-person")
	public ResponseEntity<List<BuyerResponse>> findByContactPerson(@RequestParam("contactPerson") String contactPerson){
		List<BuyerResponse> results = buyerService.findByContactPerson(contactPerson);
		return ResponseEntity.ok(results);
	}
	
	@PreAuthorize(RoleGroups.BUYER_READ_ACCESS)
	@GetMapping("/search/by-contact-person-fragment")
	public ResponseEntity<List<BuyerResponse>> findByContactPersonContainingIgnoreCase(@RequestParam("contactPersonFragment") String contactPersonFragment){
		List<BuyerResponse> results = buyerService.findByContactPersonContainingIgnoreCase(contactPersonFragment);
		return ResponseEntity.ok(results);
	}
	
	@PreAuthorize(RoleGroups.BUYER_READ_ACCESS)
	@GetMapping("/search/by-phone-fragment")
	public ResponseEntity<List<BuyerResponse>> findByPhoneNumberContaining(@RequestParam("phoneFragment") String phoneFragment){
		List<BuyerResponse> results = buyerService.findByPhoneNumberContaining(phoneFragment);
		return ResponseEntity.ok(results);
	}
	
	@PreAuthorize(RoleGroups.BUYER_READ_ACCESS)
	@GetMapping("/search/company-name-and-address")
	public ResponseEntity<List<BuyerResponse>> findByCompanyNameContainingIgnoreCaseAndAddressContainingIgnoreCase(@RequestParam("companyName") String companyName,
			@RequestParam("address") String address){
		List<BuyerResponse> results = buyerService.findByCompanyNameContainingIgnoreCaseAndAddressContainingIgnoreCase(companyName, address);
		return ResponseEntity.ok(results);
	}
	
	@PreAuthorize(RoleGroups.BUYER_READ_ACCESS)
	@GetMapping("/search/buyer-with-sales-orders")
	public ResponseEntity<List<BuyerResponse>> findBuyersWithSalesOrders(){
		List<BuyerResponse> results = buyerService.findBuyersWithSalesOrders();
		return ResponseEntity.ok(results);
	}
	
	@PreAuthorize(RoleGroups.BUYER_READ_ACCESS)
	@GetMapping("/search/buyer-without-sales-orders")
	public ResponseEntity<List<BuyerResponse>> findBuyersWithoutSalesOrders(){
		List<BuyerResponse> results = buyerService.findBuyersWithoutSalesOrders();
		return ResponseEntity.ok(results);
	}
	
	@PreAuthorize(RoleGroups.BUYER_READ_ACCESS)
	@GetMapping("/exists-by-email")
	public ResponseEntity<Boolean> existsByEmail(@RequestParam("email") String email){
		Boolean results = buyerService.existsByEmail(email);
		return ResponseEntity.ok(results);
	}
	
	@PreAuthorize(RoleGroups.BUYER_READ_ACCESS)
	@GetMapping("/search-buyers")
	public ResponseEntity<List<BuyerResponse>> searchBuyers(@RequestParam("companyName") String companyName,@RequestParam("email") String email){
		List<BuyerResponse> results = buyerService.searchBuyers(companyName, email);
		return ResponseEntity.ok(results);
	}
	
}
