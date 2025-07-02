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

import com.jovan.erp_v1.request.InventoryItemsRequest;
import com.jovan.erp_v1.response.InventoryItemsResponse;
import com.jovan.erp_v1.service.IInventoryItemsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventoryItems")
@CrossOrigin("http://localhost:5173")
public class InventoryItemsController {

	private final IInventoryItemsService inventoryItemsService;
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PostMapping("/create/new-inventory-items")
	public ResponseEntity<InventoryItemsResponse> create(@Valid @RequestBody InventoryItemsRequest request){
		InventoryItemsResponse response = inventoryItemsService.create(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@PutMapping("/update/{id}")
	public ResponseEntity<InventoryItemsResponse> update(@PathVariable Long id, @Valid @RequestBody InventoryItemsRequest request){
		InventoryItemsResponse response = inventoryItemsService.update(id, request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		inventoryItemsService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/get-one/{id}")
	public ResponseEntity<InventoryItemsResponse> findOneById(@PathVariable Long id){
		InventoryItemsResponse response = inventoryItemsService.findById(id);
		return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/get-all")
	public ResponseEntity<List<InventoryItemsResponse>> findAll(){
		List<InventoryItemsResponse> responses = inventoryItemsService.findAllInventories();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/by-quantity")
	public ResponseEntity<List<InventoryItemsResponse>> getByQuantity(@RequestParam("quantity") BigDecimal quantity){
		List<InventoryItemsResponse> responses = inventoryItemsService.getByQuantity(quantity);
		return ResponseEntity.ok(responses);
	}
	
	
	@GetMapping("/by-condition")
	public ResponseEntity<List<InventoryItemsResponse>> getByCondition(@RequestParam("condition") Integer itemCondition){
		List<InventoryItemsResponse> responses = inventoryItemsService.getByItemCondition(itemCondition);
		return ResponseEntity.ok(responses);
	}
	
	
	@GetMapping("/by-inventory/{inventoryId}")
	public ResponseEntity<List<InventoryItemsResponse>> getByInventoryId(@PathVariable Long inventoryId){
		List<InventoryItemsResponse> responses = inventoryItemsService.getByInventoryId(inventoryId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/by-product/{productId}")
	public ResponseEntity<List<InventoryItemsResponse>> getByProductId(@PathVariable Long productId){
		List<InventoryItemsResponse> responses = inventoryItemsService.getByProductId(productId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/by-product-name")
	public ResponseEntity<List<InventoryItemsResponse>> getByProductName(@RequestParam("productName") String productName){
		List<InventoryItemsResponse> responses = inventoryItemsService.getByProductName(productName);
		return ResponseEntity.ok(responses);
	}
	
	
	@GetMapping("/find-by-threshold")
	public ResponseEntity<List<InventoryItemsResponse>> findItemsWithDifference(@RequestParam("threshold") BigDecimal threshold){
		List<InventoryItemsResponse> responses = inventoryItemsService.findItemsWithDifference(threshold);
		return ResponseEntity.ok(responses);
	}
}
