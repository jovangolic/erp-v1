package com.jovan.erp_v1.controller;

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

import com.jovan.erp_v1.enumeration.SeverityLevel;
import com.jovan.erp_v1.request.DefectRequest;
import com.jovan.erp_v1.response.DefectResponse;
import com.jovan.erp_v1.service.IDefectService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/defects")
public class DefectController {

	private final IDefectService defectService;
	
	@PostMapping("/create/new-defect")
	public ResponseEntity<DefectResponse> create(@Valid @RequestBody DefectRequest request){
		DefectResponse item = defectService.create(request);
		return ResponseEntity.ok(item);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<DefectResponse> update(@PathVariable Long id, @Valid @RequestBody DefectRequest request){
		DefectResponse item = defectService.update(id, request);
		return ResponseEntity.ok(item);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		defectService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/find-one/{id}")
	public ResponseEntity<DefectResponse> findOne(@PathVariable Long id){
		DefectResponse item = defectService.findOne(id);
		return ResponseEntity.ok(item);
	}
	
	@GetMapping("/find-all")
	public ResponseEntity<List<DefectResponse>> findAll(){
		List<DefectResponse> items = defectService.findAll();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-code")
	public ResponseEntity<List<DefectResponse>> findByCodeContainingIgnoreCase(@RequestParam("code") String code){
		List<DefectResponse> items = defectService.findByCodeContainingIgnoreCase(code);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-name")
	public ResponseEntity<List<DefectResponse>> findByNameContainingIgnoreCase(@RequestParam("name") String name){
		List<DefectResponse> items = defectService.findByNameContainingIgnoreCase(name);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-description")
	public ResponseEntity<List<DefectResponse>> findByDescriptionContainingIgnoreCase(@RequestParam("description") String description){
		List<DefectResponse> items = defectService.findByDescriptionContainingIgnoreCase(description);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-code-and-name")
	public ResponseEntity<List<DefectResponse>> findByCodeContainingIgnoreCaseAndNameContainingIgnoreCase(@RequestParam("code") String code,@RequestParam("name")  String name){
		List<DefectResponse> items = defectService.findByCodeContainingIgnoreCaseAndNameContainingIgnoreCase(code, name);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-severity")
	public ResponseEntity<List<DefectResponse>> findBySeverity(@RequestParam("severity") SeverityLevel severity){
		List<DefectResponse> items = defectService.findBySeverity(severity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-code-and-severity")
	public ResponseEntity<List<DefectResponse>> findByCodeContainingIgnoreCaseAndSeverity(@RequestParam("code") String code,@RequestParam("severity")  SeverityLevel severity){
		List<DefectResponse> items = defectService.findByCodeContainingIgnoreCaseAndSeverity(code, severity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-name-and-severity")
	public ResponseEntity<List<DefectResponse>> findByNameContainingIgnoreCaseAndSeverity(@RequestParam("name") String name,@RequestParam("severity")  SeverityLevel severity){
		List<DefectResponse> items = defectService.findByNameContainingIgnoreCaseAndSeverity(name, severity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/orders-by-severity-asc")
	public ResponseEntity<List<DefectResponse>> findAllByOrderBySeverityAsc(){
		List<DefectResponse> items = defectService.findAllByOrderBySeverityAsc();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/orders-by-severity-desc")
	public ResponseEntity<List<DefectResponse>> findAllByOrderBySeverityDesc(){
		List<DefectResponse> items = defectService.findAllByOrderBySeverityDesc();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-severity-and-desc-part")
	public ResponseEntity<List<DefectResponse>> findBySeverityAndDescriptionContainingIgnoreCase(@RequestParam("severity") SeverityLevel severity,@RequestParam("descPart")  String descPart){
		List<DefectResponse> items = defectService.findBySeverityAndDescriptionContainingIgnoreCase(severity, descPart);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/count-by-severity")
	public ResponseEntity<Long> countBySeverity(@RequestParam("severity") SeverityLevel severity){
		Long items = defectService.countBySeverity(severity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/count-by-code")
	public ResponseEntity<Long> countByCode(@RequestParam("code") String code){
		Long items = defectService.countByCode(code);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/count-by-name")
	public ResponseEntity<Long> countByName(@RequestParam("name") String name){
		Long items = defectService.countByName(name);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-severity-in-levels")
	public ResponseEntity<List<DefectResponse>> findBySeverityIn(@RequestParam("levels") List<SeverityLevel> levels){
		List<DefectResponse> items = defectService.findBySeverityIn(levels);
		return ResponseEntity.ok(items);
	}
	
}
