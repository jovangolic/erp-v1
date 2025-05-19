package com.jovan.erp_v1.controller;

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

import com.jovan.erp_v1.mapper.RawMaterialMapper;
import com.jovan.erp_v1.request.RawMaterialRequest;
import com.jovan.erp_v1.response.RawMaterialResponse;
import com.jovan.erp_v1.service.IRawMaterialService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rawMaterials")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class RawMaterialController {

	
	private final IRawMaterialService rawMaterialService;
	private final RawMaterialMapper rawMaterialMapper;
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PostMapping("/create/new-rawMaterial")
	public ResponseEntity<RawMaterialResponse> create(@Valid @RequestBody RawMaterialRequest request){
		RawMaterialResponse response = rawMaterialService.save(request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<RawMaterialResponse> update(@PathVariable Long id, @Valid @RequestBody RawMaterialRequest request){
		RawMaterialResponse response = rawMaterialService.update(id, request);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		rawMaterialService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/find-one/{id}")
	public ResponseEntity<RawMaterialResponse> findOne(@PathVariable Long id){
		RawMaterialResponse response = rawMaterialService.findById(id);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/get-all-raw-materials")
	public ResponseEntity<List<RawMaterialResponse>> findAll(){
		List<RawMaterialResponse> responses = rawMaterialService.findAll();
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("find-by-material-name")
	public ResponseEntity<List<RawMaterialResponse>> findByName(@RequestParam("name") String name){
		List<RawMaterialResponse> responses = rawMaterialService.findByName(name);
		return ResponseEntity.ok(responses);
	}
}
