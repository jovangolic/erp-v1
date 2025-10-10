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

import com.jovan.erp_v1.enumeration.BatchStatus;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.BatchRequest;
import com.jovan.erp_v1.response.BatchResponse;
import com.jovan.erp_v1.save_as.BatchSaveAsRequest;
import com.jovan.erp_v1.search_request.BatchSearchRequest;
import com.jovan.erp_v1.service.IBatchService;
import com.jovan.erp_v1.statistics.batch.BatchConfirmedStatDTO;
import com.jovan.erp_v1.statistics.batch.BatchMonthlyStatDTO;
import com.jovan.erp_v1.statistics.batch.BatchStatusStatDTO;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/batches")
@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
public class BatchController {

	private final IBatchService batchService;
	
	@PreAuthorize(RoleGroups.BATCH_FULL_ACCESS)
	@PostMapping("/create/new-batch")
	public ResponseEntity<BatchResponse> create(@Valid @RequestBody BatchRequest request){
		BatchResponse items = batchService.create(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BATCH_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<BatchResponse> update(@PathVariable Long id, @Valid @RequestBody BatchRequest request){
		BatchResponse items = batchService.update(id, request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BATCH_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		batchService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
	@GetMapping("/find-one/{id}")
	public ResponseEntity<BatchResponse> findOne(@PathVariable Long id){
		BatchResponse items = batchService.findOne(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
	@GetMapping("/find-all")
	public ResponseEntity<List<BatchResponse>> findAll(){
		List<BatchResponse> items = batchService.findAll();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
	@GetMapping("/expired")
    public ResponseEntity<List<BatchResponse>> getExpiredBatches() {
		List<BatchResponse> items = batchService.getExpiredBatches();
		return ResponseEntity.ok(items);
    }

	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/active")
    public ResponseEntity<List<BatchResponse>> getActiveBatches() {
		List<BatchResponse> items = batchService.getActiveBatches();
		return ResponseEntity.ok(items);
    }

	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/upcoming/{daysAhead}")
    public ResponseEntity<List<BatchResponse>> getUpcomingBatches(@PathVariable Integer daysAhead) {
		List<BatchResponse> items = batchService.getUpcomingBatches(daysAhead);
		return ResponseEntity.ok(items);
    }

	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/produced-between")
    public ResponseEntity<List<BatchResponse>> getBatchesProducedBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
		List<BatchResponse> items = batchService.getBatchesProducedBetween(start, end);
		return ResponseEntity.ok(items);
    }

	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/expiring-between")
    public ResponseEntity<List<BatchResponse>> getBatchesExpiringBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
		List<BatchResponse> items = batchService.getBatchesExpiringBetween(start, end);
		return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/exists-by-code")
    public ResponseEntity<Boolean> existsByCode(@RequestParam("code") String code){
    	Boolean items = batchService.existsByCode(code);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/by-code")
    public ResponseEntity<List<BatchResponse>> findByCodeContainingIgnoreCase(@RequestParam("code") String code){
    	List<BatchResponse> items = batchService.findByCodeContainingIgnoreCase(code);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/by-quantity-produced")
    public ResponseEntity<List<BatchResponse>> findByQuantityProduced(@RequestParam("quantityProduced") Integer quantityProduced){
    	List<BatchResponse> items = batchService.findByQuantityProduced(quantityProduced);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/by-quantity-produced-greater-than")
    public ResponseEntity<List<BatchResponse>> findByQuantityProducedGreaterThan(@RequestParam("quantityProduced") Integer quantityProduced){
    	List<BatchResponse> items = batchService.findByQuantityProducedGreaterThan(quantityProduced);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/by-quantity-produced-less-than")
    public ResponseEntity<List<BatchResponse>> findByQuantityProducedLessThan(@RequestParam("quantityProduced") Integer quantityProduced){
    	List<BatchResponse> items = batchService.findByQuantityProducedLessThan(quantityProduced);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/by-production-date")
    public ResponseEntity<List<BatchResponse>> findByProductionDate(@RequestParam("productionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate productionDate){
    	List<BatchResponse> items = batchService.findByProductionDate(productionDate);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/by-production-date-before")
    public ResponseEntity<List<BatchResponse>> findByProductionDateBefore(@RequestParam("productionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate productionDate){
    	List<BatchResponse> items = batchService.findByProductionDateBefore(productionDate);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/by-production-date-after")
    public ResponseEntity<List<BatchResponse>> findByProductionDateAfter(@RequestParam("productionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate productionDate){
    	List<BatchResponse> items = batchService.findByProductionDateAfter(productionDate);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/by-production-date-between")
    public ResponseEntity<List<BatchResponse>> findByProductionDateBetween(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
    		@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
    	List<BatchResponse> items = batchService.findByProductionDateBetween(startDate, endDate);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/by-expiry-date")
    public ResponseEntity<List<BatchResponse>> findByExpiryDate(@RequestParam("expiryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate){
    	List<BatchResponse> items = batchService.findByExpiryDate(expiryDate);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/by-expiry-date-before")
    public ResponseEntity<List<BatchResponse>> findByExpiryDateBefore(@RequestParam("expiryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate){
    	List<BatchResponse> items = batchService.findByExpiryDateBefore(expiryDate);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/by-expiry-date-after")
    public ResponseEntity<List<BatchResponse>> findByExpiryDateAfter(@RequestParam("expiryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate){
    	List<BatchResponse> items = batchService.findByExpiryDateAfter(expiryDate);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/by-expiry-date-between")
    public ResponseEntity<List<BatchResponse>> findByExpiryDateBetween(@RequestParam("expiryDateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDateStart,
    		@RequestParam("expiryDateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDateEnd){
    	List<BatchResponse> items = batchService.findByExpiryDateBetween(expiryDateStart, expiryDateEnd);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/production-date-equal")
    public ResponseEntity<List<BatchResponse>> findByProductionDateEquals(@RequestParam("today") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate today){
    	List<BatchResponse> items = batchService.findByProductionDateEquals(today);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/expiry-date-less-than-equal")
    public ResponseEntity<List<BatchResponse>> findByExpiryDateLessThanEqual(@RequestParam("today") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate today){
    	List<BatchResponse> items = batchService.findByExpiryDateLessThanEqual(today);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/production-date-greater-than-equal")
    public ResponseEntity<List<BatchResponse>> findByProductionDateGreaterThanEqual(@RequestParam("today") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate today){
    	List<BatchResponse> items = batchService.findByProductionDateGreaterThanEqual(today);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/expiry-date-greater-than-equal")
    public ResponseEntity<List<BatchResponse>> findByExpiryDateGreaterThanEqual(@RequestParam("expiryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate){
    	List<BatchResponse> items = batchService.findByExpiryDateGreaterThanEqual(expiryDate);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/production-date-less-than-equal")
    public ResponseEntity<List<BatchResponse>> findByProductionDateLessThanEqual(@RequestParam("productionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate productionDate){
    	List<BatchResponse> items = batchService.findByProductionDateLessThanEqual(productionDate);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/expiry-date-greater-than")
    public ResponseEntity<List<BatchResponse>> findByExpiryDateGreaterThan(@RequestParam("today") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate today){
    	List<BatchResponse> items = batchService.findByExpiryDateGreaterThan(today);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/expiry-date-not-null")
    public ResponseEntity<List<BatchResponse>> findByExpiryDateIsNotNull(){
    	List<BatchResponse> items = batchService.findByExpiryDateIsNotNull();
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/production-date-is-null")
    public ResponseEntity<List<BatchResponse>> findByProductionDateIsNull(){
    	List<BatchResponse> items = batchService.findByProductionDateIsNull();
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/production-date-not-null")
    public ResponseEntity<List<BatchResponse>> findByProductionDateIsNotNull(){
    	List<BatchResponse> items = batchService.findByProductionDateIsNotNull();
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/expiry-date-null")
    public ResponseEntity<List<BatchResponse>> findByExpiryDateIsNull(){
    	List<BatchResponse> items = batchService.findByExpiryDateIsNull();
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/expiry-date-after/product/{productId}")
    public ResponseEntity<List<BatchResponse>> findByExpiryDateAfterAndProductId(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
    		@PathVariable Long productId){
    	List<BatchResponse> items = batchService.findByExpiryDateAfterAndProductId(date, productId);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<BatchResponse>> findByProductId(@PathVariable Long productId){
    	List<BatchResponse> items = batchService.findByProductId(productId);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product-current-quantity")
    public ResponseEntity<List<BatchResponse>> findByProductCurrentQuantity(@RequestParam("currentQuantity") BigDecimal currentQuantity){
    	List<BatchResponse> items = batchService.findByProductCurrentQuantity(currentQuantity);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product-current-quantity-greater-than")
    public ResponseEntity<List<BatchResponse>> findByProductCurrentQuantityGreaterThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
    	List<BatchResponse> items = batchService.findByProductCurrentQuantityGreaterThan(currentQuantity);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product-current-quantity-less-than")
    public ResponseEntity<List<BatchResponse>> findByProductCurrentQuantityLessThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
    	List<BatchResponse> items = batchService.findByProductCurrentQuantityLessThan(currentQuantity);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product-current-quantity-between")
    public ResponseEntity<List<BatchResponse>> findByProductCurrentQuantityBetween(@RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max){
    	List<BatchResponse> items = batchService.findByProductCurrentQuantityBetween(min, max);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product/{productId}/expiry-date-less-than-equal")
    public ResponseEntity<List<BatchResponse>> findByProductIdAndExpiryDateLessThanEqual(@PathVariable Long productId,@RequestParam("today") LocalDate today){
    	List<BatchResponse> items = batchService.findByProductIdAndExpiryDateLessThanEqual(productId, today);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product/{productId}/product-date-after")
    public ResponseEntity<List<BatchResponse>> findByProductIdAndProductionDateAfter(@PathVariable Long productId,@RequestParam("date") LocalDate date){
    	List<BatchResponse> items = batchService.findByProductIdAndProductionDateAfter(productId, date);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product/{productId}/expiry-date-between")
    public ResponseEntity<List<BatchResponse>> findByProductIdAndExpiryDateBetween(@PathVariable Long productId,
    		@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
    		@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
    	List<BatchResponse> items = batchService.findByProductIdAndExpiryDateBetween(productId, startDate, endDate);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product-name")
    public ResponseEntity<List<BatchResponse>> findByProduct_NameContainingIgnoreCase(@RequestParam("productName") String productName){
    	List<BatchResponse> items = batchService.findByProduct_NameContainingIgnoreCase(productName);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product-unit-measure")
    public ResponseEntity<List<BatchResponse>> findByProduct_UnitMeasure(@RequestParam("unitMeasure") UnitMeasure unitMeasure){
    	List<BatchResponse> items = batchService.findByProduct_UnitMeasure(unitMeasure);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product-supplier-type")
    public ResponseEntity<List<BatchResponse>> findByProduct_SupplierType(@RequestParam("supplierType") SupplierType supplierType){
    	List<BatchResponse> items = batchService.findByProduct_SupplierType(supplierType);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product-storage-type")
    public ResponseEntity<List<BatchResponse>> findByProduct_StorageType(@RequestParam("storageType") StorageType storageType){
    	List<BatchResponse> items = batchService.findByProduct_StorageType(storageType);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product-goods-type")
    public ResponseEntity<List<BatchResponse>> findByProduct_GoodsType(@RequestParam("goodsType") GoodsType goodsType){
    	List<BatchResponse> items = batchService.findByProduct_GoodsType(goodsType);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product/storage/{storageId}")
    public ResponseEntity<List<BatchResponse>> findByProduct_StorageId(@PathVariable Long storageId){
    	List<BatchResponse> items = batchService.findByProduct_StorageId(storageId);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product-storage-name")
    public ResponseEntity<List<BatchResponse>> findByProduct_StorageNameContainingIgnoreCase(@RequestParam("storageName") String storageName){
    	List<BatchResponse> items = batchService.findByProduct_StorageNameContainingIgnoreCase(storageName);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product-storage-location")
    public ResponseEntity<List<BatchResponse>> findByProduct_StorageLocationContainingIgnoreCase(@RequestParam("storageLocation") String storageLocation){
    	List<BatchResponse> items = batchService.findByProduct_StorageLocationContainingIgnoreCase(storageLocation);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product-storage-capacity")
    public ResponseEntity<List<BatchResponse>> findByProduct_StorageCapacity(@RequestParam("capacity") BigDecimal capacity){
    	List<BatchResponse> items = batchService.findByProduct_StorageCapacity(capacity);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product-storage-capacity-greater-than")
    public ResponseEntity<List<BatchResponse>> findByProduct_StorageCapacityGreaterThan(@RequestParam("capacity") BigDecimal capacity){
    	List<BatchResponse> items = batchService.findByProduct_StorageCapacityGreaterThan(capacity);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product-storage-capacity-less-than")
    public ResponseEntity<List<BatchResponse>> findByProduct_StorageCapacityLessThan(@RequestParam("capacity") BigDecimal capacity){
    	List<BatchResponse> items = batchService.findByProduct_StorageCapacityLessThan(capacity);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product-storage-status")
    public ResponseEntity<List<BatchResponse>> findByProduct_StorageStatus(@RequestParam("status") StorageStatus status){
    	List<BatchResponse> items = batchService.findByProduct_StorageStatus(status);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product/storage-shelves-is-null")
    public ResponseEntity<List<BatchResponse>> findByProduct_StoragehasShelvesForIsNull(){
    	List<BatchResponse> items = batchService.findByProduct_StoragehasShelvesForIsNull();
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product/supply{supplyId}")
    public ResponseEntity<List<BatchResponse>> findByProduct_SupplyId(@PathVariable Long supplyId){
    	List<BatchResponse> items = batchService.findByProduct_SupplyId(supplyId);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product/shelf/{shelfId}")
    public ResponseEntity<List<BatchResponse>> findByProduct_ShelfId(@PathVariable Long shelfId){
    	List<BatchResponse> items = batchService.findByProduct_ShelfId(shelfId);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product/shelf-row-count")
    public ResponseEntity<List<BatchResponse>> findByProduct_ShelfRowCount(@RequestParam("rowCount") Integer rowCount){
    	List<BatchResponse> items = batchService.findByProduct_ShelfRowCount(rowCount);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/product/shelf-cols")
    public ResponseEntity<List<BatchResponse>> findByProduct_ShelfCols(@RequestParam("cols") Integer cols){
    	List<BatchResponse> items = batchService.findByProduct_ShelfCols(cols);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/exists/shelf/rows/storage/{storageId}")
    public ResponseEntity<Boolean> existsByRowCountAndStorageId(@RequestParam("rows") Integer rows,@PathVariable Long storageId){
    	Boolean items = batchService.existsByRowCountAndStorageId(rows, storageId);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/exists/shelf/cols/storage/{storageId}")
    public ResponseEntity<Boolean> existsByColsAndStorageId(@RequestParam("cols") Integer cols,@PathVariable Long storageId){
    	Boolean items = batchService.existsByColsAndStorageId(cols, storageId);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/exists/shelf/rows-cols/storage/{storageId}")
    public ResponseEntity<Boolean> existsByRowCountAndColsAndStorageId(@RequestParam("rows") Integer rows,@RequestParam("cols") Integer cols,@PathVariable Long storageId){
    	Boolean items = batchService.existsByRowCountAndColsAndStorageId(rows, cols, storageId);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/row-and-cols/storage/{storageId}")
    public ResponseEntity<BatchResponse> findByRowCountAndColsAndStorageId(@RequestParam("rows") Integer rows,@RequestParam("cols") Integer cols,@PathVariable Long storageId){
    	BatchResponse items = batchService.findByRowCountAndColsAndStorageId(rows, cols, storageId);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/shelf-row/storage/{storageId}")
    public ResponseEntity<List<BatchResponse>> findByRowCountAndStorageId(@RequestParam("rows") Integer rows,@PathVariable Long storageId){
    	List<BatchResponse> items = batchService.findByRowCountAndStorageId(rows, storageId);
    	return ResponseEntity.ok(items);
    }
    
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
    @GetMapping("/search/shelf-cols/storage/{storageId}")
    public ResponseEntity<List<BatchResponse>> findByColsAndStorageId(@RequestParam("cols") Integer cols,@PathVariable Long storageId){
    	List<BatchResponse> items = batchService.findByColsAndStorageId(cols, storageId);
    	return ResponseEntity.ok(items);
    }
    
	//nove metode
	
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
	@GetMapping("/count/batches-status")
	public ResponseEntity<List<BatchStatusStatDTO>> countBatchesByStatus(){
		List<BatchStatusStatDTO> items = batchService.countBatchesByStatus();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
	@GetMapping("/count/batches-confirmed")
	public ResponseEntity<List<BatchConfirmedStatDTO>> countBatchesByConfirmed(){
		List<BatchConfirmedStatDTO> items = batchService.countBatchesByConfirmed();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
	@GetMapping("/count/batches-year-and-month")
	public ResponseEntity<List<BatchMonthlyStatDTO>> countBatchesByYearAndMonth(){
		List<BatchMonthlyStatDTO> items = batchService.countBatchesByYearAndMonth();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BATCH_READ_ACCESS)
	@GetMapping("/track/{id}")
	public ResponseEntity<BatchResponse> trackBatch(@PathVariable Long id){
		BatchResponse items = batchService.trackBatch(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BATCH_FULL_ACCESS)
	@PostMapping("/{id}/confirm")
	public ResponseEntity<BatchResponse> confirmBatch(@PathVariable Long id){
		BatchResponse items = batchService.confirmBatch(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BATCH_FULL_ACCESS)
	@PostMapping("/{id}/close")
	public ResponseEntity<BatchResponse> closeBatch(@PathVariable Long id){
		BatchResponse items = batchService.closeBatch(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BATCH_FULL_ACCESS)
	@PostMapping("/{id}/cancel")
	public ResponseEntity<BatchResponse> cancelBatch(@PathVariable Long id){
		BatchResponse items = batchService.cancelBatch(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BATCH_FULL_ACCESS)
	@PostMapping("/{id}/status/{status}")
	public ResponseEntity<BatchResponse> changeStatus(@PathVariable Long id,@PathVariable  BatchStatus status){
		BatchResponse items = batchService.changeStatus(id, status);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BATCH_FULL_ACCESS)
	@PostMapping("/save")
	public ResponseEntity<BatchResponse> saveBatch(@Valid @RequestBody BatchRequest request){
		BatchResponse items = batchService.saveBatch(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BATCH_FULL_ACCESS)
	@PostMapping("/save-as")
	public ResponseEntity<BatchResponse> saveAs(@Valid @RequestBody BatchSaveAsRequest request){
		BatchResponse items = batchService.saveAs(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BATCH_FULL_ACCESS)
	@PostMapping("/save-all")
	public ResponseEntity<List<BatchResponse>> saveAll(@Valid @RequestBody List<BatchRequest> requests){
		List<BatchResponse> items = batchService.saveAll(requests);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.BATCH_FULL_ACCESS)
	@PostMapping("/general-search")
	public ResponseEntity<List<BatchResponse>> generalSearch(@RequestBody BatchSearchRequest request){
		List<BatchResponse> items = batchService.generalSearch(request);
		return ResponseEntity.ok(items);
	}
}
