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

import com.jovan.erp_v1.request.BillOfMaterialsRequest;
import com.jovan.erp_v1.response.BillOfMaterialsResponse;
import com.jovan.erp_v1.service.IBillOfMaterialsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/billOfMaterials")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRODUCTION_PLANNER', 'QUALITY_MANAGER')")
public class BillOfMaterialsController {

    private final IBillOfMaterialsService billOfMaterialsService;

    @PostMapping("/create/new-billOfMaterial")
    public ResponseEntity<BillOfMaterialsResponse> create(@Valid @RequestBody BillOfMaterialsRequest request) {
        BillOfMaterialsResponse response = billOfMaterialsService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BillOfMaterialsResponse> update(@PathVariable Long id,
            @Valid @RequestBody BillOfMaterialsRequest request) {
        BillOfMaterialsResponse response = billOfMaterialsService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        billOfMaterialsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRODUCTION_PLANNER', 'QUALITY_MANAGER','INVENTORY_MANAGER')")
    @GetMapping("/find-one/{id}")
    public ResponseEntity<BillOfMaterialsResponse> findOne(@PathVariable Long id) {
        BillOfMaterialsResponse response = billOfMaterialsService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRODUCTION_PLANNER', 'QUALITY_MANAGER','INVENTORY_MANAGER')")
    @GetMapping("/find-all")
    public ResponseEntity<List<BillOfMaterialsResponse>> findAll() {
        List<BillOfMaterialsResponse> responses = billOfMaterialsService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRODUCTION_PLANNER', 'QUALITY_MANAGER','INVENTORY_MANAGER')")
    @GetMapping("/by-parent/{parentProductId}")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByParentProductId(@PathVariable Long parentProductId) {
        List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByParentProductId(parentProductId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRODUCTION_PLANNER', 'QUALITY_MANAGER','INVENTORY_MANAGER')")
    @GetMapping("/by-component/{componentId}")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByComponentId(@PathVariable Long componentId) {
        List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByComponentId(componentId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRODUCTION_PLANNER', 'QUALITY_MANAGER','INVENTORY_MANAGER')")
    @GetMapping("/find-all")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByQuantityGreaterThan(
            @RequestParam("quantity") BigDecimal quantity) {
        List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByQuantityGreaterThan(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRODUCTION_PLANNER', 'QUALITY_MANAGER','INVENTORY_MANAGER')")
    @GetMapping("/find-all")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByQuantity(@RequestParam("quantity") BigDecimal quantity) {
        List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByQuantity(quantity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'PRODUCTION_PLANNER', 'QUALITY_MANAGER','INVENTORY_MANAGER')")
    @GetMapping("/find-all")
    public ResponseEntity<List<BillOfMaterialsResponse>> findByQuantityLessThan(
            @RequestParam("quantity") BigDecimal quantity) {
        List<BillOfMaterialsResponse> responses = billOfMaterialsService.findByQuantityLessThan(quantity);
        return ResponseEntity.ok(responses);
    }
}
