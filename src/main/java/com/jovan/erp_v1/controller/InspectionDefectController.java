package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
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

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.InspectionResult;
import com.jovan.erp_v1.enumeration.InspectionType;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.enumeration.SeverityLevel;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.InspectionDefectRequest;
import com.jovan.erp_v1.response.InspectionDefectResponse;
import com.jovan.erp_v1.service.InfInspectionDefectService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inspectionDefects")
@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
public class InspectionDefectController {

	private final InfInspectionDefectService inspectionDefectService;
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_FULL_ACCESS)
	@PostMapping("/crete/new-inspectionDefects")
	public ResponseEntity<InspectionDefectResponse> create(@Valid @RequestBody InspectionDefectRequest request){
		InspectionDefectResponse items = inspectionDefectService.create(request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<InspectionDefectResponse> update(@PathVariable Long id, @Valid @RequestBody InspectionDefectRequest request){
		InspectionDefectResponse items = inspectionDefectService.update(id, request);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_FULL_ACCESS)
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		inspectionDefectService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/find-one/{id}")
	public ResponseEntity<InspectionDefectResponse> findOne(@PathVariable Long id){
		InspectionDefectResponse items = inspectionDefectService.findOne(id);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/find-all")
	public ResponseEntity<List<InspectionDefectResponse>> findAll(){
		List<InspectionDefectResponse> items = inspectionDefectService.findAll();
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/by-quantity-affected")
	public ResponseEntity<List<InspectionDefectResponse>> findByQuantityAffected(@RequestParam("quantityAffected") Integer quantityAffected){
		List<InspectionDefectResponse> items = inspectionDefectService.findByQuantityAffected(quantityAffected);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/by-quantity-affected-greater-than")
	public ResponseEntity<List<InspectionDefectResponse>> findByQuantityAffectedGreaterThan(@RequestParam("quantityAffected") Integer quantityAffected){
		List<InspectionDefectResponse> items = inspectionDefectService.findByQuantityAffectedGreaterThan(quantityAffected);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/by-quantity-affected-less-than")
	public ResponseEntity<List<InspectionDefectResponse>> findByQuantityAffectedLessThan(@RequestParam("quantityAffected") Integer quantityAffected){
		List<InspectionDefectResponse> items = inspectionDefectService.findByQuantityAffectedLessThan(quantityAffected);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/by-quantity-affected-between")
	public ResponseEntity<List<InspectionDefectResponse>> findByQuantityAffectedBetween(@RequestParam("min") Integer min,@RequestParam("max") Integer max){
		List<InspectionDefectResponse> items = inspectionDefectService.findByQuantityAffectedBetween(min, max);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/inspection/{inspectionId}")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspectionId(@PathVariable Long inspectionId){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspectionId(inspectionId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/exist/inspection-code")
	public ResponseEntity<Boolean> existsByInspectionCode(@RequestParam("inspectionCode") String inspectionCode){
		Boolean items = inspectionDefectService.existsByInspectionCode(inspectionCode);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection-code")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_CodeLikeIgnoreCase(@RequestParam("inspectionCode") String inspectionCode){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_CodeLikeIgnoreCase(inspectionCode);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection-type")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_Type(@RequestParam("type") InspectionType type){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_Type(type);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection-result")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_Result(@RequestParam("result") InspectionResult result){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_Result(result);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection-notes")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_Notes(@RequestParam("notes") String notes){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_Notes(notes);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection-type-and-result")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_TypeAndInspection_Result(
			@RequestParam("type") InspectionType type,@RequestParam("result")  InspectionResult result){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_TypeAndInspection_Result(type, result);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection-notes-and-type")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_NotesAndInspection_Type(
			@RequestParam("notes") String notes,@RequestParam("type")  InspectionType type){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_NotesAndInspection_Type(notes, type);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection-notes-result")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_NotesAndInspection_Result(
			@RequestParam("notes") String notes,@RequestParam("result")  InspectionResult result){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_NotesAndInspection_Result(notes, result);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection-date")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_InspectionDate(
			@RequestParam("inspectionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inspectionDate){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_InspectionDate(inspectionDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection-date-before")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_InspectionDateBefore(
			@RequestParam("inspectionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inspectionDate){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_InspectionDateBefore(inspectionDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection-date-after")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_InspectionDateAfter(
			@RequestParam("inspectionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inspectionDate){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_InspectionDateAfter(inspectionDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection-date-between")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_InspectionDateBetween(
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_InspectionDateBetween(start, end);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection-date-and-result")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_InspectionDateAndInspection_Result(
			@RequestParam("inspectionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inspectionDate,
			@RequestParam("result")  InspectionResult result){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_InspectionDateAndInspection_Result(inspectionDate, result);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/inspector/{inspectorId}")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_InspectorId(@PathVariable Long inspectorId){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_InspectorId(inspectorId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/inspector-full-name")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_InspectorFirstNameContainingIgnoreCaseAndInspection_InspectorLastNameContainingIgnoreCase(
			@RequestParam("firstName") String firstName,@RequestParam("lastName")  String lastName){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_InspectorFirstNameContainingIgnoreCaseAndInspection_InspectorLastNameContainingIgnoreCase(firstName, lastName);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/inspector-email")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_InspectorEmailLikeIgnoreCase(@RequestParam("inspectorEmail") String inspectorEmail){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_InspectorEmailLikeIgnoreCase(inspectorEmail);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/inspector-phone-number")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_InspectorPhoneNumberLikeIgnoreCase(@RequestParam("inspectorPhoneNumber") String inspectorPhoneNumber){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_InspectorPhoneNumberLikeIgnoreCase(inspectorPhoneNumber);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quantity-inspected")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QuantityInspected(@RequestParam("quantityInspected") Integer quantityInspected){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QuantityInspected(quantityInspected);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quantity-inspected-greater-than")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QuantityInspectedGreaterThan(@RequestParam("quantityInspected") Integer quantityInspected){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QuantityInspectedGreaterThan(quantityInspected);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quantity-inspected-less-than")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QuantityInspectedLessThan(@RequestParam("quantityInspected") Integer quantityInspected){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QuantityInspectedLessThan(quantityInspected);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quantity-inspected-range")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QuantityInspectedBetween(@RequestParam("min") Integer min,@RequestParam("max") Integer max){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QuantityInspectedBetween(min, max);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quantity-accepted")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QuantityAccepted(@RequestParam("quantityAccepted") Integer quantityAccepted){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QuantityAccepted(quantityAccepted);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quantity-accepted-greater-than")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QuantityAcceptedGreaterThan(@RequestParam("quantityAccepted") Integer quantityAccepted){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QuantityAcceptedGreaterThan(quantityAccepted);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quantity-accepted-less-than")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QuantityAcceptedLessThan(@RequestParam("quantityAccepted") Integer quantityAccepted){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QuantityAcceptedLessThan(quantityAccepted);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quantity-accepted-range")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QuantityAcceptedBetween(@RequestParam("min") Integer min,@RequestParam("max") Integer max){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QuantityAcceptedBetween(min, max);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quantity-rejected")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QuantityRejected(@RequestParam("quantityRejected") Integer quantityRejected){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QuantityRejected(quantityRejected);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quantity-rejected-greater-than")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QuantityRejectedGreaterThan(@RequestParam("quantityRejected") Integer quantityRejected){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QuantityRejectedGreaterThan(quantityRejected);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quantity-rejected-less-than")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QuantityRejectedLessThan(@RequestParam("quantityRejected") Integer quantityRejected){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QuantityRejectedLessThan(quantityRejected);
		return ResponseEntity.ok(items);
			
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quantity-rejected-range")
	public ResponseEntity<List<InspectionDefectResponse>>  findByInspection_QuantityRejectedBetween(@RequestParam("min") Integer min,@RequestParam("max") Integer max){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QuantityRejectedBetween(min, max);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quality-check/{qualityCheckId}")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QualityCheck_Id(@PathVariable Long qualityCheckId){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QualityCheck_Id(qualityCheckId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quality-check-loc-date")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QualityCheck_LocDate(
			@RequestParam("locDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime locDate){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QualityCheck_LocDate(locDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quality-check-loc-date-after")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QualityCheck_LocDateAfter(
			@RequestParam("locDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime locDate){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QualityCheck_LocDateAfter(locDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quality-check-loc-date-before")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QualityCheck_LocDateBefore(
			@RequestParam("locDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime locDate){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QualityCheck_LocDateBefore(locDate);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quality-check-loc-date-range")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QualityCheck_LocDateBetween(
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QualityCheck_LocDateBetween(start, end);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quality-check-notes")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QualityCheck_Notes(@RequestParam("notes") String notes){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QualityCheck_Notes(notes);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quality-check-reference-id")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QualityCheck_ReferenceId(@RequestParam("referenceId") Long referenceId){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QualityCheck_ReferenceId(referenceId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quality-check-reference-type")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QualityCheck_ReferenceType(@RequestParam("referenceType") ReferenceType referenceType){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QualityCheck_ReferenceType(referenceType);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quality-check/check-type")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QualityCheck_CheckType(@RequestParam("checkType") QualityCheckType checkType){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QualityCheck_CheckType(checkType);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quality-check-status")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QualityCheck_Status(@RequestParam("status") QualityCheckStatus status){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QualityCheck_Status(status);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quality-check/reference-type-and-check-type")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QualityCheck_ReferenceTypeAndInspection_QualityCheck_CheckType(
			@RequestParam("referenceType") ReferenceType referenceType,@RequestParam("checkType") QualityCheckType checkType){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QualityCheck_ReferenceTypeAndInspection_QualityCheck_CheckType(referenceType, checkType);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quality-check/reference-type-status")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QualityCheck_ReferenceTypeAndInspection_QualityCheck_Status(
			@RequestParam("referenceType") ReferenceType referenceType,@RequestParam("status") QualityCheckStatus status){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QualityCheck_ReferenceTypeAndInspection_QualityCheck_Status(referenceType, status);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/quality-check/check-type-status")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_QualityCheck_CheckTypeAndInspection_QualityCheck_Status(
			@RequestParam("checkType") QualityCheckType checkType,@RequestParam("status") QualityCheckStatus status){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_QualityCheck_CheckTypeAndInspection_QualityCheck_Status(checkType, status);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/product/{productId}")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_Product_Id(@PathVariable Long productId){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_Product_Id(productId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/product-current-quantity")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_Product_CurrentQuantity(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_Product_CurrentQuantity(currentQuantity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/product-current-quantity-greater-than")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_Product_CurrentQuantityGreaterThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_Product_CurrentQuantityGreaterThan(currentQuantity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/product-current-quantity-less-than")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_Product_CurrentQuantityLessThan(@RequestParam("currentQuantity") BigDecimal currentQuantity){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_Product_CurrentQuantityLessThan(currentQuantity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/product-current-quantity-range")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_Product_CurrentQuantityBetween(
			@RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_Product_CurrentQuantityBetween(min, max);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/product-name")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_Product_NameContainingIgnoreCase(@RequestParam("productName") String productName){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_Product_NameContainingIgnoreCase(productName);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/product-unit-measure")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_Product_UnitMeasure(@RequestParam("unitMeasure") UnitMeasure unitMeasure){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_Product_UnitMeasure(unitMeasure);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/product-supplier-type")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_Product_SupplierType(@RequestParam("supplierType") SupplierType supplierType){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_Product_SupplierType(supplierType);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/product-storage-type")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_Product_StorageType(@RequestParam("storageType") StorageType storageType){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_Product_StorageType(storageType);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/product-goods-type")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_Product_GoodsType(@RequestParam("goodsType") GoodsType goodsType){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_Product_GoodsType(goodsType);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/inspection/batch/{batchId}")
	public ResponseEntity<List<InspectionDefectResponse>> findByInspection_Batch_Id(@PathVariable Long batchId){
		List<InspectionDefectResponse> items = inspectionDefectService.findByInspection_Batch_Id(batchId);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/defect-code")
	public ResponseEntity<List<InspectionDefectResponse>> findByDefect_CodeContainingIgnoreCase(@RequestParam("code") String code){
		List<InspectionDefectResponse> items = inspectionDefectService.findByDefect_CodeContainingIgnoreCase(code);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/defect-name")
	public ResponseEntity<List<InspectionDefectResponse>> findByDefect_NameContainingIgnoreCase(@RequestParam("name") String name){
		List<InspectionDefectResponse> items = inspectionDefectService.findByDefect_NameContainingIgnoreCase(name);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/defect-description")
	public ResponseEntity<List<InspectionDefectResponse>> findByDefect_DescriptionContainingIgnoreCase(@RequestParam("description") String description){
		List<InspectionDefectResponse> items = inspectionDefectService.findByDefect_DescriptionContainingIgnoreCase(description);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/defect/code-and-name")
	public ResponseEntity<List<InspectionDefectResponse>> findByDefect_CodeContainingIgnoreCaseAndDefect_NameContainingIgnoreCase(
			@RequestParam("code") String code,@RequestParam("name") String name){
		List<InspectionDefectResponse> items = inspectionDefectService.findByDefect_CodeContainingIgnoreCaseAndDefect_NameContainingIgnoreCase(code, name);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/defect-severity")
	public ResponseEntity<List<InspectionDefectResponse>> findByDefect_Severity(@RequestParam("severity") SeverityLevel severity){
		List<InspectionDefectResponse> items = inspectionDefectService.findByDefect_Severity(severity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/defect/code-and-severity")
	public ResponseEntity<List<InspectionDefectResponse>> findByDefect_CodeContainingIgnoreCaseAndDefect_Severity(
			@RequestParam("code") String code,@RequestParam("severity") SeverityLevel severity){
		List<InspectionDefectResponse> items = inspectionDefectService.findByDefect_CodeContainingIgnoreCaseAndDefect_Severity(code, severity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/defect/name-and-severity")
	public ResponseEntity<List<InspectionDefectResponse>> findByDefect_NameContainingIgnoreCaseAndDefect_Severity(
			@RequestParam("name") String name,@RequestParam("severity") SeverityLevel severity){
		List<InspectionDefectResponse> items = inspectionDefectService.findByDefect_NameContainingIgnoreCaseAndDefect_Severity(name, severity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/search/defect/severity-and-desc-part")
	public ResponseEntity<List<InspectionDefectResponse>> findByDefect_SeverityAndDefect_DescriptionContainingIgnoreCase(
			@RequestParam("severity") SeverityLevel severity,@RequestParam("descPart") String descPart){
		List<InspectionDefectResponse> items = inspectionDefectService.findByDefect_SeverityAndDefect_DescriptionContainingIgnoreCase(severity, descPart);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/count/defect-severity")
	public ResponseEntity<Long> countByDefect_Severity(@RequestParam("severity") SeverityLevel severity){
		Long items = inspectionDefectService.countByDefect_Severity(severity);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/count/defect-code")
	public ResponseEntity<Long> countByDefect_Code(@RequestParam("code") String code){
		Long items = inspectionDefectService.countByDefect_Code(code);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/count/defect-name")
	public ResponseEntity<Long> countByDefect_Name(@RequestParam("name") String name){
		Long items = inspectionDefectService.countByDefect_Name(name);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/exist/defect-code")
	public ResponseEntity<Boolean> existsByDefect_Code(@RequestParam("code") String code){
		Boolean items = inspectionDefectService.existsByDefect_Code(code);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/exist/defect-name")
	public ResponseEntity<Boolean> existsByDefect_Name(@RequestParam("name") String name){
		Boolean items = inspectionDefectService.existsByDefect_Name(name);
		return ResponseEntity.ok(items);
	}
	
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	@GetMapping("/confirmed")
	public ResponseEntity<List<InspectionDefectResponse>> findByConfirmed(@RequestParam("confirmed") Boolean confirmed){
		List<InspectionDefectResponse> items = inspectionDefectService.findByConfirmed(confirmed);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/defects/{defectId}/confirmed")
	@PreAuthorize(RoleGroups.INSPECTION_DEFECT_READ_ACCESS)
	public ResponseEntity<List<InspectionDefectResponse>> findByDefectIdAndConfirmed(@PathVariable Long defectId,@RequestParam("confirmed") Boolean confirmed){
		List<InspectionDefectResponse> items = inspectionDefectService.findByDefectIdAndConfirmed(defectId, confirmed);
		return ResponseEntity.ok(items);
	}
}
