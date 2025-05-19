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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/buyers")
@CrossOrigin("http://localhost:5173")
public class BuyerController {

	private final BuyerRepository buyerRepository;
	private final IBuyerService buyerService;
	
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PostMapping("/create/new-buyer")
	public ResponseEntity<BuyerResponse> createBuyer(@Valid @RequestBody BuyerRequest request){
		BuyerResponse response = buyerService.createBuyer(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PutMapping("/update/{pib}")
	public ResponseEntity<BuyerResponse> updateBuyer(@PathVariable String pib, @Valid @RequestBody BuyerRequest request){
		BuyerResponse updated = buyerService.updateBuyer(pib, request);
		return ResponseEntity.ok(updated);
	}
	
	@GetMapping("/buyer/{id}")
	public ResponseEntity<BuyerResponse> getBuyerById(@PathVariable Long id){
		BuyerResponse response = buyerService.getBuyerById(id);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteBuyer(@PathVariable Long id){
		buyerService.deleteBuyer(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<List<BuyerResponse>> getAllBuyers(){
		List<BuyerResponse> responses = buyerService.getAllBuyers();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("exists-by-pib")
	public ResponseEntity<Boolean> existsByPib(@RequestParam("pib") String pib){
		boolean exists = buyerRepository.existsByPib(pib);
		return ResponseEntity.ok(exists);
	}
	
}
