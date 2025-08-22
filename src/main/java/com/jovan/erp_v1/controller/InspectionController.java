package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.dto.InspectionQuantityAcceptedDTO;
import com.jovan.erp_v1.dto.InspectionQuantityAcceptedSummaryDTO;
import com.jovan.erp_v1.dto.InspectionQuantityInspectedDTO;
import com.jovan.erp_v1.dto.InspectionQuantityInspectedSummaryDTO;
import com.jovan.erp_v1.dto.InspectionQuantityRejectedDTO;
import com.jovan.erp_v1.dto.InspectionQuantityRejectedSummaryDTO;
import com.jovan.erp_v1.request.InspectionRequest;
import com.jovan.erp_v1.response.InspectionResponse;
import com.jovan.erp_v1.service.InfInspectionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inspections")
public class InspectionController {

	private final InfInspectionService inspectionService;
	
	@PostMapping("/create/new-inspection")
	public ResponseEntity<InspectionResponse> create(@Valid @RequestBody InspectionRequest request){
		InspectionResponse items = inspectionService.create(request);
		return ResponseEntity.ok(items);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<InspectionResponse> update(@PathVariable Long id, @Valid @RequestBody InspectionRequest request){
		InspectionResponse items = inspectionService.update(id, request);
		return ResponseEntity.ok(items);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		inspectionService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/find-one/{id}")
	public ResponseEntity<InspectionResponse> findOne(@PathVariable Long id){
		InspectionResponse items = inspectionService.findOne(id);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/find-all")
	public ResponseEntity<List<InspectionResponse>> findAll(){
		List<InspectionResponse> items = inspectionService.findAll();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search-inspections")
	public ResponseEntity<List<InspectionResponse>> searchInspections(
			@RequestParam(required = false) String storageName, 
			@RequestParam(required = false) String storageLocation,
			@RequestParam(required = false) BigDecimal minCapacity, 
			@RequestParam(required = false) BigDecimal maxCapacity){
		List<InspectionResponse> items = inspectionService.searchInspections(storageName, storageLocation, minCapacity, maxCapacity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/quantity-inspected/{inspectionId}")
	public ResponseEntity<InspectionQuantityInspectedDTO> getQuantityInspected(@PathVariable Long inspectionId){
		InspectionQuantityInspectedDTO items = inspectionService.getQuantityInspected(inspectionId);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/quantity-accepted/{inspectionId}")
	public ResponseEntity<InspectionQuantityAcceptedDTO> getQuantityAccepted(@PathVariable Long inspectionId){
		InspectionQuantityAcceptedDTO items = inspectionService.getQuantityAccepted(inspectionId);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/quantity-rejected/{inspectionId}")
	public ResponseEntity<InspectionQuantityRejectedDTO> getQuantityRejected(@PathVariable Long inspectionId){
		InspectionQuantityRejectedDTO items = inspectionService.getQuantityRejected(inspectionId);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/quantity-inspected-summary")
	public ResponseEntity<InspectionQuantityInspectedSummaryDTO> getQuantityInspectedSummary(){
		InspectionQuantityInspectedSummaryDTO items = inspectionService.getQuantityInspectedSummary();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/quantity-accepted-summary")
	public ResponseEntity<InspectionQuantityAcceptedSummaryDTO> getQuantityAcceptedSummary(){
		InspectionQuantityAcceptedSummaryDTO items = inspectionService.getQuantityAcceptedSummary();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/quantity-rejected-summary")
	public ResponseEntity<InspectionQuantityRejectedSummaryDTO> getQuantityRejectedSummary(){
		InspectionQuantityRejectedSummaryDTO items = inspectionService.getQuantityRejectedSummary();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/exists/by-code")
	public ResponseEntity<Boolean> existsByCode(@RequestParam("code") String code){
		Boolean items = inspectionService.existsByCode(code);
		return ResponseEntity.ok(items);
	}
}
