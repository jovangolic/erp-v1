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

import com.jovan.erp_v1.dto.InspectionQuantityAcceptedDTO;
import com.jovan.erp_v1.dto.InspectionQuantityAcceptedSummaryDTO;
import com.jovan.erp_v1.dto.InspectionQuantityInspectedDTO;
import com.jovan.erp_v1.dto.InspectionQuantityInspectedSummaryDTO;
import com.jovan.erp_v1.dto.InspectionQuantityRejectedDTO;
import com.jovan.erp_v1.dto.InspectionQuantityRejectedSummaryDTO;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionStatus;
import com.jovan.erp_v1.enumeration.InspectionType;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.InspectionRequest;
import com.jovan.erp_v1.response.InspectionResponse;
import com.jovan.erp_v1.save_as.InspectionSaveAsRequest;
import com.jovan.erp_v1.search_request.InspectionSearchRequest;
import com.jovan.erp_v1.service.InfInspectionService;
import com.jovan.erp_v1.statistics.inspection.InspectionResultStatDTO;
import com.jovan.erp_v1.statistics.inspection.InspectionTypeStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityAcceptedByBatchStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityAcceptedByInspectorStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityAcceptedByProductStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityAcceptedByQualityCheckStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityInspectedByBatchStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityInspectedByInspectorStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityInspectedByProductStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityInspectedByQualityCheckStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityRejectedByBatchStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityRejectedByInspectorStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityRejectedByProductStatDTO;
import com.jovan.erp_v1.statistics.inspection.QuantityRejectedByQualityCheckStatDTO;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inspections")
@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
public class InspectionController {

	private final InfInspectionService inspectionService;
	
	@PreAuthorize(RoleGroups.INSPECTION_FULL_ACCESS)
	@PostMapping("/create/new-inspection")
	public ResponseEntity<InspectionResponse> create(@Valid @RequestBody InspectionRequest request){
		InspectionResponse items = inspectionService.create(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<InspectionResponse> update(@PathVariable Long id, @Valid @RequestBody InspectionRequest request){
		InspectionResponse items = inspectionService.update(id, request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		inspectionService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/find-one/{id}")
	public ResponseEntity<InspectionResponse> findOne(@PathVariable Long id){
		InspectionResponse items = inspectionService.findOne(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/find-all")
	public ResponseEntity<List<InspectionResponse>> findAll(){
		List<InspectionResponse> items = inspectionService.findAll();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search-inspections")
	public ResponseEntity<List<InspectionResponse>> searchInspections(
			@RequestParam(required = false) String storageName, 
			@RequestParam(required = false) String storageLocation,
			@RequestParam(required = false) BigDecimal minCapacity, 
			@RequestParam(required = false) BigDecimal maxCapacity){
		List<InspectionResponse> items = inspectionService.searchInspections(storageName, storageLocation, minCapacity, maxCapacity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-inspected/{inspectionId}")
	public ResponseEntity<InspectionQuantityInspectedDTO> getQuantityInspected(@PathVariable Long inspectionId){
		InspectionQuantityInspectedDTO items = inspectionService.getQuantityInspected(inspectionId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-accepted/{inspectionId}")
	public ResponseEntity<InspectionQuantityAcceptedDTO> getQuantityAccepted(@PathVariable Long inspectionId){
		InspectionQuantityAcceptedDTO items = inspectionService.getQuantityAccepted(inspectionId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-rejected/{inspectionId}")
	public ResponseEntity<InspectionQuantityRejectedDTO> getQuantityRejected(@PathVariable Long inspectionId){
		InspectionQuantityRejectedDTO items = inspectionService.getQuantityRejected(inspectionId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-inspected-summary")
	public ResponseEntity<InspectionQuantityInspectedSummaryDTO> getQuantityInspectedSummary(){
		InspectionQuantityInspectedSummaryDTO items = inspectionService.getQuantityInspectedSummary();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-accepted-summary")
	public ResponseEntity<InspectionQuantityAcceptedSummaryDTO> getQuantityAcceptedSummary(){
		InspectionQuantityAcceptedSummaryDTO items = inspectionService.getQuantityAcceptedSummary();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-rejected-summary")
	public ResponseEntity<InspectionQuantityRejectedSummaryDTO> getQuantityRejectedSummary(){
		InspectionQuantityRejectedSummaryDTO items = inspectionService.getQuantityRejectedSummary();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/exists/by-code")
	public ResponseEntity<Boolean> existsByCode(@RequestParam("code") String code){
		Boolean items = inspectionService.existsByCode(code);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/by-code")
	public ResponseEntity<List<InspectionResponse>> findByCode(@RequestParam("code") String code){
		List<InspectionResponse> items = inspectionService.findByCode(code);
		return ResponseEntity.ok(items);	
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/by-type")
	public ResponseEntity<List<InspectionResponse>> findByType(@RequestParam("type") InspectionType type){
		List<InspectionResponse> items = inspectionService.findByType(type);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/by-result")
	public ResponseEntity<List<InspectionResponse>> findByResult(@RequestParam("result") InspectionResult result){
		List<InspectionResponse> items = inspectionService.findByResult(result);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/by-notes")
	public ResponseEntity<List<InspectionResponse>> findByNotes(@RequestParam("notes") String notes){
		List<InspectionResponse> items = inspectionService.findByNotes(notes);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/type-and-result")
	public ResponseEntity<List<InspectionResponse>> findByTypeAndResult(@RequestParam("type") InspectionType type,@RequestParam("result") InspectionResult result){
		List<InspectionResponse> items = inspectionService.findByTypeAndResult(type, result);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/notes-and-type")
	public ResponseEntity<List<InspectionResponse>> findByNotesAndType(@RequestParam("notes") String notes,@RequestParam("type") InspectionType type){
		List<InspectionResponse> items = inspectionService.findByNotesAndType(notes, type);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/notes-and-result")
	public ResponseEntity<List<InspectionResponse>> findByNotesAndResult(@RequestParam("notes") String notes,@RequestParam("result") InspectionResult result){
		List<InspectionResponse> items = inspectionService.findByNotesAndResult(notes, result);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/inspection-date")
	public ResponseEntity<List<InspectionResponse>> findByInspectionDate(@RequestParam("inspectionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime inspectionDate){
		List<InspectionResponse> items = inspectionService.findByInspectionDate(inspectionDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/inspection-date-before")
	public ResponseEntity<List<InspectionResponse>> findByInspectionDateBefore(@RequestParam("inspectionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime inspectionDate){
		List<InspectionResponse> items = inspectionService.findByInspectionDateBefore(inspectionDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/inspection-date-after")
	public ResponseEntity<List<InspectionResponse>> findByInspectionDateAfter(@RequestParam("inspectionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime inspectionDate){
		List<InspectionResponse> items = inspectionService.findByInspectionDateAfter(inspectionDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/inspection-date-between")
	public ResponseEntity<List<InspectionResponse>> findByInspectionDateBetween(
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime end){
		List<InspectionResponse> items = inspectionService.findByInspectionDateBetween(start, end);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/inspection-date-and-result")
	public ResponseEntity<List<InspectionResponse>> findByInspectionDateAndResult(@RequestParam("inspectionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime inspectionDate,
			@RequestParam("") InspectionResult result){
		List<InspectionResponse> items = inspectionService.findByInspectionDateAndResult(inspectionDate, result);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/batch/{batchId}")
	public ResponseEntity<List<InspectionResponse>> findByBatchId(@PathVariable Long batchId){
		List<InspectionResponse> items = inspectionService.findByBatchId(batchId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch-code")
	public ResponseEntity<List<InspectionResponse>> findByBatchCode(@RequestParam("batchCode") String batchCode){
		List<InspectionResponse> items = inspectionService.findByBatchCode(batchCode);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/exists/batch-code")
	public ResponseEntity<Boolean> existsByBatchCode(@RequestParam("batchCode") String batchCode){
		Boolean items = inspectionService.existsByBatchCode(batchCode);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/expiry-date")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ExpiryDate(@RequestParam("expiryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate){
		List<InspectionResponse> items = inspectionService.findByBatch_ExpiryDate(expiryDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/expiry-date-after")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ExpiryDateAfter(@RequestParam("expiryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate){
		List<InspectionResponse> items = inspectionService.findByBatch_ExpiryDateAfter(expiryDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/expiry-date-before")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ExpiryDateBefore(@RequestParam("expiryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate){
		List<InspectionResponse> items = inspectionService.findByBatch_ExpiryDateBefore(expiryDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/expiry-date-range")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ExpiryDateBetween(
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
		List<InspectionResponse> items = inspectionService.findByBatch_ExpiryDateBetween(start, end);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/production-date")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductionDate(@RequestParam("productionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate productionDate){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductionDate(productionDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/production-date-after")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductionDateAfter(@RequestParam("productionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate productionDate){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductionDateAfter(productionDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/production-date-before")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductionDateBefore(@RequestParam("productionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate productionDate){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductionDateBefore(productionDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/production-date-range")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductionDateBetween(
			@RequestParam("productionDateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate productionDateStart,
			@RequestParam("productionDateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate productionDateEnd){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductionDateBetween(productionDateStart, productionDateEnd);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/production-date-now")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductionDateEqualsDateNow(){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductionDateEqualsDateNow();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/expiry-date-less-than-equal-now")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ExpiryDateLessThanEqualDateNow(){
		List<InspectionResponse> items = inspectionService.findByBatch_ExpiryDateLessThanEqualDateNow();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/production-date-greater-than-now")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductionDateGreaterThanEqualDateNow(){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductionDateGreaterThanEqualDateNow();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/expiry-date-greater-than-equal")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ExpiryDateGreaterThanEqual(@RequestParam("expiryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate){
		List<InspectionResponse> items = inspectionService.findByBatch_ExpiryDateGreaterThanEqual(expiryDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/production-date-less-than-equal")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductionDateLessThanEqual(@RequestParam("productionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate productionDate){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductionDateLessThanEqual(productionDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/expiry-date-after-today")
	public ResponseEntity<List<InspectionResponse>> findByBatchExpiryDateAfterToday(){
		List<InspectionResponse> items = inspectionService.findByBatchExpiryDateAfterToday();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/expiry-date-not-null")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ExpiryDateIsNotNull(){
		List<InspectionResponse> items = inspectionService.findByBatch_ExpiryDateIsNotNull();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/production-date-is-null")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductionDateIsNull(){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductionDateIsNull();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/production-date-not-null")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductionDateIsNotNull(){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductionDateIsNotNull();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/expiry-date-is-null")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ExpiryDateIsNull(){
		List<InspectionResponse> items = inspectionService.findByBatch_ExpiryDateIsNull();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/quantity-produced")
	public ResponseEntity<List<InspectionResponse>> findByBatch_QuantityProduced(@RequestParam("quantityProduced") Integer quantityProduced){
		List<InspectionResponse> items = inspectionService.findByBatch_QuantityProduced(quantityProduced);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/quantity-produced-greater-than")
	public ResponseEntity<List<InspectionResponse>> findByBatch_QuantityProducedGreaterThan(@RequestParam("quantityProduced") Integer quantityProduced){
		List<InspectionResponse> items = inspectionService.findByBatch_QuantityProducedGreaterThan(quantityProduced);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/quantity-produced-less-than")
	public ResponseEntity<List<InspectionResponse>> findByBatch_QuantityProducedLessThan(@RequestParam("quantityProduced") Integer quantityProduced){
		List<InspectionResponse> items = inspectionService.findByBatch_QuantityProducedLessThan(quantityProduced);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/quantity-produced-range")
	public ResponseEntity<List<InspectionResponse>> findByBatch_QuantityProducedBetween(@RequestParam("min") Integer min,@RequestParam("max") Integer max){
		List<InspectionResponse> items = inspectionService.findByBatch_QuantityProducedBetween(min, max);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/product/{productId}")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductId(@PathVariable Long productId){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductId(productId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/product-current-quantity")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductCurrentQuantity(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductCurrentQuantity(currentQuantity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/product-current-quantity-greater-than")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductCurrentQuantityGreaterThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductCurrentQuantityGreaterThan(currentQuantity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/product-current-quantity-less-than")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductCurrentQuantityLessThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductCurrentQuantityLessThan(currentQuantity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/product-current-quantity-between")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductCurrentQuantityBetween(@RequestParam("min") BigDecimal min,@RequestParam("max")  BigDecimal max){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductCurrentQuantityBetween(min, max);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/product-name")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductNameContainingIgnoreCase(@RequestParam("productName") String productName){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductNameContainingIgnoreCase(productName);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/product-unit-measure")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductUnitMeasure(@RequestParam("unitMeasure") UnitMeasure unitMeasure){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductUnitMeasure(unitMeasure);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/product-supplier-type")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductSupplierType(@RequestParam("supplierType") SupplierType supplierType){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductSupplierType(supplierType);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/product-storage-type")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductStorageType(@RequestParam("storageType") StorageType storageType){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductStorageType(storageType);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/product-goods-type")
	public ResponseEntity<List<InspectionResponse>> findByBatch_ProductGoodsType(@RequestParam("goodsType") GoodsType goodsType){
		List<InspectionResponse> items = inspectionService.findByBatch_ProductGoodsType(goodsType);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/product/storage/{storageId}")
	public ResponseEntity<List<InspectionResponse>> findByBatch_Product_StorageId(@PathVariable Long storageId){
		List<InspectionResponse> items = inspectionService.findByBatch_Product_StorageId(storageId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/product/shelf/{shelfId}")
	public ResponseEntity<List<InspectionResponse>> findByBatch_Product_ShelfId(@PathVariable Long shelfId){
		List<InspectionResponse> items = inspectionService.findByBatch_Product_ShelfId(shelfId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/batch/product/supply/{supplyId}")
	public ResponseEntity<List<InspectionResponse>> findByBatch_Product_SupplyId(@PathVariable Long supplyId){
		List<InspectionResponse> items = inspectionService.findByBatch_Product_SupplyId(supplyId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/inspector/{inspectorId}")
	public ResponseEntity<List<InspectionResponse>> findByInspectorId(@PathVariable Long inspectorId){
		List<InspectionResponse> items = inspectionService.findByInspectorId(inspectorId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/inspector-full-name")
	public ResponseEntity<List<InspectionResponse>> findByInspectorFirstNameContainingIgnoreCaseAndInspectorLastNameContainingIgnoreCase(
			@RequestParam("firstName") String firstName,@RequestParam("lastName") String lastName){
		List<InspectionResponse> items = inspectionService.findByInspectorFirstNameContainingIgnoreCaseAndInspectorLastNameContainingIgnoreCase(firstName, lastName);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/inspector-email")
	public ResponseEntity<List<InspectionResponse>> findByInspectorEmailLikeIgnoreCase(@RequestParam("inspectorEmail") String inspectorEmail){
		List<InspectionResponse> items = inspectionService.findByInspectorEmailLikeIgnoreCase(inspectorEmail);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/search/inspector-phone-number")
	public ResponseEntity<List<InspectionResponse>> findByInspectorPhoneNumberLikeIgnoreCase(@RequestParam("inspectorPhoneNumber") String inspectorPhoneNumber){
		List<InspectionResponse> items = inspectionService.findByInspectorPhoneNumberLikeIgnoreCase(inspectorPhoneNumber);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<InspectionResponse>> findByProductId(@PathVariable Long productId){
		List<InspectionResponse> items = inspectionService.findByProductId(productId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product-current-quantity")
	public ResponseEntity<List<InspectionResponse>> findByProductCurrentQuantity(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		List<InspectionResponse> items = inspectionService.findByProductCurrentQuantity(currentQuantity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product-current-quantity-greater-than")
	public ResponseEntity<List<InspectionResponse>> findByProductCurrentQuantityGreaterThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		List<InspectionResponse> items = inspectionService.findByProductCurrentQuantityGreaterThan(currentQuantity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product-current-quantity-less-than")
	public ResponseEntity<List<InspectionResponse>> findByProductCurrentQuantityLessThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		List<InspectionResponse> items = inspectionService.findByProductCurrentQuantityLessThan(currentQuantity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product-current-quantity-range")
	public ResponseEntity<List<InspectionResponse>> findByProductCurrentQuantityBetween(@RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max){
		List<InspectionResponse> items = inspectionService.findByProductCurrentQuantityBetween(min, max);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product-name")
	public ResponseEntity<List<InspectionResponse>> findByProductNameContainingIgnoreCase(@RequestParam("productName") String productName){
		List<InspectionResponse> items = inspectionService.findByProductNameContainingIgnoreCase(productName);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product-unit-measure")
	public ResponseEntity<List<InspectionResponse>> findByProductUnitMeasure(@RequestParam("unitMeasure") UnitMeasure unitMeasure){
		List<InspectionResponse> items = inspectionService.findByProductUnitMeasure(unitMeasure);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product-supplier-type")
	public ResponseEntity<List<InspectionResponse>> findByProductSupplierType(@RequestParam("supplierType") SupplierType supplierType){
		List<InspectionResponse> items = inspectionService.findByProductSupplierType(supplierType);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product-storage-type")
	public ResponseEntity<List<InspectionResponse>> findByProductStorageType(@RequestParam("storageType") StorageType storageType){
		List<InspectionResponse> items = inspectionService.findByProductStorageType(storageType);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product-goods-type")
	public ResponseEntity<List<InspectionResponse>> findByProductGoodsType(@RequestParam("goodsType") GoodsType goodsType){
		List<InspectionResponse> items = inspectionService.findByProductGoodsType(goodsType);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product/supply/{supplyId}")
	public ResponseEntity<List<InspectionResponse>> findByProduct_SupplyId(@PathVariable Long supplyId){
		List<InspectionResponse> items = inspectionService.findByProduct_SupplyId(supplyId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product/shelf/{shelfId}")
	public ResponseEntity<List<InspectionResponse>> findByProduct_ShelfId(@PathVariable Long shelfId){
		List<InspectionResponse> items = inspectionService.findByProduct_ShelfId(shelfId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product/storage-has-shelves")
	public ResponseEntity<InspectionResponse> findByProduct_StorageHasShelvesForIsNull(){
		InspectionResponse items = inspectionService.findByProduct_StorageHasShelvesForIsNull();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product/shelf-row-count")
	public ResponseEntity<List<InspectionResponse>> findByProduct_ShelfRowCount(@RequestParam("rowCount") Integer rowCount){
		List<InspectionResponse> items = inspectionService.findByProduct_ShelfRowCount(rowCount);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product/shelf-cols")
	public ResponseEntity<List<InspectionResponse>> findByProduct_ShelfCols(@RequestParam("cols") Integer cols){
		List<InspectionResponse> items = inspectionService.findByProduct_ShelfCols(cols);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product/shelf-row-col-nullable")
	public ResponseEntity<List<InspectionResponse>> findByProduct_ShelfRowAndColNullable(@RequestParam("row") Integer row,@RequestParam("col") Integer col){
		List<InspectionResponse> items = inspectionService.findByProduct_ShelfRowAndColNullable(row, col);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/product/shelf/row-range-and-col-range-nullable")
	public ResponseEntity<List<InspectionResponse>> findByProduct_ShelfRowAndColBetweenNullable(@RequestParam("rowMin") Integer rowMin,
			@RequestParam("rowMax") Integer rowMax,@RequestParam("colMin") Integer colMin,@RequestParam("colMax") Integer colMax){
		List<InspectionResponse> items = inspectionService.findByProduct_ShelfRowAndColBetweenNullable(rowMin, rowMax, colMin, colMax);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-inspected")
	public ResponseEntity<List<InspectionResponse>> findByQuantityInspected(@RequestParam("quantityInspected") Integer quantityInspected){
		List<InspectionResponse> items = inspectionService.findByQuantityInspected(quantityInspected);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-inspected-greater-than")
	public ResponseEntity<List<InspectionResponse>> findByQuantityInspectedGreaterThan(@RequestParam("quantityInspected") Integer quantityInspected){
		List<InspectionResponse> items = inspectionService.findByQuantityInspectedGreaterThan(quantityInspected);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-inspected-less-than")
	public ResponseEntity<List<InspectionResponse>> findByQuantityInspectedLessThan(@RequestParam("quantityInspected") Integer quantityInspected){
		List<InspectionResponse> items = inspectionService.findByQuantityInspectedLessThan(quantityInspected);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-inspected-between")
	public ResponseEntity<List<InspectionResponse>> findByQuantityInspectedBetween(@RequestParam("min") Integer min,@RequestParam("max") Integer max){
		List<InspectionResponse> items = inspectionService.findByQuantityInspectedBetween(min, max);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-accepted")
	public ResponseEntity<List<InspectionResponse>> findByQuantityAccepted(@RequestParam("quantityAccepted") Integer quantityAccepted){
		List<InspectionResponse> items = inspectionService.findByQuantityAccepted(quantityAccepted);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-accepted-greater-than")
	public ResponseEntity<List<InspectionResponse>> findByQuantityAcceptedGreaterThan(@RequestParam("quantityAccepted") Integer quantityAccepted){
		List<InspectionResponse> items = inspectionService.findByQuantityAcceptedGreaterThan(quantityAccepted);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-accepted-less-than")
	public ResponseEntity<List<InspectionResponse>> findByQuantityAcceptedLessThan(@RequestParam("quantityAccepted") Integer quantityAccepted){
		List<InspectionResponse> items = inspectionService.findByQuantityAcceptedLessThan(quantityAccepted);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-accepted-between")
	public ResponseEntity<List<InspectionResponse>> findByQuantityAcceptedBetween(@RequestParam("min") Integer min,@RequestParam("max") Integer max){
		List<InspectionResponse> items = inspectionService.findByQuantityAcceptedBetween(min, max);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-rejected")
	public ResponseEntity<List<InspectionResponse>> findByQuantityRejected(@RequestParam("quantityRejected") Integer quantityRejected){
		List<InspectionResponse> items = inspectionService.findByQuantityRejected(quantityRejected);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-rejected-greater-than")
	public ResponseEntity<List<InspectionResponse>> findByQuantityRejectedGreaterThan(@RequestParam("quantityRejected") Integer quantityRejected){
		List<InspectionResponse> items = inspectionService.findByQuantityRejectedGreaterThan(quantityRejected);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-rejected-less-than")
	public ResponseEntity<List<InspectionResponse>> findByQuantityRejectedLessThan(@RequestParam("quantityRejected") Integer quantityRejected){
		List<InspectionResponse> items = inspectionService.findByQuantityRejectedLessThan(quantityRejected);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quantity-rejected-between")
	public ResponseEntity<List<InspectionResponse>> findByQuantityRejectedBetween(@RequestParam("min") Integer min,@RequestParam("max") Integer max){
		List<InspectionResponse> items = inspectionService.findByQuantityRejectedBetween(min, max);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check/{qualityCheckId}")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheckId(@PathVariable Long qualityCheckId){
		List<InspectionResponse> items = inspectionService.findByQualityCheckId(qualityCheckId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check/loc-date")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheckLocDate(@RequestParam("locDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime locDate){
		List<InspectionResponse> items = inspectionService.findByQualityCheckLocDate(locDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check/loc-date-after")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheckLocDateAfter(@RequestParam("locDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime locDate){
		List<InspectionResponse> items = inspectionService.findByQualityCheckLocDateAfter(locDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check/loc-date-before")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheckLocDateBefore(@RequestParam("locDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime locDate){
		List<InspectionResponse> items = inspectionService.findByQualityCheckLocDateBefore(locDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check/loc-date-range")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheckLocDateBetween(
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start, 
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<InspectionResponse> items = inspectionService.findByQualityCheckLocDateBetween(start, end);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check-notes")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheckNotes(@RequestParam("notes") String notes){
		List<InspectionResponse> items = inspectionService.findByQualityCheckNotes(notes);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check/reference-id")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheckReferenceId(@RequestParam("referenceId") Long referenceId){
		List<InspectionResponse> items = inspectionService.findByQualityCheckReferenceId(referenceId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check/by-reference-type")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheckReferenceType(@RequestParam("referenceType") ReferenceType referenceType){
		List<InspectionResponse> items = inspectionService.findByQualityCheckReferenceType(referenceType);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check/by-check-type")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheck_CheckType(@RequestParam("checkType") QualityCheckType checkType){
		List<InspectionResponse> items = inspectionService.findByQualityCheck_CheckType(checkType);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check/by-status")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheck_Status(@RequestParam("status") QualityCheckStatus status){
		List<InspectionResponse> items = inspectionService.findByQualityCheck_Status(status);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check/reference-type-and-check-type")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheck_ReferenceTypeAndQualityCheck_CheckType(
			@RequestParam("referenceType") ReferenceType referenceType,@RequestParam("checkType") QualityCheckType checkType){
		List<InspectionResponse> items = inspectionService.findByQualityCheck_ReferenceTypeAndQualityCheck_CheckType(referenceType, checkType);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check/reference-type-and-status")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheck_ReferenceTypeAndQualityCheck_Status(@RequestParam("referenceType") ReferenceType referenceType,
			@RequestParam("status") QualityCheckStatus status){
		List<InspectionResponse> items = inspectionService.findByQualityCheck_ReferenceTypeAndQualityCheck_Status(referenceType, status);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check/by-check-type-and-status")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheck_CheckTypeAndQualityCheck_Status(@RequestParam("checkType") QualityCheckType checkType,
			@RequestParam("status") QualityCheckStatus status){
		List<InspectionResponse> items = inspectionService.findByQualityCheck_CheckTypeAndQualityCheck_Status(checkType, status);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check/inspector/{inspectorId}")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheckInspectorId(@PathVariable Long inspectorId){
		List<InspectionResponse> items = inspectionService.findByQualityCheckInspectorId(inspectorId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check/inspector-email")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheckInspectorEmailLikeIgnoreCase(@RequestParam("inspectorEmail") String inspectorEmail){
		List<InspectionResponse> items = inspectionService.findByQualityCheckInspectorEmailLikeIgnoreCase(inspectorEmail);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check/inspector-phone-number")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheckInspectorPhoneNumberLikeIgnoreCase(@RequestParam("inspectorPhoneNumber") String inspectorPhoneNumber){
		List<InspectionResponse> items = inspectionService.findByQualityCheckInspectorPhoneNumberLikeIgnoreCase(inspectorPhoneNumber);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/quality-check/inspector-full-name")
	public ResponseEntity<List<InspectionResponse>> findByQualityCheckInspectorFirstNameContainingIgnoreCaseAndQualityCheckInspectorLastNameContainingIgnoreCase(
			@RequestParam("firstName") String firstName,@RequestParam("lastName") String lastName){
		List<InspectionResponse> items = inspectionService.findByQualityCheckInspectorFirstNameContainingIgnoreCaseAndQualityCheckInspectorLastNameContainingIgnoreCase(firstName, lastName);
		return ResponseEntity.ok(items);
	}
	
	//nove metode
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/count/quantity-inspected-batch")
	public ResponseEntity<List<QuantityInspectedByBatchStatDTO>> countQuantityInspectedByBatch(){
		List<QuantityInspectedByBatchStatDTO> items = inspectionService.countQuantityInspectedByBatch();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/count/quantity-rejected-batch")
	public ResponseEntity<List<QuantityRejectedByBatchStatDTO>> countQuantityRejectedByBatch(){
		List<QuantityRejectedByBatchStatDTO> items = inspectionService.countQuantityRejectedByBatch();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/count/quantity-accepted-batch")
	public ResponseEntity<List<QuantityAcceptedByBatchStatDTO>> countQuantityAcceptedByBatch(){
		List<QuantityAcceptedByBatchStatDTO> items = inspectionService.countQuantityAcceptedByBatch();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/count/quantity-inspected-product")
	public ResponseEntity<List<QuantityInspectedByProductStatDTO>> countQuantityInspectedByProduct(){
		List<QuantityInspectedByProductStatDTO> items = inspectionService.countQuantityInspectedByProduct();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/count/quantity-accepted-product")
	public ResponseEntity<List<QuantityAcceptedByProductStatDTO>> countQuantityAcceptedByProduct(){
		List<QuantityAcceptedByProductStatDTO> items = inspectionService.countQuantityAcceptedByProduct();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/count/quantity-rejected-product")
	public ResponseEntity<List<QuantityRejectedByProductStatDTO>> countQuantityRejectedByProduct(){
		List<QuantityRejectedByProductStatDTO> items = inspectionService.countQuantityRejectedByProduct();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/count/quantity-inspected-inspector")
	public ResponseEntity<List<QuantityInspectedByInspectorStatDTO>> countQuantityInspectedByInspector(){
		List<QuantityInspectedByInspectorStatDTO> items = inspectionService.countQuantityInspectedByInspector();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/count/quantity-accepted-inspector")
	public ResponseEntity<List<QuantityAcceptedByInspectorStatDTO>> countQuantityAcceptedByInspector(){
		List<QuantityAcceptedByInspectorStatDTO> items = inspectionService.countQuantityAcceptedByInspector();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/count/quantity-rejected-inspector")
	public ResponseEntity<List<QuantityRejectedByInspectorStatDTO>> countQuantityRejectedByInspector(){
		List<QuantityRejectedByInspectorStatDTO> items = inspectionService.countQuantityRejectedByInspector();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/count/quantity-inspected-quality-check")
	public ResponseEntity<List<QuantityInspectedByQualityCheckStatDTO>> countQuantityInspectedByQualityCheck(){
		List<QuantityInspectedByQualityCheckStatDTO> items = inspectionService.countQuantityInspectedByQualityCheck();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/count/quantity-accepted-quality-check")
	public ResponseEntity<List<QuantityAcceptedByQualityCheckStatDTO>> countQuantityAcceptedByQualityCheck(){
		List<QuantityAcceptedByQualityCheckStatDTO> items = inspectionService.countQuantityAcceptedByQualityCheck();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/count/quantity-rejected-quality-check")
	public ResponseEntity<List<QuantityRejectedByQualityCheckStatDTO>> countQuantityRejectedByQualityCheck(){
		List<QuantityRejectedByQualityCheckStatDTO> items = inspectionService.countQuantityRejectedByQualityCheck();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/count/by-type")
	public ResponseEntity<List<InspectionTypeStatDTO>> countInspectionByType(){
		List<InspectionTypeStatDTO> items = inspectionService.countInspectionByType();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/count/by-result")
	public ResponseEntity<List<InspectionResultStatDTO>> countInspectionByResult(){
		List<InspectionResultStatDTO> items = inspectionService.countInspectionByResult();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/track-inspection-defect/{id}")
	public ResponseEntity<InspectionResponse> trackInspectionByInspectionDefect(@PathVariable Long id){
		InspectionResponse items = inspectionService.trackInspectionByInspectionDefect(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/track-test-measurement/{id}")
	public ResponseEntity<InspectionResponse> trackInspectionByTestMeasurement(@PathVariable Long id){
		InspectionResponse items = inspectionService.trackInspectionByTestMeasurement(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_READ_ACCESS)
	@GetMapping("/reports")
	public ResponseEntity<List<InspectionResponse>> findByReports(@RequestParam Long id,@RequestParam(required = false) String notes){
		List<InspectionResponse> items = inspectionService.findByReports(id, notes);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_FULL_ACCESS)
	@PostMapping("/{id}/confirm")
	public ResponseEntity<InspectionResponse> confirmInspection(@PathVariable Long id){
		InspectionResponse items = inspectionService.confirmInspection(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_FULL_ACCESS)
	@PostMapping("/{id}/cancel")
	public ResponseEntity<InspectionResponse> cancelInspection(@PathVariable Long id){
		InspectionResponse items = inspectionService.cancelInspection(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_FULL_ACCESS)
	@PostMapping("/{id}/close")
	public ResponseEntity<InspectionResponse> closeInspection(@PathVariable Long id){
		InspectionResponse items = inspectionService.closeInspection(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_FULL_ACCESS)
	@PostMapping("/{id}/status/{status}")
	public ResponseEntity<InspectionResponse> changeStatus(@PathVariable Long id,@PathVariable  InspectionStatus status){
		InspectionResponse items = inspectionService.changeStatus(id, status);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_FULL_ACCESS)
	@PostMapping("/save")
	public ResponseEntity<InspectionResponse> saveInspection(@Valid @RequestBody InspectionRequest request){
		InspectionResponse items = inspectionService.saveInspection(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_FULL_ACCESS)
	@PostMapping("/save-as")
	public ResponseEntity<InspectionResponse> saveAs(@Valid @RequestBody InspectionSaveAsRequest request){
		InspectionResponse items = inspectionService.saveAs(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_FULL_ACCESS)
	@PostMapping("/save-all")
	public ResponseEntity<List<InspectionResponse>> saveAll(@Valid @RequestBody List<InspectionRequest> requests){
		List<InspectionResponse> items = inspectionService.saveAll(requests);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_FULL_ACCESS)
	@PostMapping("/general-search")
	public ResponseEntity<List<InspectionResponse>> generalSearch(@RequestBody InspectionSearchRequest request){
		List<InspectionResponse> items = inspectionService.generalSearch(request);
		return ResponseEntity.ok(items);
	}
}
