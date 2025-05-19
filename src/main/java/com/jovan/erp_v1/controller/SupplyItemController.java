package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
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

import com.jovan.erp_v1.mapper.SupplyMapper;
import com.jovan.erp_v1.request.SupplyItemRequest;
import com.jovan.erp_v1.response.SupplyItemResponse;
import com.jovan.erp_v1.service.ISupplyItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suppliesItems")
@CrossOrigin("http://localhost:5173")
public class SupplyItemController {

	
	private final ISupplyItemService supplyItemService;
	private final SupplyMapper supplyMapper;
	
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PostMapping("/create/new-supply-item")
	public ResponseEntity<SupplyItemResponse> create(@Valid @RequestBody SupplyItemRequest request){
		SupplyItemResponse response = supplyItemService.createSupplyItem(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<SupplyItemResponse> update(@PathVariable Long id, @Valid @RequestBody SupplyItemRequest request){
		SupplyItemResponse response = supplyItemService.updateSupplyItem(id, request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		supplyItemService.deleteSupplyItem(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/supplyItem/{id}")
	public ResponseEntity<SupplyItemResponse> getOne(@PathVariable Long id){
		SupplyItemResponse response = supplyItemService.getOneById(id);
		return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/get-all-supplies-items")
	public ResponseEntity<List<SupplyItemResponse>> getAllSuppliesItems(){
		List<SupplyItemResponse> responses = supplyItemService.getAllSupplyItem();
		return ResponseEntity.ok(responses);
	}
	
	
	@GetMapping("/procurement/{procurementId}")
	public ResponseEntity<List<SupplyItemResponse>> getByProcurementId(@PathVariable Long procurementId){
		List<SupplyItemResponse> responses = supplyItemService.getByProcurementId(procurementId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/supplier/{supplierId}")
	public ResponseEntity<List<SupplyItemResponse>> getBySupplierId(@PathVariable Long supplierId){
		List<SupplyItemResponse> responses = supplyItemService.getBySupplierId(supplierId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/cost-between")
	public ResponseEntity<List<SupplyItemResponse>> getByCostBetween(@RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max){
		List<SupplyItemResponse> responses = supplyItemService
				.getByCostBetween(min, max);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/procurement-date-between")
	public ResponseEntity<List<SupplyItemResponse>> getByProcurementDateBetween(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		List<SupplyItemResponse> responses = supplyItemService.getByProcurementDateBetween(startDate, endDate);
		return ResponseEntity.ok(responses);
	}
	
	
	@GetMapping("/date-cost-between")
	public ResponseEntity<List<SupplyItemResponse>> getByProcurementDateAndCostBetween(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max){
		List<SupplyItemResponse> responses = supplyItemService.getByProcurementDateAndCostBetween(startDate, endDate, min, max);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/by-procurement-vendor/{procurementId}/{vendorId}")
	public ResponseEntity<List<SupplyItemResponse>> getByProcurementAndVendor(@PathVariable Long procurementId, @PathVariable Long vendorId){
		List<SupplyItemResponse> responses = supplyItemService.getByProcurementAndVendor(procurementId, vendorId);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/by-supplier-procurement-cost/{supplierId}/{procurementId}")
	public ResponseEntity<List<SupplyItemResponse>> getByVendorAndProcurementAndCost(
	        @PathVariable Long supplierId,
	        @PathVariable Long procurementId,
	        @RequestParam("minCost") BigDecimal minCost){
		List<SupplyItemResponse> responses = supplyItemService.getByVendorAndProcurementAndCost(supplierId, procurementId, minCost);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/date-cost")
	public ResponseEntity<List<SupplyItemResponse>> getByDateAndCost(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max
			){
		List<SupplyItemResponse> responses = supplyItemService.getByDateAndCost(startDate, endDate, min, max);
		return ResponseEntity.ok(responses);
	}
	
	
	@GetMapping("/filter-by-supplier-date-cost")
	public ResponseEntity<List<SupplyItemResponse>> getBySupplierNameAndProcurementDateAndMaxCost(
			@RequestParam("supplierName") String supplierName, 
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam("max") BigDecimal max
			){
		List<SupplyItemResponse> responses = supplyItemService.getBySupplierNameAndProcurementDateAndMaxCost(supplierName, startDate, endDate, max);
		return ResponseEntity.ok(responses);
	}
		
}

