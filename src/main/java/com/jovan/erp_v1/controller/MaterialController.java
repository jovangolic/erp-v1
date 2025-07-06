package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
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

import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.request.MaterialRequest;
import com.jovan.erp_v1.response.MaterialResponse;
import com.jovan.erp_v1.service.IMaterialService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/materials")
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER')")
public class MaterialController {

    private final IMaterialService materialService;

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER')")
    @PostMapping("/create/new-material")
    public ResponseEntity<MaterialResponse> create(@Valid @RequestBody MaterialRequest request) {
        MaterialResponse response = materialService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<MaterialResponse> update(@PathVariable Long id, @Valid @RequestBody MaterialRequest request) {
        MaterialResponse response = materialService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/find-one/{id}")
    public ResponseEntity<MaterialResponse> findOne(@PathVariable Long id) {
        MaterialResponse response = materialService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/find-all")
    public ResponseEntity<List<MaterialResponse>> findAll() {
        List<MaterialResponse> responses = materialService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search")
    public ResponseEntity<List<MaterialResponse>> searchMaterials(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) UnitOfMeasure unit,
            @RequestParam(required = false) BigDecimal currentStock,
            @RequestParam(required = false) Long storageId,
            @RequestParam(required = false) BigDecimal reorderLevel) {

        List<MaterialResponse> result = materialService.searchMaterials(name, code, unit, currentStock, storageId,
                reorderLevel);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/storage/{storageId}")
    public ResponseEntity<List<MaterialResponse>> findByStorage_Id(@PathVariable Long storageId) {
        List<MaterialResponse> responses = materialService.findByStorage_Id(storageId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-code")
    public ResponseEntity<List<MaterialResponse>> findByCode(@RequestParam("code") String code) {
        List<MaterialResponse> responses = materialService.findByCode(code);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search-by-name")
    public ResponseEntity<List<MaterialResponse>> findByNameContainingIgnoreCase(@RequestParam("name") String name) {
        List<MaterialResponse> responses = materialService.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-name-code")
    public ResponseEntity<List<MaterialResponse>> search(@RequestParam("name") String name,
            @RequestParam("code") String code) {
        List<MaterialResponse> responses = materialService.search(name, code);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-unit")
    public ResponseEntity<List<MaterialResponse>> findByUnit(@RequestParam("unit") UnitOfMeasure unit) {
        List<MaterialResponse> responses = materialService.findByUnit(unit);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-storageName")
    public ResponseEntity<List<MaterialResponse>> findByStorage_Name(@RequestParam("storageName") String storageName) {
        List<MaterialResponse> responses = materialService.findByStorage_Name(storageName);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-storageCapacity")
    public ResponseEntity<List<MaterialResponse>> findByStorage_Capacity(@RequestParam("capacity") BigDecimal capacity) {
        List<MaterialResponse> responses = materialService.findByStorage_Capacity(capacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-storageType")
    public ResponseEntity<List<MaterialResponse>> findByStorage_Type(@RequestParam("type") StorageType type) {
        List<MaterialResponse> responses = materialService.findByStorage_Type(type);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-currentStock")
    public ResponseEntity<List<MaterialResponse>> findByCurrentStock(
            @RequestParam("currentStock") BigDecimal currentStock) {
        List<MaterialResponse> responses = materialService.findByCurrentStock(currentStock);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-reorderLevel")
    public ResponseEntity<List<MaterialResponse>> findByReorderLevel(
            @RequestParam("reorderLevel") BigDecimal reorderLevel) {
        List<MaterialResponse> responses = materialService.findByReorderLevel(reorderLevel);
        return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/current-stock-greater-than")
    public ResponseEntity<List<MaterialResponse>> findByCurrentStockGreaterThan(@RequestParam("currentStock") BigDecimal currentStock){
    	List<MaterialResponse> responses = materialService.findByCurrentStockGreaterThan(currentStock);
    	return ResponseEntity.ok(responses);			
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN','INVENTORY_MANAGER','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/current-stock-less-than")
    public ResponseEntity<List<MaterialResponse>> findByCurrentStockLessThan(@RequestParam("currentStock") BigDecimal currentStock){
    	List<MaterialResponse> responses = materialService.findByCurrentStockLessThan(currentStock);
    	return ResponseEntity.ok(responses);	
    }
}
