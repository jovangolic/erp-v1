package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
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
    
    //nove metode
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/{id}/available-capacity")
    public ResponseEntity<BigDecimal> getAvailableCapacity(@PathVariable Long id) {
        BigDecimal capacity = productionOrderService.countAvailableCapacity(id);
        return ResponseEntity.ok(capacity);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @PostMapping("/{id}/allocate")
    public ResponseEntity<Void> allocateCapacity(@PathVariable Long id, @RequestBody BigDecimal amount) {
    	productionOrderService.allocateCapacity(id, amount);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @PostMapping("/{id}/release")
    public ResponseEntity<Void> releaseCapacity(@PathVariable Long id, @RequestBody BigDecimal amount) {
    	productionOrderService.releaseCapacity(id, amount);
        return ResponseEntity.ok().build();
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-current-quantity-greater-than")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_CurrentQuantityGreaterThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_CurrentQuantityGreaterThan(currentQuantity);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-current-less-greater-")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_CurrentQuantityLessThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_CurrentQuantityLessThan(currentQuantity);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-unit-measure")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_UnitMeasure(@RequestParam("unitMeasure") UnitMeasure unitMeasure){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_UnitMeasure(unitMeasure);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-supplier-type")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_SupplierType(@RequestParam("supplierType") SupplierType supplierType){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_SupplierType(supplierType);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-storage-type")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_StorageType(@RequestParam("storageType") StorageType storageType){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_StorageType(storageType);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product/{storageId}")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_StorageId(@PathVariable Long storageId){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_StorageId(storageId);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/searech/product-storage-name")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_StorageNameContainingIgnoreCase(@RequestParam("storageName") String storageName){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_StorageNameContainingIgnoreCase(storageName);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-storage-location")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_StorageLocationContainingIgnoreCase(@RequestParam("storageLocation") String storageLocation){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_StorageLocationContainingIgnoreCase(storageLocation);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-storage-capacity")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_StorageCapacity(@RequestParam("quantity") BigDecimal capacity){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_StorageCapacity(capacity);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-storage-capacity-greater-than")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_StorageCapacityGreaterThan(@RequestParam("quantity") BigDecimal capacity){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_StorageCapacityGreaterThan(capacity);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-storage-capacity-less-than")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_StorageCapacityLessThan(@RequestParam("quantity") BigDecimal capacity){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_StorageCapacityLessThan(capacity);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/searc/product/supply/{supplyId}")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_SupplyId(@PathVariable Long supplyId){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_SupplyId(supplyId);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-supply-quantity")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_SupplyQuantity(@RequestParam("quantity") BigDecimal quantity){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_SupplyQuantity(quantity);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-supply-quantity-greater-than")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_SupplyQuantityGreaterThan(@RequestParam("quantity") BigDecimal quantity){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_SupplyQuantityGreaterThan(quantity);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-supply-quantity-less-than")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_SupplyQuantityLessThan(@RequestParam("quantity") BigDecimal quantity){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_SupplyQuantityLessThan(quantity);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-supply-updates")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_SupplyUpdates(@RequestParam("updates") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updates){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_SupplyUpdates(updates);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-supply-updates-between")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_SupplyUpdatesBetween(@RequestParam("updatesStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatesStart,
    		@RequestParam("updatesEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatesEnd){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_SupplyUpdatesBetween(updatesStart, updatesEnd);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product/shelf/{shelfId}")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_ShelfId(@PathVariable Long shelfId){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_ShelfId(shelfId);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-shelf-row-count")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_ShelfRowCount(@RequestParam("rowCount") Integer rowCount){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_ShelfRowCount(rowCount);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-shelf-cols")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_ShelfCols(@RequestParam("cols") Integer cols){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_ShelfCols(cols);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','PRODUCTION_PLANNER','QUALITY_MANAGER')")
    @GetMapping("/search/product-shelf-is-null")
    public ResponseEntity<List<ProductionOrderResponse>> findByProduct_ShelfIsNull(){
    	List<ProductionOrderResponse> response = productionOrderService.findByProduct_ShelfIsNull();
    	return ResponseEntity.ok(response);
    }
}
