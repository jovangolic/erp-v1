package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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

import com.jovan.erp_v1.enumeration.MovementType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.request.MaterialMovementRequest;
import com.jovan.erp_v1.response.MaterialMovementResponse;
import com.jovan.erp_v1.service.IMaterialMovementService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/materialMovements")
@PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
public class MaterialMovementController {

    private final IMaterialMovementService materialMovementService;

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_FULL_ACCESS)
    @PostMapping("/create/new-materialMovement")
    public ResponseEntity<MaterialMovementResponse> create(@Valid @RequestBody MaterialMovementRequest request) {
        MaterialMovementResponse response = materialMovementService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<MaterialMovementResponse> update(@PathVariable Long id,
            @Valid @RequestBody MaterialMovementRequest request) {
        MaterialMovementResponse response = materialMovementService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialMovementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<MaterialMovementResponse> findOne(@PathVariable Long id) {
        MaterialMovementResponse response = materialMovementService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<MaterialMovementResponse>> findAll() {
        List<MaterialMovementResponse> responses = materialMovementService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/by-movement-type")
    public ResponseEntity<List<MaterialMovementResponse>> findByType(MovementType type) {
        List<MaterialMovementResponse> responses = materialMovementService.findByType(type);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/by-quantity")
    public ResponseEntity<List<MaterialMovementResponse>> findByQuantity(
            @RequestParam("quantity") BigDecimal quantity) {
        List<MaterialMovementResponse> responses = materialMovementService.findByQuantity(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/quantity-greater-than")
    public ResponseEntity<List<MaterialMovementResponse>> findByQuantityGreaterThan(
            @RequestParam("quantity") BigDecimal quantity) {
        List<MaterialMovementResponse> responses = materialMovementService.findByQuantityGreaterThan(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/quantity-less-than")
    public ResponseEntity<List<MaterialMovementResponse>> findByQuantityLessThan(
            @RequestParam("quantity") BigDecimal quantity) {
        List<MaterialMovementResponse> responses = materialMovementService.findByQuantityLessThan(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/fromStorage/{fromStorageId}")
    public ResponseEntity<List<MaterialMovementResponse>> findByFromStorage_Id(@PathVariable Long fromStorageId) {
        List<MaterialMovementResponse> responses = materialMovementService.findByFromStorage_Id(fromStorageId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/toStorage/{toStorageId}")
    public ResponseEntity<List<MaterialMovementResponse>> findByToStorage_Id(@PathVariable Long toStorageId) {
        List<MaterialMovementResponse> responses = materialMovementService.findByToStorage_Id(toStorageId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/from-storage-name")
    public ResponseEntity<List<MaterialMovementResponse>> findByFromStorage_NameContainingIgnoreCase(
            @RequestParam("fromStorageName") String fromStorageName) {
        List<MaterialMovementResponse> responses = materialMovementService
                .findByFromStorage_NameContainingIgnoreCase(fromStorageName);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/to-storage-name")
    public ResponseEntity<List<MaterialMovementResponse>> findByToStorage_NameContainingIgnoreCase(
            @RequestParam("toStorageName") String toStorageName) {
        List<MaterialMovementResponse> responses = materialMovementService
                .findByToStorage_NameContainingIgnoreCase(toStorageName);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/from-storage-location")
    public ResponseEntity<List<MaterialMovementResponse>> findByFromStorage_LocationContainingIgnoreCase(
            @RequestParam("fromStorageLocation") String fromStorageLocation) {
        List<MaterialMovementResponse> responses = materialMovementService
                .findByFromStorage_LocationContainingIgnoreCase(fromStorageLocation);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/to-storage-location")
    public ResponseEntity<List<MaterialMovementResponse>> findByToStorage_LocationContainingIgnoreCase(
            @RequestParam("toStorageLocation") String toStorageLocation) {
        List<MaterialMovementResponse> responses = materialMovementService
                .findByToStorage_LocationContainingIgnoreCase(toStorageLocation);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/from-storage-capacity")
    public ResponseEntity<List<MaterialMovementResponse>> findByFromStorage_Capacity(
            @RequestParam("capacity") BigDecimal capacity) {
        List<MaterialMovementResponse> responses = materialMovementService.findByFromStorage_Capacity(capacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/to-storage-capacity")
    public ResponseEntity<List<MaterialMovementResponse>> findByToStorage_Capacity(
            @RequestParam("capacity") BigDecimal capacity) {
        List<MaterialMovementResponse> responses = materialMovementService.findByToStorage_Capacity(capacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/by-movement-date")
    public ResponseEntity<List<MaterialMovementResponse>> findByMovementDate(
            @RequestParam("movementDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate movementDate) {
        List<MaterialMovementResponse> responses = materialMovementService.findByMovementDate(movementDate);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/between-dates")
    public ResponseEntity<List<MaterialMovementResponse>> findByMovementDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<MaterialMovementResponse> responses = materialMovementService.findByMovementDateBetween(start, end);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/date-greater-than-equal")
    public ResponseEntity<List<MaterialMovementResponse>> findByMovementDateGreaterThanEqual(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<MaterialMovementResponse> responses = materialMovementService.findByMovementDateGreaterThanEqual(date);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/by-movement-date-after-equal")
    public ResponseEntity<List<MaterialMovementResponse>> findByMovementDateAfterOrEqual(
            @RequestParam("movementDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate movementDate) {
        List<MaterialMovementResponse> responses = materialMovementService.findByMovementDateAfterOrEqual(movementDate);
        return ResponseEntity.ok(responses);
    }
    
    //nove metode
    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/from-storage/{fromStorageId}/available-capacity")
    public ResponseEntity<BigDecimal> getAvailableCapacityFromStorage(@PathVariable Long fromStorageId) {
        BigDecimal capacity = materialMovementService.countAvailableCapacityFromStorage(fromStorageId);
        return ResponseEntity.ok(capacity);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @PostMapping("/from-storage/{fromStorageId}/allocate")
    public ResponseEntity<Void> allocateCapacityFromStorage(@PathVariable Long fromStorageId, @RequestBody BigDecimal amount) {
    	materialMovementService.allocateCapacityFromStorage(fromStorageId, amount);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @PostMapping("/from-storage/{fromStorageId}/release")
    public ResponseEntity<Void> releaseCapacityFromStorage(@PathVariable Long fromStorageId, @RequestBody BigDecimal amount) {
    	materialMovementService.releaseCapacityFromStorage(fromStorageId, amount);
        return ResponseEntity.ok().build();
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/to-storage/{toStorageId}/available-capacity")
    public ResponseEntity<BigDecimal> getAvailableCapacityToStorage(@PathVariable Long toStorageId) {
        BigDecimal capacity = materialMovementService.countAvailableCapacityToStorage(toStorageId);
        return ResponseEntity.ok(capacity);
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @PostMapping("/to-storage/{toStorageId}/allocate")
    public ResponseEntity<Void> allocateCapacityToStorage(@PathVariable Long toStorageId, @RequestBody BigDecimal amount) {
    	materialMovementService.allocateCapacityToStorage(toStorageId, amount);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @PostMapping("/to-storage/{toStorageId}/release")
    public ResponseEntity<Void> releaseCapacityToStorage(@PathVariable Long toStorageId, @RequestBody BigDecimal amount) {
    	materialMovementService.releaseCapacityToStorage(toStorageId, amount);
        return ResponseEntity.ok().build();
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/search/from-storage-capacity-greater-than")
    public ResponseEntity<List<MaterialMovementResponse>> findByFromStorage_CapacityGreaterThan(@RequestParam("capacity") BigDecimal capacity){
    	List<MaterialMovementResponse> responses = materialMovementService.findByFromStorage_CapacityGreaterThan(capacity);
        return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/search/to-storage-capacity-greater-than")
    public ResponseEntity<List<MaterialMovementResponse>> findByToStorage_CapacityGreaterThan(@RequestParam("capacity") BigDecimal capacity){
    	List<MaterialMovementResponse> responses = materialMovementService.findByToStorage_CapacityGreaterThan(capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/search/from-storage-capacity-less-than")
    public ResponseEntity<List<MaterialMovementResponse>> findByFromStorage_CapacityLessThan(@RequestParam("capacity") BigDecimal capacity){
    	List<MaterialMovementResponse> responses = materialMovementService.findByFromStorage_CapacityLessThan(capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/search/to-storage-capacity-less-than")
    public ResponseEntity<List<MaterialMovementResponse>> findByToStorage_CapacityLessThan(@RequestParam("capacity") BigDecimal capacity){
    	List<MaterialMovementResponse> responses = materialMovementService.findByToStorage_CapacityLessThan(capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/search/from-storage-type")
    public ResponseEntity<List<MaterialMovementResponse>> findByFromStorage_Type(@RequestParam("type") StorageType type){
    	List<MaterialMovementResponse> responses = materialMovementService.findByFromStorage_Type(type);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/search/to-storage-type")
    public ResponseEntity<List<MaterialMovementResponse>> findByToStorage_Type(@RequestParam("type") StorageType type){
    	List<MaterialMovementResponse> responses = materialMovementService.findByToStorage_Type(type);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/search/from-storage-status")
    public ResponseEntity<List<MaterialMovementResponse>> findByFromStorage_Status(@RequestParam("status") StorageStatus status){
    	List<MaterialMovementResponse> responses = materialMovementService.findByFromStorage_Status(status);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_MOVEMENT_READ_ACCESS)
    @GetMapping("/search/to-storage-status")
    public ResponseEntity<List<MaterialMovementResponse>> findByToStorage_Status(@RequestParam("status") StorageStatus status){
    	List<MaterialMovementResponse> responses = materialMovementService.findByToStorage_Status(status);
    	return ResponseEntity.ok(responses);
    }

}
