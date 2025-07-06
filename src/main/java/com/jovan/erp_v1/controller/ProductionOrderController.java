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

import com.jovan.erp_v1.enumeration.ProductionOrderStatus;
import com.jovan.erp_v1.request.ProductionOrderRequest;
import com.jovan.erp_v1.response.ProductionOrderResponse;
import com.jovan.erp_v1.service.IProductionOrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/productionOrders")
public class ProductionOrderController {

    private final IProductionOrderService productionOrderService;

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER')")
    @PostMapping("/create/new-productionOrder")
    public ResponseEntity<ProductionOrderResponse> create(@Valid @RequestBody ProductionOrderRequest request) {
        ProductionOrderResponse response = productionOrderService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductionOrderResponse> update(@PathVariable Long id,
            @Valid @RequestBody ProductionOrderRequest request) {
        ProductionOrderResponse response = productionOrderService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productionOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/find-one/{id}")
    public ResponseEntity<ProductionOrderResponse> findOne(@PathVariable Long id) {
        ProductionOrderResponse response = productionOrderService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")

    public ResponseEntity<ProductionOrderResponse> findByOrderNumber(@RequestParam("orderNumber") String orderNumber) {
        ProductionOrderResponse response = productionOrderService.findByOrderNumber(orderNumber);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_Id(@PathVariable Long productId) {
        List<ProductionOrderResponse> response = productionOrderService.findByProduct_Id(productId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-productName")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_NameContainingIgnoreCase(
            @RequestParam("name") String name) {
        List<ProductionOrderResponse> response = productionOrderService.findByProduct_NameContainingIgnoreCase(name);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-productQuantity")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_CurrentQuantity(
            @RequestParam("currentQuantity") BigDecimal currentQuantity) {
        List<ProductionOrderResponse> response = productionOrderService.findByProduct_CurrentQuantity(currentQuantity);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-status")
    public ResponseEntity<List<ProductionOrderResponse>> findByStatus(
            @RequestParam("status") ProductionOrderStatus status) {
        List<ProductionOrderResponse> response = productionOrderService.findByStatus(status);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/workCenter/{workCenterId}")
    public ResponseEntity<List<ProductionOrderResponse>> findByWorkCenter_Id(@PathVariable Long workCenterId) {
        List<ProductionOrderResponse> response = productionOrderService.findByWorkCenter_Id(workCenterId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-workCenterName")
    public ResponseEntity<List<ProductionOrderResponse>> findByWorkCenter_NameContainingIgnoreCase(
            @RequestParam("name") String name) {
        List<ProductionOrderResponse> response = productionOrderService.findByWorkCenter_NameContainingIgnoreCase(name);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-workCenter-location")
    public ResponseEntity<List<ProductionOrderResponse>> findByWorkCenter_LocationContainingIgnoreCase(
            @RequestParam("location") String location) {
        List<ProductionOrderResponse> response = productionOrderService
                .findByWorkCenter_LocationContainingIgnoreCase(location);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-workCenter-capacity")
    public ResponseEntity<List<ProductionOrderResponse>> findByWorkCenter_Capacity(
            @RequestParam("capacity") Integer capacity) {
        List<ProductionOrderResponse> response = productionOrderService.findByWorkCenter_Capacity(capacity);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-workCenter-capacityGreaterThan")
    public ResponseEntity<List<ProductionOrderResponse>> findByWorkCenter_CapacityGreaterThan(
            @RequestParam("capacity") Integer capacity) {
        List<ProductionOrderResponse> response = productionOrderService.findByWorkCenter_CapacityGreaterThan(capacity);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-workCenter-capacityLessThan")
    public ResponseEntity<List<ProductionOrderResponse>> findByWorkCenter_CapacityLessThan(
            @RequestParam("capacity") Integer capacity) {
        List<ProductionOrderResponse> response = productionOrderService.findByWorkCenter_CapacityLessThan(capacity);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-quantityPlanned")
    public ResponseEntity<List<ProductionOrderResponse>> findByQuantityPlanned(
            @RequestParam("quantityPlanned") Integer quantityPlanned) {
        List<ProductionOrderResponse> response = productionOrderService.findByQuantityPlanned(quantityPlanned);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-quantityProduced")
    public ResponseEntity<List<ProductionOrderResponse>> findByQuantityProduced(
            @RequestParam("quantityProduced") Integer quantityProduced) {
        List<ProductionOrderResponse> response = productionOrderService.findByQuantityProduced(quantityProduced);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/date-range")
    public ResponseEntity<List<ProductionOrderResponse>> findByStartDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<ProductionOrderResponse> response = productionOrderService.findByStartDateBetween(start, end);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-startDate")
    public ResponseEntity<List<ProductionOrderResponse>> findByStartDate(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        List<ProductionOrderResponse> response = productionOrderService.findByStartDate(startDate);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/by-endDate")
    public ResponseEntity<List<ProductionOrderResponse>> findByEndDate(
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ProductionOrderResponse> response = productionOrderService.findByEndDate(endDate);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/searchOrders")
    public ResponseEntity<List<ProductionOrderResponse>> searchOrders(@RequestParam("productName") String productName,
            @RequestParam("workCenterName") String workCenterName,
            @RequestParam("startDateFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateFrom,
            @RequestParam("startDateTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateTo,
            @RequestParam("status") ProductionOrderStatus status) {
        List<ProductionOrderResponse> response = productionOrderService.searchOrders(productName, workCenterName,
                startDateFrom, startDateTo, status);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/date-greater-than-equal")
    public ResponseEntity<List<ProductionOrderResponse>> findByStartDateGreaterThanEqual(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start) {
        List<ProductionOrderResponse> response = productionOrderService.findByStartDateGreaterThanEqual(start);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search-ordersWith-date-after-equal")
    public ResponseEntity<List<ProductionOrderResponse>> findOrdersWithStartDateAfterOrEqual(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        List<ProductionOrderResponse> response = productionOrderService.findOrdersWithStartDateAfterOrEqual(startDate);
        return ResponseEntity.ok(response);
    }
}
