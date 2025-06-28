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
import com.jovan.erp_v1.request.MaterialMovementRequest;
import com.jovan.erp_v1.response.MaterialMovementResponse;
import com.jovan.erp_v1.service.IMaterialMovementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/materialMovements")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE')")
public class MaterialMovementController {

    private final IMaterialMovementService materialMovementService;

    @PostMapping("/create/new-materialMovement")
    public ResponseEntity<MaterialMovementResponse> create(@Valid @RequestBody MaterialMovementRequest request) {
        MaterialMovementResponse response = materialMovementService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MaterialMovementResponse> update(@PathVariable Long id,
            @Valid @RequestBody MaterialMovementRequest request) {
        MaterialMovementResponse response = materialMovementService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialMovementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/find-one/{id}")
    public ResponseEntity<MaterialMovementResponse> findOne(@PathVariable Long id) {
        MaterialMovementResponse response = materialMovementService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/find-all")
    public ResponseEntity<List<MaterialMovementResponse>> findAll() {
        List<MaterialMovementResponse> responses = materialMovementService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/by-movementType")
    public ResponseEntity<List<MaterialMovementResponse>> findByType(MovementType type) {
        List<MaterialMovementResponse> responses = materialMovementService.findByType(type);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/by-quantity")
    public ResponseEntity<List<MaterialMovementResponse>> findByQuantity(
            @RequestParam("quantity") BigDecimal quantity) {
        List<MaterialMovementResponse> responses = materialMovementService.findByQuantity(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/quantity-greater-than")
    public ResponseEntity<List<MaterialMovementResponse>> findByQuantityGreaterThan(
            @RequestParam("quantity") BigDecimal quantity) {
        List<MaterialMovementResponse> responses = materialMovementService.findByQuantityGreaterThan(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/quantity-less-than")
    public ResponseEntity<List<MaterialMovementResponse>> findByQuantityLessThan(
            @RequestParam("quantity") BigDecimal quantity) {
        List<MaterialMovementResponse> responses = materialMovementService.findByQuantityLessThan(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/fromStorage/{fromStorageId}")
    public ResponseEntity<List<MaterialMovementResponse>> findByFromStorage_Id(@PathVariable Long fromStorageId) {
        List<MaterialMovementResponse> responses = materialMovementService.findByFromStorage_Id(fromStorageId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/toStorage/{toStorageId}")
    public ResponseEntity<List<MaterialMovementResponse>> findByToStorage_Id(@PathVariable Long toStorageId) {
        List<MaterialMovementResponse> responses = materialMovementService.findByToStorage_Id(toStorageId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/fromStorageName")
    public ResponseEntity<List<MaterialMovementResponse>> findByFromStorage_NameContainingIgnoreCase(
            @RequestParam("fromStorageName") String fromStorageName) {
        List<MaterialMovementResponse> responses = materialMovementService
                .findByFromStorage_NameContainingIgnoreCase(fromStorageName);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/toStorageName")
    public ResponseEntity<List<MaterialMovementResponse>> findByToStorage_NameContainingIgnoreCase(
            @RequestParam("toStorageName") String toStorageName) {
        List<MaterialMovementResponse> responses = materialMovementService
                .findByToStorage_NameContainingIgnoreCase(toStorageName);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/fromStorageLocation")
    public ResponseEntity<List<MaterialMovementResponse>> findByFromStorage_LocationContainingIgnoreCase(
            @RequestParam("fromStorageLocation") String fromStorageLocation) {
        List<MaterialMovementResponse> responses = materialMovementService
                .findByFromStorage_LocationContainingIgnoreCase(fromStorageLocation);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/toStorageLocation")
    public ResponseEntity<List<MaterialMovementResponse>> findByToStorage_LocationContainingIgnoreCase(
            @RequestParam("toStorageLocation") String toStorageLocation) {
        List<MaterialMovementResponse> responses = materialMovementService
                .findByToStorage_LocationContainingIgnoreCase(toStorageLocation);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/fromStorage-capacity")
    public ResponseEntity<List<MaterialMovementResponse>> findByFromStorage_Capacity(
            @RequestParam("capacity") BigDecimal capacity) {
        List<MaterialMovementResponse> responses = materialMovementService.findByFromStorage_Capacity(capacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/toStorage-capacity")
    public ResponseEntity<List<MaterialMovementResponse>> findByToStorage_Capacity(
            @RequestParam("capacity") BigDecimal capacity) {
        List<MaterialMovementResponse> responses = materialMovementService.findByToStorage_Capacity(capacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/by-movementDate")
    public ResponseEntity<List<MaterialMovementResponse>> findByMovementDate(
            @RequestParam("movementDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate movementDate) {
        List<MaterialMovementResponse> responses = materialMovementService.findByMovementDate(movementDate);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/between-dates")
    public ResponseEntity<List<MaterialMovementResponse>> findByMovementDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<MaterialMovementResponse> responses = materialMovementService.findByMovementDateBetween(start, end);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/date-greater-than-equal")
    public ResponseEntity<List<MaterialMovementResponse>> findByMovementDateGreaterThanEqual(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<MaterialMovementResponse> responses = materialMovementService.findByMovementDateGreaterThanEqual(date);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_STORAGE_FOREMAN', 'ROLE_STORAGE_EMPLOYEE', 'ROLE_QUALITY_CONTROL')")
    @GetMapping("/by-movementDate-after-equal")
    public ResponseEntity<List<MaterialMovementResponse>> findByMovementDateAfterOrEqual(
            @RequestParam("movementDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate movementDate) {
        List<MaterialMovementResponse> responses = materialMovementService.findByMovementDateAfterOrEqual(movementDate);
        return ResponseEntity.ok(responses);
    }

}
