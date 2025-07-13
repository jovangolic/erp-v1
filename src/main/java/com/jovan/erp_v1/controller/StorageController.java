package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.mapper.StorageMapper;
import com.jovan.erp_v1.request.StorageRequest;
import com.jovan.erp_v1.response.StorageResponse;
import com.jovan.erp_v1.service.IStorageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/storages")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class StorageController {

	private final IStorageService storageService;
	private final StorageMapper storageMapper;

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PostMapping("/create/new-storage")
	public ResponseEntity<StorageResponse> createStorage(@Valid @RequestBody StorageRequest request) {
		StorageResponse response = storageService.createStorage(request);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PutMapping("/update/{storageId}")
	public ResponseEntity<StorageResponse> updateStorage(@PathVariable Long storageId,
			@Valid @RequestBody StorageRequest request) {
		StorageResponse updated = storageService.updateStorage(storageId, request);
		return ResponseEntity.ok(updated);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@DeleteMapping("/delete/{storageId}")
	public ResponseEntity<Void> deleteStorage(@PathVariable Long storageId) {
		storageService.deleteStorage(storageId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/get-by-storage-type")
	public ResponseEntity<List<StorageResponse>> getByStorageType(@RequestParam("storageType") StorageType type) {
		List<StorageResponse> responses = storageService.getByStorageType(type);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/{storageId}")
	public ResponseEntity<StorageResponse> getByStorageId(@PathVariable Long storageId) {
		StorageResponse response = storageService.getByStorageId(storageId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/storage/by-name")
	public ResponseEntity<List<StorageResponse>> getByStorageName(@RequestParam("name") String name) {
		List<StorageResponse> responses = storageService.getByName(name);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/by-location")
	public ResponseEntity<List<StorageResponse>> getByStorageLocation(@RequestParam("location") String location) {
		List<StorageResponse> responses = storageService.getByLocation(location);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/by-capacity")
	public ResponseEntity<List<StorageResponse>> getByStorageCapacity(@RequestParam("capacity") BigDecimal capacity) {
		List<StorageResponse> responses = storageService.getByCapacity(capacity);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/by-name-and-location")
	public ResponseEntity<List<StorageResponse>> getStorageByNameAndLocation(@RequestParam("name") String name,
			@RequestParam("location") String location) {
		List<StorageResponse> responses = storageService.getByNameAndLocation(name, location);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/by-type-and-capacity")
	public ResponseEntity<List<StorageResponse>> getByTypeAndCapacityGreaterThan(@RequestParam("type") StorageType type,
			@RequestParam("capacity") BigDecimal capacity) {
		List<StorageResponse> responses = storageService.getByTypeAndCapacityGreaterThan(type, capacity);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/by-minCount")
	public ResponseEntity<List<StorageResponse>> getStoragesWithMinGoods(@RequestParam("minCount") Integer minCount) {
		List<StorageResponse> responses = storageService.getStoragesWithMinGoods(minCount);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/ignore-case")
	public ResponseEntity<List<StorageResponse>> getByNameContainingIgnoreCase(@RequestParam("name") String name) {
		List<StorageResponse> responses = storageService.getByNameContainingIgnoreCase(name);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/get-all-storages")
	public ResponseEntity<List<StorageResponse>> getAllStorage() {
		List<StorageResponse> responses = storageService.getAllStorage();
		return ResponseEntity.ok(responses);
	}

}
