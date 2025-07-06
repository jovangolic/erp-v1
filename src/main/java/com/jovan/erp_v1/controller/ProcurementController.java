package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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

import com.jovan.erp_v1.exception.ProcurementNotFoundException;
import com.jovan.erp_v1.mapper.ProcurementMapper;
import com.jovan.erp_v1.request.ProcurementRequest;
import com.jovan.erp_v1.response.ProcurementResponse;
import com.jovan.erp_v1.service.IProcurementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/procurements")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class ProcurementController {

	
	private final IProcurementService procurementService;
	private final ProcurementMapper procurementMapper;
	
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PostMapping("/create/new-procurement")
	public ResponseEntity<ProcurementResponse> create(@Valid @RequestBody ProcurementRequest request){
		ProcurementResponse response = procurementService.createProcurement(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<ProcurementResponse> update(@PathVariable Long id, @Valid @RequestBody ProcurementRequest request){
		ProcurementResponse updated = procurementService.updateProcurement(id, request);
		return ResponseEntity.ok(updated);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteProcurement(@PathVariable Long id){
		procurementService.deleteProcurement(id);
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping("/procurement/{id}")
	public ResponseEntity<ProcurementResponse> getByProcurementId(@PathVariable Long id){
		ProcurementResponse response = procurementService.getByProcurementId(id);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all-procurement")
	public ResponseEntity<List<ProcurementResponse>> getAllProcurement(){
		List<ProcurementResponse> responses = procurementService.getAllProcurement();
		return ResponseEntity.ok(responses);		
	}
	
	@GetMapping("/procurement/total-cost")
	public ResponseEntity<List<ProcurementResponse>> getByTotalCost(@RequestParam("totalCost") BigDecimal totalCost){
		List<ProcurementResponse> responses = procurementService.getByTotalCost(totalCost);
		return ResponseEntity.ok(responses);
	}
	
	
	@GetMapping("/procurement/date-between")
	public ResponseEntity<List<ProcurementResponse>> getByDateBetween(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		List<ProcurementResponse> responses = procurementService.getByDateBetween(startDate, endDate);
		return ResponseEntity.ok(responses);
	}
	
	
	@GetMapping("/procurement/cost/min-max")
	public ResponseEntity<List<ProcurementResponse>> getByTotalCostBetween(@RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max){
		List<ProcurementResponse> responses = procurementService.getByTotalCostBetween(min, max);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/total-cost-greater-than")
	public ResponseEntity<List<ProcurementResponse>> getByTotalCostGreaterThan(@RequestParam("totalCost")  BigDecimal totalCost){
		List<ProcurementResponse> responses = procurementService.getByTotalCostGreaterThan(totalCost);
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/total-cost-less-than")
	public ResponseEntity<List<ProcurementResponse>> getByTotalCostLessThan(@RequestParam("totalCost") BigDecimal totalCost){
		List<ProcurementResponse> responses = procurementService.getByTotalCostLessThan(totalCost);
		return ResponseEntity.ok(responses);
	}
	
}
