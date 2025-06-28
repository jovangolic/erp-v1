package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
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

import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.mapper.SupplyMapper;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.SupplyRequest;
import com.jovan.erp_v1.response.SupplyResponse;
import com.jovan.erp_v1.service.ISupplyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supplies")
@CrossOrigin("http://localhost:5173")
public class SupplyController {

	private final ISupplyService supplyService;
	private final SupplyMapper supplyMapper;
	private final StorageRepository storageRepository;

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PostMapping("/create/new-supply")
	public ResponseEntity<SupplyResponse> createSupply(@Valid @RequestBody SupplyRequest request) {
		SupplyResponse response = supplyService.createSupply(request);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PutMapping("/update/{id}")
	public ResponseEntity<SupplyResponse> updateSupply(@PathVariable Long id,
			@Valid @RequestBody SupplyRequest request) {
		SupplyResponse response = supplyService.updateSupply(id, request);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteSupply(@PathVariable Long id) {
		supplyService.deleteSupply(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/supply/{supplyId}")
	public ResponseEntity<SupplyResponse> getBySupplyId(@PathVariable Long supplyId) {
		SupplyResponse response = supplyService.getBySupplyId(supplyId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-all-supplies")
	public ResponseEntity<List<SupplyResponse>> getAllSupplies() {
		List<SupplyResponse> responses = supplyService.getAllSupply();
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/supply/by-storageId")
	public ResponseEntity<List<SupplyResponse>> getByStorage(@RequestParam("storageId") Long storageId) {
		Storage storage = storageRepository.findById(storageId)
				.orElseThrow(() -> new StorageNotFoundException("Storage not found with id: " + storageId));
		List<SupplyResponse> responses = supplyService.getByStorage(storage);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/supply/by-goods-name")
	public ResponseEntity<List<SupplyResponse>> getBySuppliesByGoodsName(@RequestParam("name") String name) {
		List<SupplyResponse> responses = supplyService.getBySuppliesByGoodsName(name);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/supply/by-minQuantity")
	public ResponseEntity<List<SupplyResponse>> getBySuppliesWithMinQuantity(
			@RequestParam("minQuantity") BigDecimal minQuantity) {
		List<SupplyResponse> responses = supplyService.getBySuppliesWithMinQuantity(minQuantity);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/supply/storageId/{storageId}")
	public ResponseEntity<List<SupplyResponse>> getBySuppliesByStorageId(@PathVariable Long storageId) {
		List<SupplyResponse> responses = supplyService.getBySuppliesByStorageId(storageId);
		return ResponseEntity.ok(responses);
	}

}
