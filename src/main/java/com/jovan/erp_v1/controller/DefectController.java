package com.jovan.erp_v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.DefectStatus;
import com.jovan.erp_v1.enumeration.SeverityLevel;
import com.jovan.erp_v1.request.DefectRequest;
import com.jovan.erp_v1.response.DefectResponse;
import com.jovan.erp_v1.service.IDefectService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/defects")
@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
public class DefectController {

	private final IDefectService defectService;
	
	@PreAuthorize(RoleGroups.DEFECT_FULL_ACCESS)
	@PostMapping("/create/new-defect")
	public ResponseEntity<DefectResponse> create(@Valid @RequestBody DefectRequest request){
		DefectResponse item = defectService.create(request);
		return ResponseEntity.ok(item);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<DefectResponse> update(@PathVariable Long id, @Valid @RequestBody DefectRequest request){
		DefectResponse item = defectService.update(id, request);
		return ResponseEntity.ok(item);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		defectService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/find-one/{id}")
	public ResponseEntity<DefectResponse> findOne(@PathVariable Long id){
		DefectResponse item = defectService.findOne(id);
		return ResponseEntity.ok(item);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/find-all")
	public ResponseEntity<List<DefectResponse>> findAll(){
		List<DefectResponse> items = defectService.findAll();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/by-code")
	public ResponseEntity<List<DefectResponse>> findByCodeContainingIgnoreCase(@RequestParam("code") String code){
		List<DefectResponse> items = defectService.findByCodeContainingIgnoreCase(code);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/by-name")
	public ResponseEntity<List<DefectResponse>> findByNameContainingIgnoreCase(@RequestParam("name") String name){
		List<DefectResponse> items = defectService.findByNameContainingIgnoreCase(name);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/by-description")
	public ResponseEntity<List<DefectResponse>> findByDescriptionContainingIgnoreCase(@RequestParam("description") String description){
		List<DefectResponse> items = defectService.findByDescriptionContainingIgnoreCase(description);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/by-code-and-name")
	public ResponseEntity<List<DefectResponse>> findByCodeContainingIgnoreCaseAndNameContainingIgnoreCase(@RequestParam("code") String code,@RequestParam("name")  String name){
		List<DefectResponse> items = defectService.findByCodeContainingIgnoreCaseAndNameContainingIgnoreCase(code, name);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/by-severity")
	public ResponseEntity<List<DefectResponse>> findBySeverity(@RequestParam("severity") SeverityLevel severity){
		List<DefectResponse> items = defectService.findBySeverity(severity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/by-code-and-severity")
	public ResponseEntity<List<DefectResponse>> findByCodeContainingIgnoreCaseAndSeverity(@RequestParam("code") String code,@RequestParam("severity")  SeverityLevel severity){
		List<DefectResponse> items = defectService.findByCodeContainingIgnoreCaseAndSeverity(code, severity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/by-name-and-severity")
	public ResponseEntity<List<DefectResponse>> findByNameContainingIgnoreCaseAndSeverity(@RequestParam("name") String name,@RequestParam("severity")  SeverityLevel severity){
		List<DefectResponse> items = defectService.findByNameContainingIgnoreCaseAndSeverity(name, severity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/orders-by-severity-asc")
	public ResponseEntity<List<DefectResponse>> findAllByOrderBySeverityAsc(){
		List<DefectResponse> items = defectService.findAllByOrderBySeverityAsc();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/orders-by-severity-desc")
	public ResponseEntity<List<DefectResponse>> findAllByOrderBySeverityDesc(){
		List<DefectResponse> items = defectService.findAllByOrderBySeverityDesc();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/by-severity-and-desc-part")
	public ResponseEntity<List<DefectResponse>> findBySeverityAndDescriptionContainingIgnoreCase(@RequestParam("severity") SeverityLevel severity,@RequestParam("descPart")  String descPart){
		List<DefectResponse> items = defectService.findBySeverityAndDescriptionContainingIgnoreCase(severity, descPart);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/count-by-severity")
	public ResponseEntity<Long> countBySeverity(@RequestParam("severity") SeverityLevel severity){
		Long items = defectService.countBySeverity(severity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/count-by-code")
	public ResponseEntity<Long> countByCode(@RequestParam("code") String code){
		Long items = defectService.countByCode(code);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/count-by-name")
	public ResponseEntity<Long> countByName(@RequestParam("name") String name){
		Long items = defectService.countByName(name);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/by-severity-in-levels")
	public ResponseEntity<List<DefectResponse>> findBySeverityIn(@RequestParam("levels") List<SeverityLevel> levels){
		List<DefectResponse> items = defectService.findBySeverityIn(levels);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/exists/by-name")
	public ResponseEntity<Boolean> existsByNameContainingIgnoreCase(@RequestParam("name") String name){
		Boolean items = defectService.existsByNameContainingIgnoreCase(name);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/exists/by-code")
	public ResponseEntity<Boolean> existsByCodeContainingIgnoreCase(@RequestParam("code") String code){
		Boolean items = defectService.existsByCodeContainingIgnoreCase(code);
		return ResponseEntity.ok(items);
	}
	
	//genericka metoda
	@PreAuthorize(RoleGroups.DEFECT_FULL_ACCESS)
	@PostMapping("/{id}/status/{status}")
	public ResponseEntity<DefectResponse> changeStatus(
	        @PathVariable Long id,
	        @PathVariable DefectStatus status) {
	    return ResponseEntity.ok(defectService.changeStatus(id, status));
	}
	
	@PreAuthorize(RoleGroups.DEFECT_FULL_ACCESS)
	@PostMapping("/{id}/confirm")
    public ResponseEntity<DefectResponse> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(defectService.confirmDefect(id));
    }

	@PreAuthorize(RoleGroups.DEFECT_FULL_ACCESS)
    @PostMapping("/{id}/close")
    public ResponseEntity<DefectResponse> close(@PathVariable Long id) {
        return ResponseEntity.ok(defectService.closeDefect(id));
    }

	@PreAuthorize(RoleGroups.DEFECT_FULL_ACCESS)
    @PostMapping("/{id}/cancel")
    public ResponseEntity<DefectResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(defectService.cancelDefect(id));
    }
	
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS)
	@GetMapping("/search-defects")
	public ResponseEntity<List<DefectResponse>> searchDefects(@RequestParam("severity") SeverityLevel severity,@RequestParam("descPart") String descPart,
			@RequestParam("status") DefectStatus status,@RequestParam("confirmed") Boolean confirmed){
		List<DefectResponse> items = defectService.searchDefects(severity, descPart, status, confirmed);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/track/{id}")
	@PreAuthorize(RoleGroups.DEFECT_READ_ACCESS) // ili FULL_ACCESS po potrebi
	public ResponseEntity<DefectResponse> trackDefect(@PathVariable Long id) {
	    DefectResponse defect = defectService.trackDefect(id);
	    return ResponseEntity.ok(defect);
	}
}
