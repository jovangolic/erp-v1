package com.jovan.erp_v1.controller;

import java.util.List;
import java.util.Optional;

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

import com.jovan.erp_v1.request.ShelfRequest;
import com.jovan.erp_v1.response.ShelfResponse;
import com.jovan.erp_v1.response.ShelfResponseWithGoods;
import com.jovan.erp_v1.service.IShelfService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shelves")
@CrossOrigin("http://localhost:5173")
public class ShelfController {

	private final IShelfService shelfService;

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PostMapping("/create/new-shelf")
	public ResponseEntity<ShelfResponse> createShelf(@Valid @RequestBody ShelfRequest request) {
		ShelfResponse response = shelfService.createShelf(request);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<ShelfResponse> updateShelf(@PathVariable Long id, @Valid @RequestBody ShelfRequest request) {
		ShelfResponse response = shelfService.updateShelf(id, request);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteShelf(@PathVariable Long id) {
		shelfService.deleteShelf(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/find-one/{id}")
	public ResponseEntity<ShelfResponse> findOne(@PathVariable Long id) {
		ShelfResponse response = shelfService.findOne(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/find-all")
	public ResponseEntity<List<ShelfResponse>> findAll() {
		List<ShelfResponse> responses = shelfService.findAll();
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/{storageId}/rowCount/{rowCount}/exists")
	public ResponseEntity<Boolean> existsByRowCountAndStorageId(@PathVariable Long storageId,
			@PathVariable Integer rowCount) {
		Boolean response = shelfService.existsByRowCountAndStorageId(rowCount, storageId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/storage/{storageId}/cols/{cols}/exists")
	public ResponseEntity<Boolean> existsByColsAndStorageId(@PathVariable Long storageId, @PathVariable Integer cols) {
		Boolean response = shelfService.existsByColsAndStorageId(cols, storageId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/storage/{storageId}/rowCount/{rowCount}/cols/{cols}/exists")
	public ResponseEntity<Boolean> existsByRowCountAndColumnAndStorageId(@PathVariable Long storageId,
			@PathVariable Integer rowCount, @PathVariable Integer cols) {
		Boolean response = shelfService.existsByRowCountAndColsAndStorageId(rowCount, cols, storageId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/by-storage/{storageId}")
	public ResponseEntity<List<ShelfResponse>> findByStorageId(@PathVariable Long storageId) {
		List<ShelfResponse> responses = shelfService.findByStorageId(storageId);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/{storageId}/rowCount/{rowCount}")
	public ResponseEntity<List<ShelfResponse>> findByRowCountAndStorageId(@PathVariable Long storageId,
			@PathVariable Integer rowCount) {
		List<ShelfResponse> responses = shelfService.findByRowCountAndStorageId(rowCount, storageId);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/{storageId}/cols/{cols}")
	public ResponseEntity<List<ShelfResponse>> findByColumnAndStorageId(@PathVariable Long storageId,
			@PathVariable Integer cols) {
		List<ShelfResponse> responses = shelfService.findByColsAndStorageId(cols, storageId);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/storage/{storageId}/rowCount/{rowCount}/cols/{cols}")
	public ResponseEntity<ShelfResponse> findByRowCountAndColsAndStorageId(@PathVariable Long storageId,
			@PathVariable Integer rowCount,
			@PathVariable Integer cols) {
		return shelfService.findByRowCountAndColsAndStorageId(rowCount, cols, storageId)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@GetMapping("/{shelfId}/with-goods")
	public ResponseEntity<ShelfResponseWithGoods> getShelfWithGoods(@PathVariable Long shelfId) {
		ShelfResponseWithGoods response = shelfService.getShelfWithGoods(shelfId);
		return ResponseEntity.ok(response);
	}

}
