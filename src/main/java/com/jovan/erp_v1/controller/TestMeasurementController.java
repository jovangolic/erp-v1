package com.jovan.erp_v1.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionType;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.Unit;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.TestMeasurementRequest;
import com.jovan.erp_v1.response.TestMeasurementResponse;
import com.jovan.erp_v1.service.ITestMeasurementService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/testMeasurements")
public class TestMeasurementController {

	private final ITestMeasurementService testMeasurementService;
	
	@PostMapping("/create/new-test-measurement")
	public ResponseEntity<TestMeasurementResponse> create(@Valid @RequestBody TestMeasurementRequest request){
		TestMeasurementResponse items = testMeasurementService.create(request);
		return ResponseEntity.ok(items);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<TestMeasurementResponse> update(@PathVariable Long id,@Valid @RequestBody TestMeasurementRequest request){
		TestMeasurementResponse items = testMeasurementService.update(id, request);
		return ResponseEntity.ok(items);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		testMeasurementService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/find-one/{id}")
	public ResponseEntity<TestMeasurementResponse> findOne(@PathVariable Long id){
		TestMeasurementResponse items = testMeasurementService.findOne(id);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/find-all")
	public ResponseEntity<List<TestMeasurementResponse>> findAll(){
		List<TestMeasurementResponse> items = testMeasurementService.findAll();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/exists")
    public ResponseEntity<Boolean> existsByWithinSpec(@RequestParam Boolean withinSpec) {
        boolean exists = testMeasurementService.existsByWithinSpec(withinSpec);
        return ResponseEntity.ok(exists);
    }
	
	@GetMapping("/search")
    public ResponseEntity<List<TestMeasurementResponse>> search(
            @RequestParam(required = false) Long inspectionId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) QualityCheckStatus status,
            @RequestParam(required = false) BigDecimal minMeasuredValue,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

       List<TestMeasurementResponse> items = testMeasurementService.search(inspectionId, productName, status, minMeasuredValue, startDate, endDate);
       return ResponseEntity.ok(items);
    }
	
	 @GetMapping("/deep-search")
	    public ResponseEntity<List<TestMeasurementResponse>> deepSearch(
	            @RequestParam(required = false) String productName,
	            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime supplyUpdatedAfter,
	            @RequestParam(required = false) InspectionResult result
	    ) {
	        List<TestMeasurementResponse> items = testMeasurementService.deepSearch(productName, supplyUpdatedAfter, result);
	        return ResponseEntity.ok(items);
	    }
	 
	 @GetMapping("/search-standard")
	    public ResponseEntity<List<TestMeasurementResponse>> searchTestMeasurements(
	            @RequestParam(required = false) String storageName,
	            @RequestParam(required = false) String storageLocation,
	            @RequestParam(required = false) BigDecimal storageCapacityMin,
	            @RequestParam(required = false) BigDecimal storageCapacityMax,
	            @RequestParam(required = false) StorageType storageType,
	            @RequestParam(required = false) StorageStatus storageStatus,
	            @RequestParam(required = false) BigDecimal supplyQuantityMin,
	            @RequestParam(required = false) BigDecimal supplyQuantityMax,
	            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime supplyUpdatesAfter,
	            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime supplyUpdatesBefore
	    ) {

	        List<TestMeasurementResponse> results = testMeasurementService.searchTestMeasurements(
	                storageName,
	                storageLocation,
	                storageCapacityMin,
	                storageCapacityMax,
	                storageType,
	                storageStatus,
	                supplyQuantityMin,
	                supplyQuantityMax,
	                supplyUpdatesAfter,
	                supplyUpdatesBefore
	        );
	        return ResponseEntity.ok(results);
	    }
	 
	 @GetMapping("/measured-value")
	 public ResponseEntity<List<TestMeasurementResponse>> findByMeasuredValue(@RequestParam("measuredValue") BigDecimal measuredValue){
		 List<TestMeasurementResponse> items = testMeasurementService.findByMeasuredValue(measuredValue);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/measured-value-greater-than")
	 public ResponseEntity<List<TestMeasurementResponse>> findByMeasuredValueGreaterThan(@RequestParam("measuredValue") BigDecimal measuredValue){
		 List<TestMeasurementResponse> items = testMeasurementService.findByMeasuredValueGreaterThan(measuredValue);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/measured-value-less-than")
	 public ResponseEntity<List<TestMeasurementResponse>> findByMeasuredValueLessThan(@RequestParam("measuredValue") BigDecimal measuredValue){
		 List<TestMeasurementResponse> items = testMeasurementService.findByMeasuredValueLessThan(measuredValue);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/inspection/{inspectionId}")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspectionId(@PathVariable Long inspectionId){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspectionId(inspectionId);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection-code")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_CodeContainingIgnoreCase(@RequestParam("code") String code){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_CodeContainingIgnoreCase(code);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection-type")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_Type(@RequestParam("type") InspectionType type){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_Type(type);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection-date")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_Date(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_Date(date);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection-date-after")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_DateAfter(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_DateAfter(date);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection-date-before")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_DateBefore(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_DateBefore(date);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection-date-between")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_DateBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			 @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_DateBetween(start, end);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/batch/{batchId}")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_BatchId(@PathVariable Long batchId){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_BatchId(batchId);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/product/{productId}")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_ProductId(@PathVariable Long productId){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_ProductId(productId);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/product-current-quantity")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_ProductCurrentQuantity(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_ProductCurrentQuantity(currentQuantity);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/product-current-quantity-greater-than")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_ProductCurrentQuantityGreaterThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_ProductCurrentQuantityGreaterThan(currentQuantity);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/product-current-quantity-less-than")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_ProductCurrentQuantityLessThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_ProductCurrentQuantityLessThan(currentQuantity);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/product-name")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_ProductNameContainingIgnoreCase(@RequestParam("productName") String productName){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_ProductNameContainingIgnoreCase(productName);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/product-unit-measure")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_ProductUnitMeasure(@RequestParam("unitMeasure") UnitMeasure unitMeasure){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_ProductUnitMeasure(unitMeasure);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/product-supplier-type")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_ProductSupplierType(@RequestParam("supplierType") SupplierType supplierType){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_ProductSupplierType(supplierType);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/product-storage-type")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_ProductStorageType(@RequestParam("storageType") StorageType storageType){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_ProductStorageType(storageType);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/product-goods-type")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_ProductGoodsType(@RequestParam("goodsType") GoodsType goodsType){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_ProductGoodsType(goodsType);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/inspector/{inspectorId}")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_InspectorId(@PathVariable Long inspectorId){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_InspectorId(inspectorId);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/product/storage/{storageId}")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_Product_StorageId(@PathVariable Long storageId){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_Product_StorageId(storageId);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/product/supply/{supplyId}")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_Product_SupplyId(@PathVariable Long supplyId){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_Product_SupplyId(supplyId);
	     return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/product/shelf/{shelfId}")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_Product_ShelfId(@PathVariable Long shelfId){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_Product_ShelfId(shelfId);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quantity-inspected")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QuantityInspected(@RequestParam("quantityInspected") BigDecimal quantityInspected){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QuantityInspected(quantityInspected);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quantity-inspected-greater-than")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QuantityInspectedGreaterThan(@RequestParam("quantityInspected") BigDecimal quantityInspected){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QuantityInspectedGreaterThan(quantityInspected);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quantity-inspected-less-than")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QuantityInspectedLessThan(@RequestParam("quantityInspected") BigDecimal quantityInspected){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QuantityInspectedLessThan(quantityInspected);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quantity-accepted")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QuantityAccepted(@RequestParam("quantityAccepted") BigDecimal quantityAccepted){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QuantityAccepted(quantityAccepted);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quantity-accepted-greater-than")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QuantityAcceptedGreaterThan(@RequestParam("quantityAccepted") BigDecimal quantityAccepted){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QuantityAcceptedGreaterThan(quantityAccepted);
	     return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quantity-accepted-less-than")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QuantityAcceptedLessThan(@RequestParam("quantityAccepted") BigDecimal quantityAccepted){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QuantityAcceptedLessThan(quantityAccepted);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quantity-rejected")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QuantityRejected(@RequestParam("quantityRejected") BigDecimal quantityRejected){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QuantityRejected(quantityRejected);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quantity-rejected-greater-than")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QuantityRejectedGreaterThan(@RequestParam("quantityRejected") BigDecimal quantityRejected){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QuantityRejectedGreaterThan(quantityRejected);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quantity-rejected-less-than")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QuantityRejectedLessThan(@RequestParam("quantityRejected") BigDecimal quantityRejected){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QuantityRejectedLessThan(quantityRejected);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection-notes")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_Notes(@RequestParam("notes") String notes){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_Notes(notes);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection-result")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_Result(@RequestParam("result") InspectionResult result){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_Result(result);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection-result-and-type")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_ResultAndType(@RequestParam("result") InspectionResult result,@RequestParam("type") InspectionType type){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_ResultAndType(result, type);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quality-check/{qualityCheckId}")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QualityCheckId(@PathVariable Long qualityCheckId){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QualityCheckId(qualityCheckId);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quality-check-date")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QualityCheck_LocDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QualityCheck_LocDate(date);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quality-check-date-after")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QualityCheck_LocDateAfter(@RequestParam("dste") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QualityCheck_LocDateAfter(date);
	     return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quality-check-date-before")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QualityCheck_LocDateBefore(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QualityCheck_LocDateBefore(date);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quality-check-date-between")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QualityCheck_LocDateBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			 @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QualityCheck_LocDateBetween(start, end);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quality-check-notes")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QualityCheck_Notes(@RequestParam("notes") String notes){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QualityCheck_Notes(notes);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quality-check-reference-type")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QualityCheck_ReferenceType(@RequestParam("referenceType") ReferenceType referenceType){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QualityCheck_ReferenceType(referenceType);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quality-check-type")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QualityCheck_CheckType(@RequestParam("checkType") QualityCheckType checkType){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QualityCheck_CheckType(checkType);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quality-check-status")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QualityCheck_Status(@RequestParam("status") QualityCheckStatus status){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QualityCheck_Status(status);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quality-check-reference-id")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QualityCheck_ReferenceId(@RequestParam("referenceId") Long referenceId){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QualityCheck_ReferenceId(referenceId);
	     return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quality-check-status-and-check-type")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QualityCheck_StatusAndCheckType(@RequestParam("status") QualityCheckStatus status,
			 @RequestParam("") QualityCheckType checkType){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QualityCheck_StatusAndCheckType(status, checkType);
	     return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/inspection/quality-check-reference-type-notes")
	 public ResponseEntity<List<TestMeasurementResponse>> findByInspection_QualityCheck_ReferenceType_Notes(@RequestParam("referenceType") ReferenceType referenceType,
			 @RequestParam("notes") String notes){
		 List<TestMeasurementResponse> items = testMeasurementService.findByInspection_QualityCheck_ReferenceType_Notes(referenceType, notes);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/{qualityStandardId}")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_Id(@PathVariable Long qualityStandardId){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_Id(qualityStandardId);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard-description")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_Description(@RequestParam("description") String description){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_Description(description);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/min-value")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_MinValue(@RequestParam("minValue") BigDecimal minValue){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_MinValue(minValue);
	     return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/min-value-greater-than")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_MinValueGreaterThan(@RequestParam("minValue") BigDecimal minValue){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_MinValueGreaterThan(minValue);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/min-value-less-than")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_MinValueLessThan(@RequestParam("minValue") BigDecimal minValue){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_MinValueLessThan(minValue);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/max-value")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_MaxValue(@RequestParam("maxValue") BigDecimal maxValue){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_MaxValue(maxValue);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/max-value-greater-than")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_MaxValueGreaterThan(@RequestParam("maxValue") BigDecimal maxValue){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_MaxValueGreaterThan(maxValue);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/max-value-less-than")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_MaxValueLessThan(@RequestParam("maxValue") BigDecimal maxValue){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_MaxValueLessThan(maxValue);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard-unit")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_Unit(@RequestParam("unit") Unit unit){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_Unit(unit);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/product/{productId}")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_Product_Id(@PathVariable Long productId){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_Product_Id(productId);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/product-current-quantity")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_Product_CurrentQuantity (@RequestParam("currentQuantity") BigDecimal currentQuantity){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_Product_CurrentQuantity(currentQuantity);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/product-current-quantity-greater-than")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_Product_CurrentQuantityGreaterThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_Product_CurrentQuantityGreaterThan(currentQuantity);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/product-current-quantity-less-than")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_Product_CurrentQuantityLessThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_Product_CurrentQuantityLessThan(currentQuantity);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/product-name")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_Product_NameContainingIgnoreCase(@RequestParam("productName") String productName){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_Product_NameContainingIgnoreCase(productName);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/product-unit-measure")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_Product_UnitMeasure(@RequestParam("unitMeasure") UnitMeasure unitMeasure){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_Product_UnitMeasure(unitMeasure);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/product-supplier-type")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_Product_SupplierType(@RequestParam("supplierType") SupplierType supplierType){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_Product_SupplierType(supplierType);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/product-storage-type")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_Product_StorageType(@RequestParam("storageType") StorageType storageType){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_Product_StorageType(storageType);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/product-goods-type")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_Product_GoodsType(@RequestParam("goodsType") GoodsType goodsType){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_Product_GoodsType(goodsType);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/product/storage/{storageId}")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_Product_StorageId(@PathVariable Long storageId){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_Product_StorageId(storageId);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/product/storage-shelves-is-null")
	 public ResponseEntity<TestMeasurementResponse>  findByStandard_Product_StorageHasShelvesForIsNull(){
		 TestMeasurementResponse items = testMeasurementService.findByStandard_Product_StorageHasShelvesForIsNull();
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/product/supply/{supplyId}")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_Product_SupplyId(@PathVariable Long supplyId){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_Product_SupplyId(supplyId);
		 return ResponseEntity.ok(items);
	 }
	 
	 @GetMapping("/search/standard/product/shelf/{shelfId}")
	 public ResponseEntity<List<TestMeasurementResponse>> findByStandard_Product_ShelfId(@PathVariable Long shelfId){
		 List<TestMeasurementResponse> items = testMeasurementService.findByStandard_Product_ShelfId(shelfId);
		 return ResponseEntity.ok(items);
	 }
	 
}
