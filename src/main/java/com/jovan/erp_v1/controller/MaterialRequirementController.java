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

import com.jovan.erp_v1.enumeration.MaterialRequestStatus;
import com.jovan.erp_v1.enumeration.ProductionOrderStatus;
import com.jovan.erp_v1.enumeration.UnitOfMeasure;
import com.jovan.erp_v1.request.MaterialRequirementRequest;
import com.jovan.erp_v1.response.MaterialRequirementResponse;
import com.jovan.erp_v1.service.IMaterialRequirementService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/materialRequirements")
@PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
public class MaterialRequirementController {

    private final IMaterialRequirementService materialRequirementService;

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_FULL_ACCESS)
    @PostMapping("/create/new-materialRequirement")
    public ResponseEntity<MaterialRequirementResponse> create(@Valid @RequestBody MaterialRequirementRequest request) {
        MaterialRequirementResponse response = materialRequirementService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<MaterialRequirementResponse> update(@PathVariable Long id,
            @Valid @RequestBody MaterialRequirementRequest request) {
        MaterialRequirementResponse response = materialRequirementService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialRequirementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<MaterialRequirementResponse> findOne(@PathVariable Long id) {
        MaterialRequirementResponse response = materialRequirementService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<MaterialRequirementResponse>> findAll() {
        List<MaterialRequirementResponse> responses = materialRequirementService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/productionOrder/{productionOrderId}")
    public ResponseEntity<List<MaterialRequirementResponse>> findByProductionOrder_Id(
            @PathVariable Long productionOrderId) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByProductionOrder_Id(productionOrderId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/production-order-order-number")
    public ResponseEntity<List<MaterialRequirementResponse>> findByProductionOrder_OrderNumberContainingIgnoreCase(
            @RequestParam("orderNumber") String orderNumber) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByProductionOrder_OrderNumberContainingIgnoreCase(orderNumber);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/production-order/product/{productId}")
    public ResponseEntity<List<MaterialRequirementResponse>> findByProductionOrder_Product_Id(
            @PathVariable Long productId) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByProductionOrder_Product_Id(productId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/production-order-quantity-planned")
    public ResponseEntity<List<MaterialRequirementResponse>> findByProductionOrder_QuantityPlanned(
            @RequestParam("quantityPlanned") Integer quantityPlanned) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByProductionOrder_QuantityPlanned(quantityPlanned);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/production-order-quantity-planned-less-than")
    public ResponseEntity<List<MaterialRequirementResponse>> findByProductionOrder_QuantityPlannedLessThan(
            @RequestParam("quantityPlanned") Integer quantityPlanned) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByProductionOrder_QuantityPlannedLessThan(quantityPlanned);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/production-order-quantity-planned-greater-than")
    public ResponseEntity<List<MaterialRequirementResponse>> findByProductionOrder_QuantityPlannedGreaterThan(
            @RequestParam("quantityPlanned") Integer quantityPlanned) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByProductionOrder_QuantityPlannedGreaterThan(quantityPlanned);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/production-order-quantity-produced")
    public ResponseEntity<List<MaterialRequirementResponse>> findByProductionOrder_QuantityProduced(
            @RequestParam("quantityProduced") Integer quantityProduced) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByProductionOrder_QuantityProduced(quantityProduced);
        return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/production-order-quantity-produced-greater-than")
    public ResponseEntity<List<MaterialRequirementResponse>> findByProductionOrder_QuantityProducedGreaterThan(
            @RequestParam("quantityProduced") Integer quantityProduced){
    	List<MaterialRequirementResponse> responses = materialRequirementService.findByProductionOrder_QuantityProducedGreaterThan(quantityProduced);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/production-order-quantity-produced-less-than")
    public ResponseEntity<List<MaterialRequirementResponse>> findByProductionOrder_QuantityProducedLessThan(
            @RequestParam("quantityProduced") Integer quantityProduced){
    	List<MaterialRequirementResponse> responses = materialRequirementService.findByProductionOrder_QuantityProducedLessThan(quantityProduced);
    	return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/production-order-startDate")
    public ResponseEntity<List<MaterialRequirementResponse>> findByProductionOrder_StartDate(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByProductionOrder_StartDate(startDate);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/production-order-endDate")
    public ResponseEntity<List<MaterialRequirementResponse>> findByProductionOrder_EndDate(
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<MaterialRequirementResponse> responses = materialRequirementService.findByProductionOrder_EndDate(endDate);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/production-order-status")
    public ResponseEntity<List<MaterialRequirementResponse>> findByProductionOrder_Status(
            @RequestParam("status") ProductionOrderStatus status) {
        List<MaterialRequirementResponse> responses = materialRequirementService.findByProductionOrder_Status(status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/production-order/workCenter/{workCenterId}")
    public ResponseEntity<List<MaterialRequirementResponse>> findByProductionOrder_WorkCenter_Id(
            @PathVariable Long workCenterId) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByProductionOrder_WorkCenter_Id(workCenterId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/material/{materialId}")
    public ResponseEntity<List<MaterialRequirementResponse>> findByMaterial_Id(@PathVariable Long materialId) {
        List<MaterialRequirementResponse> responses = materialRequirementService.findByMaterial_Id(materialId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/material-code")
    public ResponseEntity<List<MaterialRequirementResponse>> findByMaterial_CodeContainingIgnoreCase(
            @RequestParam("materialCode") String materialCode) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByMaterial_CodeContainingIgnoreCase(materialCode);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/material-name")
    public ResponseEntity<List<MaterialRequirementResponse>> findByMaterial_NameContainingIgnoreCase(
            @RequestParam("materialName") String materialName) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByMaterial_NameContainingIgnoreCase(materialName);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/material-unit")
    public ResponseEntity<List<MaterialRequirementResponse>> findByMaterial_Unit(
            @RequestParam("unit") UnitOfMeasure unit) {
        List<MaterialRequirementResponse> responses = materialRequirementService.findByMaterial_Unit(unit);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/material-current-stock")
    public ResponseEntity<List<MaterialRequirementResponse>> findByMaterial_CurrentStock(
            @RequestParam("currentStock") BigDecimal currentStock) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByMaterial_CurrentStock(currentStock);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/material-current-stock-less-than")
    public ResponseEntity<List<MaterialRequirementResponse>> findByMaterial_CurrentStockLessThan(
            @RequestParam("currentStock") BigDecimal currentStock) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByMaterial_CurrentStockLessThan(currentStock);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/material-current-stock-greater-than")
    public ResponseEntity<List<MaterialRequirementResponse>> findByMaterial_CurrentStockGreaterThan(
            @RequestParam("currentStock") BigDecimal currentStock) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByMaterial_CurrentStockGreaterThan(currentStock);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/material/storage/{storageId}")
    public ResponseEntity<List<MaterialRequirementResponse>> findByMaterial_Storage_Id(@PathVariable Long storageId) {
        List<MaterialRequirementResponse> responses = materialRequirementService.findByMaterial_Storage_Id(storageId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/material-reorder-level")
    public ResponseEntity<List<MaterialRequirementResponse>> findByMaterial_ReorderLevel(
            @RequestParam("reorderLevel") BigDecimal reorderLevel) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByMaterial_ReorderLevel(reorderLevel);
        return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/material-reorder-level-greater-than")
    public ResponseEntity<List<MaterialRequirementResponse>> findByMaterial_ReorderLevelGreaterThan(
            @RequestParam("reorderLevel") BigDecimal reorderLevel){
    	List<MaterialRequirementResponse> responses = materialRequirementService.findByMaterial_ReorderLevelGreaterThan(reorderLevel);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/material-reorder-level-less-than")
    public ResponseEntity<List<MaterialRequirementResponse>> findByMaterial_ReorderLevelLessThan(
            @RequestParam("reorderLevel") BigDecimal reorderLevel){
    	List<MaterialRequirementResponse> responses = materialRequirementService.findByMaterial_ReorderLevelLessThan(reorderLevel);
    	return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/material-status")
    public ResponseEntity<List<MaterialRequirementResponse>> findByStatus(
            @RequestParam("status") MaterialRequestStatus status) {
        List<MaterialRequirementResponse> responses = materialRequirementService.findByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/material-required-quantity")
    public ResponseEntity<List<MaterialRequirementResponse>> findByRequiredQuantity(
            @RequestParam("requiredQuantity") BigDecimal requiredQuantity) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByRequiredQuantity(requiredQuantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/material-required-quantity-less-than")
    public ResponseEntity<List<MaterialRequirementResponse>> findByRequiredQuantityLessThan(
            @RequestParam("requiredQuantity") BigDecimal requiredQuantity) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByRequiredQuantityLessThan(requiredQuantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/material-required-quantity-greater-than")
    public ResponseEntity<List<MaterialRequirementResponse>> findByRequiredQuantityGreaterThan(
            @RequestParam("requiredQuantity") BigDecimal requiredQuantity) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByRequiredQuantityGreaterThan(requiredQuantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/available-quantity")
    public ResponseEntity<List<MaterialRequirementResponse>> findByAvailableQuantity(
            @RequestParam("availableQuantity") BigDecimal availableQuantity) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByAvailableQuantity(availableQuantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/available-quantity-less-than")
    public ResponseEntity<List<MaterialRequirementResponse>> findByAvailableQuantityLessThan(
            @RequestParam("availableQuantity") BigDecimal availableQuantity) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByAvailableQuantityLessThan(availableQuantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/available-quantity-greater-than")
    public ResponseEntity<List<MaterialRequirementResponse>> findByAvailableQuantityGreaterThan(
            @RequestParam("availableQuantity") BigDecimal availableQuantity) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByAvailableQuantityGreaterThan(availableQuantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/requirement-date")
    public ResponseEntity<List<MaterialRequirementResponse>> findByRequirementDate(
            @RequestParam("requirementDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requirementDate) {
        List<MaterialRequirementResponse> responses = materialRequirementService.findByRequirementDate(requirementDate);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/requirement-date-range")
    public ResponseEntity<List<MaterialRequirementResponse>> findByRequirementDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<MaterialRequirementResponse> responses = materialRequirementService.findByRequirementDateBetween(start,
                end);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/requirement-date-greater-than-equal")
    public ResponseEntity<List<MaterialRequirementResponse>> findByRequirementDateGreaterThanEqual(
            @RequestParam("requirementDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requirementDate) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByRequirementDateGreaterThanEqual(requirementDate);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/production-order/order-number-and-material-code")
    public ResponseEntity<List<MaterialRequirementResponse>> findByProductionOrder_OrderNumberContainingIgnoreCaseAndMaterial_CodeContainingIgnoreCase(
            @RequestParam("orderNumber") String orderNumber, @RequestParam("materialCode") String materialCode) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findByProductionOrder_OrderNumberContainingIgnoreCaseAndMaterial_CodeContainingIgnoreCase(orderNumber,
                        materialCode);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.MATERIAL_REQUIREMENT_READ_ACCESS)
    @GetMapping("/search-by-minShortage")
    public ResponseEntity<List<MaterialRequirementResponse>> findWhereShortageIsGreaterThan(
            @RequestParam("minShortage") BigDecimal minShortage) {
        List<MaterialRequirementResponse> responses = materialRequirementService
                .findWhereShortageIsGreaterThan(minShortage);
        return ResponseEntity.ok(responses);
    }
}
