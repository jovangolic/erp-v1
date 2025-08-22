package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.Unit;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.QualityStandardRequest;
import com.jovan.erp_v1.response.QualityStandardResponse;
import com.jovan.erp_v1.service.IQualityStandardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qualityStandards")
public class QualityStandardController {

	private final IQualityStandardService qualityStandardService;
	
	@PostMapping("/create/new-quality-standard")
	public ResponseEntity<QualityStandardResponse> create(@Valid @RequestBody QualityStandardRequest request){
		QualityStandardResponse items = qualityStandardService.create(request);
		return ResponseEntity.ok(items);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<QualityStandardResponse> update(@PathVariable Long id, @Valid @RequestBody QualityStandardRequest request){
		QualityStandardResponse items = qualityStandardService.update(id, request);
		return ResponseEntity.ok(items);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		qualityStandardService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/find-one/{id}")
	public ResponseEntity<QualityStandardResponse> findOne(@PathVariable Long id){
		QualityStandardResponse items = qualityStandardService.findOne(id);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/find-all")
	public ResponseEntity<List<QualityStandardResponse>> findAll(){
		List<QualityStandardResponse> items = qualityStandardService.findAll();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search-standards")
	public ResponseEntity<List<QualityStandardResponse>> search(
	        @RequestParam(required = false) Long supplyId,
	        @RequestParam(required = false) BigDecimal productStorageMin,
	        @RequestParam(required = false) BigDecimal productStorageMax,
	        @RequestParam(required = false) BigDecimal supplyMin,
	        @RequestParam(required = false) BigDecimal supplyMax,
	        @RequestParam(required = false) Integer shelfRow,
	        @RequestParam(required = false) Integer shelfCol
	) {
	    List<QualityStandardResponse> items =  qualityStandardService.searchQualityStandards(
	            supplyId, productStorageMin, productStorageMax, supplyMin, supplyMax, shelfRow, shelfCol
	    );
	    return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-unit")
	public ResponseEntity<List<QualityStandardResponse>> findByUnit(@RequestParam("unit") Unit unit){
		List<QualityStandardResponse> items = qualityStandardService.findByUnit(unit);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/description")
	public ResponseEntity<List<QualityStandardResponse>> findByDescriptionContainingIgnoreCase(@RequestParam("description") String description){
		List<QualityStandardResponse> items = qualityStandardService.findByDescriptionContainingIgnoreCase(description);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/min-value")
	public ResponseEntity<List<QualityStandardResponse>> findByMinValue(@RequestParam("minValue") BigDecimal minValue){
		List<QualityStandardResponse> items = qualityStandardService.findByMinValue(minValue);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/min-value-greater-than")
	public ResponseEntity<List<QualityStandardResponse>> findByMinValueGreaterThan(@RequestParam("minValue") BigDecimal minValue){
		List<QualityStandardResponse> items = qualityStandardService.findByMinValueLessThan(minValue);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/min-value-less-than")
	public ResponseEntity<List<QualityStandardResponse>> findByMinValueLessThan(@RequestParam("minValue") BigDecimal minValue){
		List<QualityStandardResponse> items = qualityStandardService.findByMinValueLessThan(minValue);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/min-value-between")
	public ResponseEntity<List<QualityStandardResponse>> findByMinValueBetween(@RequestParam("min") BigDecimal min,@RequestParam("max")  BigDecimal max){
		List<QualityStandardResponse> items = qualityStandardService.findByMinValueBetween(min, max);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/max-value")
	public ResponseEntity<List<QualityStandardResponse>> findByMaxValue(@RequestParam("maxValue") BigDecimal maxValue){
		List<QualityStandardResponse> items = qualityStandardService.findByMaxValue(maxValue);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/max-value-greater-than")
	public ResponseEntity<List<QualityStandardResponse>> findByMaxValueGreaterThan(@RequestParam("maxValue") BigDecimal maxValue){
		List<QualityStandardResponse> items = qualityStandardService.findByMaxValueGreaterThan(maxValue);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/max-value-less-than")
	public ResponseEntity<List<QualityStandardResponse>> findByMaxValueLessThan(@RequestParam("maxValue") BigDecimal maxValue){
		List<QualityStandardResponse> items = qualityStandardService.findByMaxValueLessThan(maxValue);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/max-value-between")
	public ResponseEntity<List<QualityStandardResponse>> findByMaxValueBetween(@RequestParam("min") BigDecimal min,@RequestParam("max")  BigDecimal max){
		List<QualityStandardResponse> items = qualityStandardService.findByMaxValueBetween(min, max);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/count/by-min-value-not-null")
	public ResponseEntity<Long> countByMinValueIsNotNull(){
		Long items = qualityStandardService.countByMinValueIsNotNull();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/count/by-max-value-not-null")
	public ResponseEntity<Long> countByMaxValueIsNotNull(){
		Long items = qualityStandardService.countByMaxValueIsNotNull();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/count-min-value")
	public ResponseEntity<Long>countByMinValue(@RequestParam("minValue") BigDecimal minValue){
		Long items = qualityStandardService.countByMinValue(minValue);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/count-max-value")
	public ResponseEntity<Long> countByMaxValue(@RequestParam("maxValue") BigDecimal maxValue){
		Long items = qualityStandardService.countByMaxValue(maxValue);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/{productId}")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_Id(@PathVariable Long productId){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_Id(productId);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product-current-quantity")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_CurrentQuantity(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_CurrentQuantity(currentQuantity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product-current-quantity-greater-than")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_CurrentQuantityGreaterThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_CurrentQuantityGreaterThan(currentQuantity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product-current-quantity-less-than")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_CurrentQuantityLessThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_CurrentQuantityLessThan(currentQuantity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product-current-quantity-between")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_CurrentQuantityBetween(@RequestParam("min") BigDecimal min,@RequestParam("max")  BigDecimal max){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_CurrentQuantityBetween(min, max);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product-name")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_NameContainingIgnoreCase(@RequestParam("productName") String productName){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_NameContainingIgnoreCase(productName);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product-unit-measure")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_UnitMeasure(@RequestParam("unitMeasure") UnitMeasure unitMeasure){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_UnitMeasure(unitMeasure);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product-supplier-type")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_SupplierType(@RequestParam("supplierType") SupplierType supplierType){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_SupplierType(supplierType);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product-storage-type")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_StorageType(@RequestParam("storageType") StorageType storageType){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_StorageType(storageType);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product-goods-type")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_GoodsType(@RequestParam("goodsType") GoodsType goodsType){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_GoodsType(goodsType);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/storage/{storageId}")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_StorageId(@PathVariable Long storageId){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_StorageId(storageId);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product-storage-name")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_StorageNameContainingIgnoreCase(@RequestParam("storageName")  String storageName){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_StorageNameContainingIgnoreCase(storageName);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product-storage-location")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_StorageLocationContainingIgnoreCase(@RequestParam("storageLocation")  String storageLocation){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_StorageLocationContainingIgnoreCase(storageLocation);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/storage-capacity")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_StorageCapacity(@RequestParam("capacity")  BigDecimal capacity){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_StorageCapacity(capacity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/storage-capacity-greater-than")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_StorageCapacityGreaterThan(@RequestParam("capacity")  BigDecimal capacity){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_StorageCapacityGreaterThan(capacity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/storage-capacity-less-than")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_StorageCapacityLessThan(@RequestParam("capacity")  BigDecimal capacity){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_StorageCapacityLessThan(capacity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/storage-capacity-between")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_StorageCapacityBetween(@RequestParam("min")  BigDecimal min,@RequestParam("max")  BigDecimal max){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_StorageCapacityBetween(min, max);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/storage-capacity-between-status")
	public ResponseEntity<List<QualityStandardResponse>> findByProductStorageCapacityBetweenAndStatus(@RequestParam("")  BigDecimal min,
			@RequestParam("max") BigDecimal max,@RequestParam("status") StorageStatus status){
		List<QualityStandardResponse> items = qualityStandardService.findByProductStorageCapacityBetweenAndStatus(min, max, status);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/storage-capacity-status-type")
	public ResponseEntity<List<QualityStandardResponse>> findByProductStorageCapacityStatusAndType(@RequestParam("min") BigDecimal min,
			@RequestParam("max") BigDecimal max,@RequestParam("status")  StorageStatus status,@RequestParam("type") StorageType type){
		List<QualityStandardResponse> items = qualityStandardService.findByProductStorageCapacityStatusAndType(min, max, status, type);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/storage-name-capacity")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_StorageNameContainingIgnoreCaseAndCapacity(@RequestParam("storageName")  String storageName,
			@RequestParam("capacity")   BigDecimal capacity){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_StorageNameContainingIgnoreCaseAndCapacity(storageName, capacity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/storage-name-capacity-greater-than")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_StorageNameContainingIgnoreCaseAndCapacityGreaterThan(@RequestParam("storageName")  String storageName, 
			@RequestParam("capacity") BigDecimal capacity){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_StorageNameContainingIgnoreCaseAndCapacityGreaterThan(storageName, capacity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/storage-name-capacity-less-than")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_StorageNameContainingIgnoreCaseAndCapacityLessThan(@RequestParam("storageName")  String storageName,
			@RequestParam("")   BigDecimal capacity){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_StorageNameContainingIgnoreCaseAndCapacityLessThan(storageName, capacity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/storage-name-capacity-between")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_StorageNameContainingIgnoreCaseAndCapacityBetween(@RequestParam("storageName")  String storageName,
			@RequestParam("min") BigDecimal min,@RequestParam("max")  BigDecimal max){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_StorageNameContainingIgnoreCaseAndCapacityBetween(storageName, min, max);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/storage-location-capacity")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_StorageLocationContainingIgnoreCaseAndCapacity(@RequestParam("storageLocation")  String storageLocation,
			@RequestParam("capacity") BigDecimal capacity){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_StorageLocationContainingIgnoreCaseAndCapacity(storageLocation, capacity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/storage-location-capacity-greater-than")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_StorageLocationContainingIgnoreCaseAndCapacityGreaterThan(@RequestParam("storageLocation")  String storageLocation,
			@RequestParam("capacity") BigDecimal capacity){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_StorageLocationContainingIgnoreCaseAndCapacityGreaterThan(storageLocation, capacity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/storage-location-capacity-less-than")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_StorageLocationContainingIgnoreCaseAndCapacityLessThan(@RequestParam("storageLocation")  String storageLocation,
			@RequestParam("capacity") BigDecimal capacity){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_StorageLocationContainingIgnoreCaseAndCapacityLessThan(storageLocation, capacity);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/storage-location-capacity-between")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_StorageLocationContainingIgnoreCaseAndCapacityBetween(@RequestParam("storageLocation")  String storageLocation,
			@RequestParam("min") BigDecimal min,@RequestParam("max")  BigDecimal max){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_StorageLocationContainingIgnoreCaseAndCapacityBetween(storageLocation, min, max);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/storage-has-shelves-is-null")
	public ResponseEntity<QualityStandardResponse> findByProduct_StorageHasShelvesForIsNull(){
		QualityStandardResponse items = qualityStandardService.findByProduct_StorageHasShelvesForIsNull();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/supply/{supplyId}")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_SupplyId(@PathVariable Long supplyId){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_SupplyId(supplyId);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/shelf/{shelfId}")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_ShelfId(@PathVariable Long shelfId){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_ShelfId(shelfId);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/shelf-row-count")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_ShelfRowCount(@RequestParam("rowCount")  Integer rowCount){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_ShelfRowCount(rowCount);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/shelf-cols")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_ShelfCols(@RequestParam("cols")  Integer cols){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_ShelfCols(cols);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/shelf-row-col")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_ShelfRowAndColNullable(@RequestParam("row")  Integer row,@RequestParam("col")  Integer col){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_ShelfRowAndColNullable(row, col);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/product/shelf-row-and-col-between")
	public ResponseEntity<List<QualityStandardResponse>> findByProduct_ShelfRowAndColBetweenNullable(@RequestParam("rowMin") Integer rowMin,@RequestParam("rowMax")  Integer rowMax,
			@RequestParam("colMin") Integer colMin,@RequestParam("colMax")   Integer colMax){
		List<QualityStandardResponse> items = qualityStandardService.findByProduct_ShelfRowAndColBetweenNullable(rowMin, rowMax, colMin, colMax);
		return ResponseEntity.ok(items);
	}
}
