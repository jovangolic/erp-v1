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
import com.jovan.erp_v1.request.ProcurementRequest;
import com.jovan.erp_v1.response.ProcurementResponse;
import com.jovan.erp_v1.service.IProcurementService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/procurements")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.PROCUREMENT_READ_ACCESS)
public class ProcurementController {

	private final IProcurementService procurementService;
	
	@PreAuthorize(RoleGroups.PROCUREMENT_FULL_ACCESS)
	@PostMapping("/create/new-procurement")
	public ResponseEntity<ProcurementResponse> create(@Valid @RequestBody ProcurementRequest request){
		ProcurementResponse response = procurementService.createProcurement(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.PROCUREMENT_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<ProcurementResponse> update(@PathVariable Long id, @Valid @RequestBody ProcurementRequest request){
		ProcurementResponse updated = procurementService.updateProcurement(id, request);
		return ResponseEntity.ok(updated);
	}
	
	@PreAuthorize(RoleGroups.PROCUREMENT_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteProcurement(@PathVariable Long id){
		procurementService.deleteProcurement(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize(RoleGroups.PROCUREMENT_READ_ACCESS)
	@GetMapping("/find-one/{id}")
	public ResponseEntity<ProcurementResponse> getByProcurementId(@PathVariable Long id){
		ProcurementResponse response = procurementService.getByProcurementId(id);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize(RoleGroups.PROCUREMENT_READ_ACCESS)
	@GetMapping("/find-all")
	public ResponseEntity<List<ProcurementResponse>> getAllProcurement(){
		List<ProcurementResponse> responses = procurementService.getAllProcurement();
		return ResponseEntity.ok(responses);		
	}
	
	@PreAuthorize(RoleGroups.PROCUREMENT_READ_ACCESS)
	@GetMapping("/total-cost")
	public ResponseEntity<List<ProcurementResponse>> getByTotalCost(@RequestParam("totalCost") BigDecimal totalCost){
		List<ProcurementResponse> responses = procurementService.getByTotalCost(totalCost);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PROCUREMENT_READ_ACCESS)
	@GetMapping("/date-between")
	public ResponseEntity<List<ProcurementResponse>> getByDateBetween(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		List<ProcurementResponse> responses = procurementService.getByDateBetween(startDate, endDate);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PROCUREMENT_READ_ACCESS)
	@GetMapping("/total-cost-range")
	public ResponseEntity<List<ProcurementResponse>> getByTotalCostBetween(@RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max){
		List<ProcurementResponse> responses = procurementService.getByTotalCostBetween(min, max);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PROCUREMENT_READ_ACCESS)
	@GetMapping("/total-cost-greater-than")
	public ResponseEntity<List<ProcurementResponse>> getByTotalCostGreaterThan(@RequestParam("totalCost")  BigDecimal totalCost){
		List<ProcurementResponse> responses = procurementService.getByTotalCostGreaterThan(totalCost);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PROCUREMENT_READ_ACCESS)
	@GetMapping("/total-cost-less-than")
	public ResponseEntity<List<ProcurementResponse>> getByTotalCostLessThan(@RequestParam("totalCost") BigDecimal totalCost){
		List<ProcurementResponse> responses = procurementService.getByTotalCostLessThan(totalCost);
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize(RoleGroups.PROCUREMENT_READ_ACCESS)
	@GetMapping("/payment-date")
	public ResponseEntity<List<ProcurementResponse>> findByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		List<ProcurementResponse> responses = procurementService.findByDate(date);
		return ResponseEntity.ok(responses);
	}
}
