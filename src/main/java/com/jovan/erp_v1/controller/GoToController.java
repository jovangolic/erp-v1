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

import com.jovan.erp_v1.enumeration.GoToCategory;
import com.jovan.erp_v1.enumeration.GoToType;
import com.jovan.erp_v1.request.GoToRequest;
import com.jovan.erp_v1.response.GoToResponse;
import com.jovan.erp_v1.service.IGoToService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/goto")
@PreAuthorize(RoleGroups.GOTO_ACCESS)
public class GoToController {

	private final IGoToService goToService;
	
	@PostMapping("/create/new-goto")
	public ResponseEntity<GoToResponse> create(@Valid @RequestBody GoToRequest request){
		GoToResponse items = goToService.create(request);
		return ResponseEntity.ok(items);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<GoToResponse> update( @PathVariable Long id, @Valid @RequestBody GoToRequest request){
		GoToResponse items = goToService.update(id, request);
		return ResponseEntity.ok(items);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		goToService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/find-one/{id}")
	public ResponseEntity<GoToResponse> findOne(@PathVariable Long id){
		GoToResponse items = goToService.findOne(id);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/find-all")
	public ResponseEntity<List<GoToResponse>> findAll(){
		List<GoToResponse> items = goToService.findAll();
		return ResponseEntity.ok(items);
	}
	
	
	@GetMapping("/by-category-and-active-true")
	public ResponseEntity<List<GoToResponse>> findByCategoryAndActiveTrue(@RequestParam("category") GoToCategory category){
		List<GoToResponse> items = goToService.findByCategoryAndActiveTrue(category);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-label")
	public ResponseEntity<List<GoToResponse>> findByLabelContainingIgnoreCase(@RequestParam("label") String label){
		List<GoToResponse> items = goToService.findByLabelContainingIgnoreCase(label);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-description")
	public ResponseEntity<List<GoToResponse>> findByDescriptionContainingIgnoreCase(@RequestParam("description") String description){
		List<GoToResponse> items = goToService.findByDescriptionContainingIgnoreCase(description);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-roles")
	public ResponseEntity<List<GoToResponse>> findByRolesContainingIgnoreCase(@RequestParam("roles") String roles){
		List<GoToResponse> items = goToService.findByRolesContainingIgnoreCase(roles);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-category")
	public ResponseEntity<List<GoToResponse>> findByCategory(@RequestParam("category") GoToCategory category){
		List<GoToResponse> items = goToService.findByCategory(category);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-type")
	public ResponseEntity<List<GoToResponse>> findByType(@RequestParam("type") GoToType type){
		List<GoToResponse> items = goToService.findByType(type);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/category-and-type")
	public ResponseEntity<List<GoToResponse>> findByCategoryAndType(@RequestParam("category") GoToCategory category,@RequestParam("type") GoToType type){
		List<GoToResponse> items = goToService.findByCategoryAndType(category, type);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-path")
	public ResponseEntity<List<GoToResponse>> findByPathContainingIgnoreCase(@RequestParam("path") String path){
		List<GoToResponse> items = goToService.findByPathContainingIgnoreCase(path);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-icon")
	public ResponseEntity<List<GoToResponse>> findByIcon(@RequestParam("icon") String icon){
		List<GoToResponse> items = goToService.findByIcon(icon);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/active-true")
	public ResponseEntity<List<GoToResponse>> findByActiveTrue(){
		List<GoToResponse> items = goToService.findByActiveTrue();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/label-and-category")
	public ResponseEntity<List<GoToResponse>> findByLabelContainingIgnoreCaseAndCategory(@RequestParam("label") String label,
			@RequestParam("category") GoToCategory category){
		List<GoToResponse> items = goToService.findByLabelContainingIgnoreCaseAndCategory(label, category);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/description-and-category")
	public ResponseEntity<List<GoToResponse>> findByDescriptionContainingIgnoreCaseAndCategory(@RequestParam("description") String description,
			@RequestParam("category") GoToCategory category){
		List<GoToResponse> items = goToService.findByDescriptionContainingIgnoreCaseAndCategory(description, category);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search-keyword")
	public ResponseEntity<List<GoToResponse>> searchByKeyword(@RequestParam("keyword") String keyword){
		List<GoToResponse> items = goToService.searchByKeyword(keyword);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/active-true-order-by-label-asc")
	public ResponseEntity<List<GoToResponse>> findByActiveTrueOrderByLabelAsc(){
		List<GoToResponse> items = goToService.findByActiveTrueOrderByLabelAsc();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/categories-in")
	public ResponseEntity<List<GoToResponse>> findByCategoryIn(@RequestParam("categories") List<GoToCategory> categories){
		List<GoToResponse> items = goToService.findByCategoryIn(categories);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/label-and-description")
	public ResponseEntity<List<GoToResponse>> findByLabelContainingIgnoreCaseAndDescriptionContainingIgnoreCase(@RequestParam("categories") String label,
			@RequestParam("description") String description){
		List<GoToResponse> items = goToService.findByLabelContainingIgnoreCaseAndDescriptionContainingIgnoreCase(label, description);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/exists/by-label")
	public ResponseEntity<Boolean> existsByLabel(@RequestParam("label") String label){
		Boolean items = goToService.existsByLabel(label);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/exists/by-category")
	public ResponseEntity<Boolean> existsByCategory(@RequestParam("category") GoToCategory category){
		Boolean items = goToService.existsByCategory(category);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/exists/by-roles")
	public ResponseEntity<Boolean> existsByRolesContainingIgnoreCase(@RequestParam("roles") String roles){
		Boolean items = goToService.existsByRolesContainingIgnoreCase(roles);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/exists/by-path-fragment")
	public ResponseEntity<Boolean> existsByPathContainingIgnoreCase(@RequestParam("path") String path){
		Boolean items = goToService.existsByPathContainingIgnoreCase(path);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/exists/by-icon")
	public ResponseEntity<Boolean> existsByIcon(@RequestParam("icon") String icon){
		Boolean items = goToService.existsByIcon(icon);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/exists//label-and-category")
	public ResponseEntity<Boolean> existsByLabelAndCategory(@RequestParam("label") String label,@RequestParam("category") GoToCategory category){
		Boolean items = goToService.existsByLabelAndCategory(label, category);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/exists/by-path")
	public ResponseEntity<Boolean> existsByPath(@RequestParam("path") String path){
		Boolean items = goToService.existsByPath(path);
		return ResponseEntity.ok(items);
	}
	
}
