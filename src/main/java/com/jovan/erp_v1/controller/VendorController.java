package com.jovan.erp_v1.controller;

import java.util.List;

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

import com.jovan.erp_v1.enumeration.MaterialTransactionStatus;
import com.jovan.erp_v1.exception.SupplierNotFoundException;
import com.jovan.erp_v1.mapper.VendorMapper;
import com.jovan.erp_v1.request.VendorRequest;
import com.jovan.erp_v1.response.VendorResponse;
import com.jovan.erp_v1.service.IVendorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/vendors")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5371")
public class VendorController {

	private final VendorMapper vendorMapper;
	private final IVendorService vendorService;

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PostMapping("/create/new-vendor")
	public ResponseEntity<VendorResponse> createVendor(@Valid @RequestBody VendorRequest request) {
		VendorResponse vendor = vendorService.createVendor(request);
		return ResponseEntity.ok(vendor);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PutMapping("/update/{id}")
	public ResponseEntity<VendorResponse> updateVendor(@PathVariable Long id,
			@Valid @RequestBody VendorRequest request) {
		VendorResponse updated = vendorService.updateVendor(id, request);
		return ResponseEntity.ok(updated);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteVendor(@PathVariable Long id) {
		vendorService.deleteVendor(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/vendor/by-name")
	public ResponseEntity<List<VendorResponse>> getVendorByName(@RequestParam("name") String name) {
		List<VendorResponse> responses = vendorService.findByName(name);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/vendor/by-address")
	public ResponseEntity<List<VendorResponse>> getVendorByAddress(@RequestParam("address") String address) {
		List<VendorResponse> responses = vendorService.findByAddress(address);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/vendor/by-email")
	public ResponseEntity<VendorResponse> getVendorByEmail(@RequestParam("email") String email) {
		VendorResponse items = vendorService.findByEmail(email);
		return ResponseEntity.ok(items);
	}

	@GetMapping("/get-one/{id}")
	public ResponseEntity<VendorResponse> getById(@PathVariable Long id) {
		VendorResponse response = vendorService.getById(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-all-vendors")
	public ResponseEntity<List<VendorResponse>> getAllVendors() {
		List<VendorResponse> responses = vendorService.getAllVendors();
		return ResponseEntity.ok(responses);
	}
	
	//nove metode
	
	@GetMapping("/by-phone-number")
	public ResponseEntity<List<VendorResponse>> findByPhoneNumberLikeIgnoreCase(@RequestParam("phoneNumber") String phoneNumber){
		List<VendorResponse> responses = vendorService.findByPhoneNumberLikeIgnoreCase(phoneNumber);
		return ResponseEntity.ok(responses);	
	}
	
	@GetMapping("/by-name-fragment")
	public ResponseEntity<List<VendorResponse>> searchByName(@RequestParam("nameFragment") String nameFragment){
		List<VendorResponse> responses = vendorService.searchByName(nameFragment);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/by-name-and-address")
	public ResponseEntity<List<VendorResponse>> findByNameIgnoreCaseContainingAndAddressIgnoreCaseContaining(@RequestParam("name") String name,@RequestParam("address") String address){
		List<VendorResponse> responses = vendorService.findByNameIgnoreCaseContainingAndAddressIgnoreCaseContaining(name, address);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/between-ids")
	public ResponseEntity<List<VendorResponse>> findByIdBetween(@RequestParam("startId") Long startId,@RequestParam("endId") Long endId){
		List<VendorResponse> responses = vendorService.findByIdBetween(startId, endId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/by-email-fragment")
	public ResponseEntity<List<VendorResponse>> findByEmailContainingIgnoreCase(@RequestParam("emailFragment") String emailFragment){
		List<VendorResponse> responses = vendorService.findByEmailContainingIgnoreCase(emailFragment);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/by-phone-number-fragment")
	public ResponseEntity<List<VendorResponse>> findByPhoneNumberContaining(@RequestParam("phoneNumberFragment") String phoneNumberFragment){
		List<VendorResponse> responses = vendorService.findByPhoneNumberContaining(phoneNumberFragment);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/by-material-transaction-status")
	public ResponseEntity<List<VendorResponse>> findVendorsByMaterialTransactionStatus(@RequestParam("status") MaterialTransactionStatus status){
		List<VendorResponse> responses = vendorService.findVendorsByMaterialTransactionStatus(status);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/coubnt-by-address-fragment")
	public ResponseEntity<Long> countByAddressContainingIgnoreCase(@RequestParam("addressFragment") String addressFragment){
		Long responses = vendorService.countByAddressContainingIgnoreCase(addressFragment);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/count-by-name-fragment")
	public ResponseEntity<Long> countByNameContainingIgnoreCase(@RequestParam("nameFragment") String nameFragment){
		Long responses = vendorService.countByNameContainingIgnoreCase(nameFragment);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/exists-by-email")
	public ResponseEntity<Boolean> existsByEmail(@RequestParam("email") String email){
		Boolean responses = vendorService.existsByEmail(email);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/by-name-ascending")
	public ResponseEntity<List<VendorResponse>> findAllByOrderByNameAsc(){
		List<VendorResponse> responses = vendorService.findAllByOrderByNameAsc();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search/by-name-descending")
	public ResponseEntity<List<VendorResponse>> findAllByOrderByNameDesc(){
		List<VendorResponse> responses = vendorService.findAllByOrderByNameDesc();
				return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/search-statuses")
	public ResponseEntity<List<VendorResponse>> findVendorsByTransactionStatuses(@RequestParam("statuses") List<MaterialTransactionStatus> statuses){
		List<VendorResponse> responses = vendorService.findVendorsByTransactionStatuses(statuses);
		return ResponseEntity.ok(responses);
	}

}
