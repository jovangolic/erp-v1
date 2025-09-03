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

import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.request.MaterialRequestDTO;
import com.jovan.erp_v1.response.MaterialRequestResponse;
import com.jovan.erp_v1.service.IMaterialRequestService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/materialRequests")
@PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
public class MaterialRequestController {

    private final IMaterialRequestService materialRequestService;

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_FULL_ACCESS)
    @PostMapping("/create/new-materialRequest")
    public ResponseEntity<MaterialRequestResponse> create(@Valid @RequestBody MaterialRequestDTO dto) {
        MaterialRequestResponse response = materialRequestService.create(dto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<MaterialRequestResponse> update(@PathVariable Long id,
            @Valid @RequestBody MaterialRequestDTO dto) {
        MaterialRequestResponse response = materialRequestService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<MaterialRequestResponse> findOne(@PathVariable Long id) {
        MaterialRequestResponse response = materialRequestService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<MaterialRequestResponse>> findAll() {
        List<MaterialRequestResponse> responses = materialRequestService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/work-center-name")
    public ResponseEntity<List<MaterialRequestResponse>> findByRequestingWorkCenter_NameContainingIgnoreCase(
            @RequestParam("workCenterName") String workCenterName) {
        List<MaterialRequestResponse> responses = materialRequestService
                .findByRequestingWorkCenter_NameContainingIgnoreCase(workCenterName);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/work-center-location")
    public ResponseEntity<List<MaterialRequestResponse>> findByRequestingWorkCenter_LocationContainingIgnoreCase(
            @RequestParam("workCenterLocation") String workCenterLocation) {
        List<MaterialRequestResponse> responses = materialRequestService
                .findByRequestingWorkCenter_LocationContainingIgnoreCase(workCenterLocation);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/work-center-capacity")
    public ResponseEntity<List<MaterialRequestResponse>> findByRequestingWorkCenter_Capacity(
            @RequestParam("workCenterCapacity") BigDecimal workCenterCapacity) {
        List<MaterialRequestResponse> responses = materialRequestService
                .findByRequestingWorkCenter_Capacity(workCenterCapacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/work-center-capacity-greater-than")
    public ResponseEntity<List<MaterialRequestResponse>> findByRequestingWorkCenter_CapacityGreaterThan(
            @RequestParam("workCenterCapacity") BigDecimal workCenterCapacity) {
        List<MaterialRequestResponse> responses = materialRequestService
                .findByRequestingWorkCenter_CapacityGreaterThan(workCenterCapacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/workCenter-capacity-less-than")
    public ResponseEntity<List<MaterialRequestResponse>> findByRequestingWorkCenter_CapacityLessThan(
            @RequestParam("workCenterCapacity") BigDecimal workCenterCapacity) {
        List<MaterialRequestResponse> responses = materialRequestService
                .findByRequestingWorkCenter_CapacityLessThan(workCenterCapacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/by-quantity")
    public ResponseEntity<List<MaterialRequestResponse>> findByQuantity(@RequestParam("quantity") BigDecimal quantity) {
        List<MaterialRequestResponse> responses = materialRequestService.findByQuantity(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/quantity-greater-than")
    public ResponseEntity<List<MaterialRequestResponse>> findByQuantityGreaterThan(
            @RequestParam("quantity") BigDecimal quantity) {
        List<MaterialRequestResponse> responses = materialRequestService.findByQuantityGreaterThan(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/quantity-less-than")
    public ResponseEntity<List<MaterialRequestResponse>> findByQuantityLessThan(
            @RequestParam("quantity") BigDecimal quantity) {
        List<MaterialRequestResponse> responses = materialRequestService.findByQuantityLessThan(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/material/{materialId}")
    public ResponseEntity<List<MaterialRequestResponse>> findByMaterial_Id(@PathVariable Long materialId) {
        List<MaterialRequestResponse> responses = materialRequestService.findByMaterial_Id(materialId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/material-by-code")
    public ResponseEntity<List<MaterialRequestResponse>> findByMaterial_CodeContainingIgnoreCase(
            @RequestParam("code") String code) {
        List<MaterialRequestResponse> responses = materialRequestService.findByMaterial_CodeContainingIgnoreCase(code);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/material-by-name")
    public ResponseEntity<List<MaterialRequestResponse>> findByMaterial_NameContainingIgnoreCase(
            @RequestParam("name") String name) {
        List<MaterialRequestResponse> responses = materialRequestService.findByMaterial_NameContainingIgnoreCase(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/material-unit")
    public ResponseEntity<List<MaterialRequestResponse>> findByMaterial_Unit(@RequestParam("unit") UnitOfMeasure unit) {
        List<MaterialRequestResponse> responses = materialRequestService.findByMaterial_Unit(unit);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/material-current-stock")
    public ResponseEntity<List<MaterialRequestResponse>> findByMaterial_CurrentStock(
            @RequestParam("currentStock") BigDecimal currentStock) {
        List<MaterialRequestResponse> responses = materialRequestService.findByMaterial_CurrentStock(currentStock);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/material-current-stock-less-than")
    public ResponseEntity<List<MaterialRequestResponse>> findByMaterial_CurrentStockLessThan(
            @RequestParam("currentStock") BigDecimal currentStock) {
        List<MaterialRequestResponse> responses = materialRequestService
                .findByMaterial_CurrentStockLessThan(currentStock);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/material-current-stock-greater-than")
    public ResponseEntity<List<MaterialRequestResponse>> findByMaterial_CurrentStockGreaterThan(
            @RequestParam("currentStock") BigDecimal currentStock) {
        List<MaterialRequestResponse> responses = materialRequestService
                .findByMaterial_CurrentStockGreaterThan(currentStock);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/material-reorder-level")
    public ResponseEntity<List<MaterialRequestResponse>> findByMaterial_ReorderLevel(
            @RequestParam("reorderLevel") BigDecimal reorderLevel) {
        List<MaterialRequestResponse> responses = materialRequestService.findByMaterial_ReorderLevel(reorderLevel);
        return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/material-reorder-level-greater-than")
    public ResponseEntity<List<MaterialRequestResponse>> findByMaterial_ReorderLevelGreaterThan(@RequestParam("reorderLevel") BigDecimal reorderLevel){
    	List<MaterialRequestResponse> responses = materialRequestService.findByMaterial_ReorderLevelGreaterThan(reorderLevel);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/material-reorder-level-less-than")
    public ResponseEntity<List<MaterialRequestResponse>> findByMaterial_ReorderLevelLessThan(@RequestParam("reorderLevel") BigDecimal reorderLevel){
    	List<MaterialRequestResponse> responses = materialRequestService.findByMaterial_ReorderLevelLessThan(reorderLevel);
    	return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/material/storage/{storageId}")
    public ResponseEntity<List<MaterialRequestResponse>> findByMaterial_Storage_Id(@PathVariable Long storageId) {
        List<MaterialRequestResponse> responses = materialRequestService.findByMaterial_Storage_Id(storageId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/request-date")
    public ResponseEntity<List<MaterialRequestResponse>> findByRequestDate(
            @RequestParam("requestDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDate) {
        List<MaterialRequestResponse> responses = materialRequestService.findByRequestDate(requestDate);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/request-date-before")
    public ResponseEntity<List<MaterialRequestResponse>> findByRequestDateBefore(
            @RequestParam("requestDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDate) {
        List<MaterialRequestResponse> responses = materialRequestService.findByRequestDateBefore(requestDate);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/request-date-after")
    public ResponseEntity<List<MaterialRequestResponse>> findByRequestDateAfter(
            @RequestParam("requestDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDate) {
        List<MaterialRequestResponse> responses = materialRequestService.findByRequestDateAfter(requestDate);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/request-date-between")
    public ResponseEntity<List<MaterialRequestResponse>> findByRequestDateBetween(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<MaterialRequestResponse> responses = materialRequestService.findByRequestDateBetween(startDate, endDate);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/needed-by")
    public ResponseEntity<List<MaterialRequestResponse>> findByNeededBy(
            @RequestParam("neededBy") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate neededBy) {
        List<MaterialRequestResponse> responses = materialRequestService.findByNeededBy(neededBy);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/needed-before")
    public ResponseEntity<List<MaterialRequestResponse>> findByNeededByBefore(
            @RequestParam("neededBy") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate neededBy) {
        List<MaterialRequestResponse> responses = materialRequestService.findByNeededByBefore(neededBy);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/needed-after")
    public ResponseEntity<List<MaterialRequestResponse>> findByNeededByAfter(
            @RequestParam("neededBy") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate neededBy) {
        List<MaterialRequestResponse> responses = materialRequestService.findByNeededByAfter(neededBy);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUEST_READ_ACCESS)
    @GetMapping("/needed-between")
    public ResponseEntity<List<MaterialRequestResponse>> findByNeededByBetween(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<MaterialRequestResponse> responses = materialRequestService.findByNeededByBetween(startDate, endDate);
        return ResponseEntity.ok(responses);
    }

}
