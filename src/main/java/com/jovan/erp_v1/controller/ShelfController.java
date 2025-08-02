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

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
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
	
	//nove metode
	
	@GetMapping("/storage-name")
    public ResponseEntity<List<ShelfResponse>> findByStorage_Name(@RequestParam("name") String name){
    	List<ShelfResponse> responses = shelfService.findByStorage_NameContainingIgnoreCase(name);
    	return ResponseEntity.ok(responses);
    }
    
	@GetMapping("/storage-location")
	public ResponseEntity<List<ShelfResponse>> findByStorage_Location(@RequestParam("location") String location){
		List<ShelfResponse> responses = shelfService.findByStorage_LocationContainingIgnoreCase(location);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/storage-type")
	public ResponseEntity<List<ShelfResponse>> findByStorage_Type(@RequestParam("type") StorageType type){
		List<ShelfResponse> responses = shelfService.findByStorage_Type(type);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/storage-capacity-greater-than")
	public ResponseEntity<List<ShelfResponse>> findByStorage_CapacityGreaterThan(@RequestParam("capacity") BigDecimal capacity){
		List<ShelfResponse> responses = shelfService.findByStorage_CapacityGreaterThan(capacity);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/storage-name-and-type")
	public ResponseEntity<List<ShelfResponse>> findByStorage_NameAndStorage_Type(@RequestParam("name") String name,@RequestParam("type") StorageType type){
		List<ShelfResponse> responses = shelfService.findByStorage_NameContainingIgnoreCaseAndStorage_Type(name, type);
		  return ResponseEntity.ok(responses);
	}
	
	//addendum
	
	@GetMapping("/{id}/available-capacity")
    public ResponseEntity<BigDecimal> getAvailableCapacity(@PathVariable Long id) {
        BigDecimal capacity = shelfService.countAvailableCapacity(id);
        return ResponseEntity.ok(capacity);
    }

    @PostMapping("/{id}/allocate")
    public ResponseEntity<Void> allocateCapacity(@PathVariable Long id, @RequestBody BigDecimal amount) {
    	shelfService.allocateCapacity(id, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<Void> releaseCapacity(@PathVariable Long id, @RequestBody BigDecimal amount) {
    	shelfService.releaseCapacity(id, amount);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/search/storage-capacity-less-than")
    public ResponseEntity<List<ShelfResponse>> findByStorage_CapacityLessThan(@RequestParam("capacity") BigDecimal capacity){
    	List<ShelfResponse> responses = shelfService.findByStorage_CapacityLessThan(capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/storage-capacity")
    public ResponseEntity<List<ShelfResponse>> findByStorage_Capacity(@RequestParam("capacity") BigDecimal capacity){
    	List<ShelfResponse> responses = shelfService.findByStorage_Capacity(capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/storage-status")
    public ResponseEntity<List<ShelfResponse>> findByStorage_Status(@RequestParam("status") StorageStatus status){
    	List<ShelfResponse> responses = shelfService.findByStorage_Status(status);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/storage-type-and-status")
    public ResponseEntity<List<ShelfResponse>> findByStorageTypeAndStatus(@RequestParam("type") StorageType type,@RequestParam("status") StorageStatus status){
    	List<ShelfResponse> responses = shelfService.findByStorageTypeAndStatus(type, status);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/storage-name-and-location")
    public ResponseEntity<List<ShelfResponse>> findByStorageNameContainingIgnoreCaseAndLocationContainingIgnoreCase(@RequestParam("name") String name,@RequestParam("location") String location){
    	List<ShelfResponse> responses = shelfService.findByStorageNameContainingIgnoreCaseAndLocationContainingIgnoreCase(name, location);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/storage-name-and-capacity")
    public ResponseEntity<List<ShelfResponse>> findByStorageNameContainingIgnoreCaseAndCapacity(@RequestParam("name") String name,@RequestParam("capacity") BigDecimal capacity){
    	List<ShelfResponse> responses = shelfService.findByStorageNameContainingIgnoreCaseAndCapacity(name, capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/storage-name-and-capacity-greater-than")
    public ResponseEntity<List<ShelfResponse>> findByStorageNameContainingIgnoreCaseAndCapacityGreaterThan(@RequestParam("name") String name,@RequestParam("capacity") BigDecimal capacity){
    	List<ShelfResponse> responses = shelfService.findByStorageNameContainingIgnoreCaseAndCapacityGreaterThan(name, capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/storage-name-and-capacity-less-than")
    public ResponseEntity<List<ShelfResponse>> findByStorageNameContainingIgnoreCaseAndCapacityLessThan(@RequestParam("name") String name,@RequestParam("capacity") BigDecimal capacity){
    	List<ShelfResponse> responses = shelfService.findByStorageNameContainingIgnoreCaseAndCapacityLessThan(name, capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/storage-name-and-status")
    public ResponseEntity<List<ShelfResponse>> findByStorageNameContainingIgnoreCaseAndStatus(@RequestParam("name") String name,@RequestParam("status") StorageStatus status){
    	List<ShelfResponse> responses = shelfService.findByStorageNameContainingIgnoreCaseAndStatus(name, status);
    	return ResponseEntity.ok(responses);	
    }
    
    @GetMapping("/search/storage-capacity-between")
    public ResponseEntity<List<ShelfResponse>> findByStorageCapacityBetween(@RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max){
    	List<ShelfResponse> responses = shelfService.findByStorageCapacityBetween(min, max);
    	return ResponseEntity.ok(responses);
    }
}
