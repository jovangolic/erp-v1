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

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.request.MaterialRequest;
import com.jovan.erp_v1.response.MaterialResponse;
import com.jovan.erp_v1.service.IMaterialService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/materials")
@PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
public class MaterialController {

    private final IMaterialService materialService;

    @PreAuthorize(RoleGroups.MATERIAL_FULL_ACCESS)
    @PostMapping("/create/new-material")
    public ResponseEntity<MaterialResponse> create(@Valid @RequestBody MaterialRequest request) {
        MaterialResponse response = materialService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.MATERIAL_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<MaterialResponse> update(@PathVariable Long id, @Valid @RequestBody MaterialRequest request) {
        MaterialResponse response = materialService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.MATERIAL_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<MaterialResponse> findOne(@PathVariable Long id) {
        MaterialResponse response = materialService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<MaterialResponse>> findAll() {
        List<MaterialResponse> responses = materialService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
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

    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/storage/{storageId}")
    public ResponseEntity<List<MaterialResponse>> findByStorage_Id(@PathVariable Long storageId) {
        List<MaterialResponse> responses = materialService.findByStorage_Id(storageId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/by-code")
    public ResponseEntity<List<MaterialResponse>> findByCode(@RequestParam("code") String code) {
        List<MaterialResponse> responses = materialService.findByCode(code);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/search-by-name")
    public ResponseEntity<List<MaterialResponse>> findByNameContainingIgnoreCase(@RequestParam("name") String name) {
        List<MaterialResponse> responses = materialService.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/by-name-code")
    public ResponseEntity<List<MaterialResponse>> search(@RequestParam("name") String name,
            @RequestParam("code") String code) {
        List<MaterialResponse> responses = materialService.search(name, code);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/by-unit")
    public ResponseEntity<List<MaterialResponse>> findByUnit(@RequestParam("unit") UnitOfMeasure unit) {
        List<MaterialResponse> responses = materialService.findByUnit(unit);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/by-storageName")
    public ResponseEntity<List<MaterialResponse>> findByStorage_Name(@RequestParam("storageName") String storageName) {
        List<MaterialResponse> responses = materialService.findByStorage_Name(storageName);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/by-storageCapacity")
    public ResponseEntity<List<MaterialResponse>> findByStorage_Capacity(@RequestParam("capacity") BigDecimal capacity) {
        List<MaterialResponse> responses = materialService.findByStorage_Capacity(capacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/by-storageType")
    public ResponseEntity<List<MaterialResponse>> findByStorage_Type(@RequestParam("type") StorageType type) {
        List<MaterialResponse> responses = materialService.findByStorage_Type(type);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/by-currentStock")
    public ResponseEntity<List<MaterialResponse>> findByCurrentStock(
            @RequestParam("currentStock") BigDecimal currentStock) {
        List<MaterialResponse> responses = materialService.findByCurrentStock(currentStock);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/by-reorderLevel")
    public ResponseEntity<List<MaterialResponse>> findByReorderLevel(
            @RequestParam("reorderLevel") BigDecimal reorderLevel) {
        List<MaterialResponse> responses = materialService.findByReorderLevel(reorderLevel);
        return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/current-stock-greater-than")
    public ResponseEntity<List<MaterialResponse>> findByCurrentStockGreaterThan(@RequestParam("currentStock") BigDecimal currentStock){
    	List<MaterialResponse> responses = materialService.findByCurrentStockGreaterThan(currentStock);
    	return ResponseEntity.ok(responses);			
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/current-stock-less-than")
    public ResponseEntity<List<MaterialResponse>> findByCurrentStockLessThan(@RequestParam("currentStock") BigDecimal currentStock){
    	List<MaterialResponse> responses = materialService.findByCurrentStockLessThan(currentStock);
    	return ResponseEntity.ok(responses);	
    }
    
    //nove metode
    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/{id}/available-capacity")
    public ResponseEntity<BigDecimal> getAvailableCapacity(@PathVariable Long id) {
        BigDecimal capacity = materialService.countAvailableCapacity(id);
        return ResponseEntity.ok(capacity);
    }

    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @PostMapping("/{id}/allocate")
    public ResponseEntity<Void> allocateCapacity(@PathVariable Long id, @RequestBody BigDecimal amount) {
    	materialService.allocateCapacity(id, amount);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @PostMapping("/{id}/release")
    public ResponseEntity<Void> releaseCapacity(@PathVariable Long id, @RequestBody BigDecimal amount) {
    	materialService.releaseCapacity(id, amount);
        return ResponseEntity.ok().build();
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/reorder-level-greater-thna")
    public ResponseEntity<List<MaterialResponse>> findByReorderLevelGreaterThan(@RequestParam("reorderLevel") BigDecimal reorderLevel){
    	List<MaterialResponse> responses = materialService.findByReorderLevelGreaterThan(reorderLevel);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/reorder-level-less-thna")
    public ResponseEntity<List<MaterialResponse>> findByReorderLevelLessThan(@RequestParam("reorderLevel") BigDecimal reorderLevel){
    	List<MaterialResponse> responses = materialService.findByReorderLevelLessThan(reorderLevel);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/search/storage-location")
    public ResponseEntity<List<MaterialResponse>> findByStorage_LocationContainingIgnoreCase(@RequestParam("storageLocation") String storageLocation){
    	List<MaterialResponse> responses = materialService.findByStorage_LocationContainingIgnoreCase(storageLocation);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/search/storage-capacity-greater-than")
    public ResponseEntity<List<MaterialResponse>> findByStorage_CapacityGreaterThan(@RequestParam("capacity") BigDecimal capacity){
    	List<MaterialResponse> responses = materialService.findByStorage_CapacityGreaterThan(capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/search/storage-capacity-less-than")
    public ResponseEntity<List<MaterialResponse>> findByStorage_CapacityLessThan(@RequestParam("capacity") BigDecimal capacity){
    	List<MaterialResponse> responses = materialService.findByStorage_CapacityLessThan(capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_READ_ACCESS)
    @GetMapping("/search/storage-status")
    public ResponseEntity<List<MaterialResponse>> findByStorage_Status(@RequestParam("status") StorageStatus status){
    	List<MaterialResponse> responses = materialService.findByStorage_Status(status);
    	return ResponseEntity.ok(responses);
    }
}
