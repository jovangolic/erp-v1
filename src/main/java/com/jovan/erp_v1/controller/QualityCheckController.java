package com.jovan.erp_v1.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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

import com.jovan.erp_v1.dto.CheckTypeCountResponse;
import com.jovan.erp_v1.dto.DateCountResponse;
import com.jovan.erp_v1.dto.InspectorCountResponse;
import com.jovan.erp_v1.dto.InspectorNameCountResponse;
import com.jovan.erp_v1.dto.ReferenceTypeCountResponse;
import com.jovan.erp_v1.dto.StatusCountResponse;
import com.jovan.erp_v1.enumeration.QualityCheckStatus;
import com.jovan.erp_v1.enumeration.QualityCheckType;
import com.jovan.erp_v1.enumeration.ReferenceType;
import com.jovan.erp_v1.request.QualityCheckRequest;
import com.jovan.erp_v1.response.QualityCheckResponse;
import com.jovan.erp_v1.service.IQualityCheckService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qualityChecks")
public class QualityCheckController {

	private final IQualityCheckService qualityCheckService;
	
	@PostMapping("/create/new-quality-check")
	public ResponseEntity<QualityCheckResponse> create(@Valid @RequestBody QualityCheckRequest request){
		QualityCheckResponse items = qualityCheckService.create(request);
		return ResponseEntity.ok(items);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<QualityCheckResponse> update(@PathVariable Long id, @Valid @RequestBody QualityCheckRequest request){
		QualityCheckResponse items = qualityCheckService.update(id, request);
		return ResponseEntity.ok(items);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		qualityCheckService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/find-one/{id}")
	public ResponseEntity<QualityCheckResponse> findOne(@PathVariable Long id){
		QualityCheckResponse items = qualityCheckService.findOne(id);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/find-all")
	public ResponseEntity<List<QualityCheckResponse>> findAll(){
		List<QualityCheckResponse> items = qualityCheckService.findAll();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-reference-type")
	public ResponseEntity<List<QualityCheckResponse>> findByReferenceType(@RequestParam("referenceType") ReferenceType referenceType){
		List<QualityCheckResponse> items = qualityCheckService.findByReferenceType(referenceType);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-check-type")
	public ResponseEntity<List<QualityCheckResponse>> findByCheckType(@RequestParam("checkType") QualityCheckType checkType){
		List<QualityCheckResponse> items = qualityCheckService.findByCheckType(checkType);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-status")
	public ResponseEntity<List<QualityCheckResponse>> findByStatus(@RequestParam("status") QualityCheckStatus status){
		List<QualityCheckResponse> items = qualityCheckService.findByStatus(status);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/check-type-and-status")
	public ResponseEntity<List<QualityCheckResponse>> findByCheckTypeAndStatus(@RequestParam("checkType") QualityCheckType checkType,@RequestParam("status") QualityCheckStatus status){
		List<QualityCheckResponse> items = qualityCheckService.findByCheckTypeAndStatus(checkType, status);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/status-reference-type")
	public ResponseEntity<List<QualityCheckResponse>> findByStatusAndReferenceType(@RequestParam("status") QualityCheckStatus status,@RequestParam("referenceType") ReferenceType referenceType){
		List<QualityCheckResponse> items = qualityCheckService.findByStatusAndReferenceType(status, referenceType);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/check-type-reference-type")
	public ResponseEntity<List<QualityCheckResponse>> findByCheckTypeAndReferenceType(@RequestParam("checkType") QualityCheckType checkType,@RequestParam("referenceType") ReferenceType referenceType){
		List<QualityCheckResponse> items = qualityCheckService.findByCheckTypeAndReferenceType(checkType, referenceType);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/reference-id-reference-type")
	public ResponseEntity<List<QualityCheckResponse>> findByReferenceIdAndReferenceType(@RequestParam("referenceId") Long referenceId,@RequestParam("referenceType") ReferenceType referenceType){
		List<QualityCheckResponse> items = qualityCheckService.findByReferenceIdAndReferenceType(referenceId, referenceType);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/reference-id-check-type")
	public ResponseEntity<List<QualityCheckResponse>> findByReferenceIdAndCheckType(@RequestParam("referenceId") Long referenceId,@RequestParam("checkType") QualityCheckType checkType){
		List<QualityCheckResponse> items = qualityCheckService.findByReferenceIdAndCheckType(referenceId, checkType);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/reference-id-status")
	public ResponseEntity<List<QualityCheckResponse>> findByReferenceIdAndStatus(@RequestParam("referenceId") Long referenceId,@RequestParam("status") QualityCheckStatus status){
		List<QualityCheckResponse> items = qualityCheckService.findByReferenceIdAndStatus(referenceId, status);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-reference-types")
	public ResponseEntity<List<QualityCheckResponse>> findByReferenceTypeIn(@RequestParam("referenceType") List<ReferenceType> referenceTypes){
		List<QualityCheckResponse> items = qualityCheckService.findByReferenceTypeIn(referenceTypes);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-check-types")
	public ResponseEntity<List<QualityCheckResponse>> findByCheckTypeIn(@RequestParam("checkType") List<QualityCheckType> checkTypes){
		List<QualityCheckResponse> items = qualityCheckService.findByCheckTypeIn(checkTypes);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-statuses")
	public ResponseEntity<List<QualityCheckResponse>> findByStatusIn(@RequestParam("status") List<QualityCheckStatus> statuses){
		List<QualityCheckResponse> items = qualityCheckService.findByStatusIn(statuses);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/reference-id-reference-type-status")
	public ResponseEntity<List<QualityCheckResponse>> findByReferenceIdAndReferenceTypeAndStatus(@RequestParam("referenceId") Long referenceId,
			@RequestParam("referenceType") ReferenceType referenceType,@RequestParam("status") QualityCheckStatus status){
		List<QualityCheckResponse> items = qualityCheckService.findByReferenceIdAndReferenceTypeAndStatus(referenceId, referenceType, status);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/reference-id-reference-type-check-type")
	public ResponseEntity<List<QualityCheckResponse>> findByReferenceIdAndReferenceTypeAndCheckType( @RequestParam("referenceId")Long referenceId,
			@RequestParam("referenceType") ReferenceType referenceType,@RequestParam("checkType") QualityCheckType checkType){
		List<QualityCheckResponse> items = qualityCheckService.findByReferenceIdAndReferenceTypeAndCheckType(referenceId, referenceType, checkType);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-notes")
	public ResponseEntity<List<QualityCheckResponse>> findByNotes(@RequestParam("notes") String notes){
		List<QualityCheckResponse> items = qualityCheckService.findByNotes(notes);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-reference-id")
	public ResponseEntity<List<QualityCheckResponse>> findByReferenceId(@RequestParam("referenceId") Long referenceId){
		List<QualityCheckResponse> items = qualityCheckService.findByReferenceId(referenceId);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-date")
	public ResponseEntity<List<QualityCheckResponse>> findByLocDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		List<QualityCheckResponse> items = qualityCheckService.findByLocDate(date);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/date-before")
	public ResponseEntity<List<QualityCheckResponse>> findByLocDateBefore(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		List<QualityCheckResponse> items = qualityCheckService.findByLocDateBefore(date);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/date-after")
	public ResponseEntity<List<QualityCheckResponse>> findByLocDateAfter(@RequestParam("date")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		List<QualityCheckResponse> items = qualityCheckService.findByLocDateAfter(date);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/date-between")
	public ResponseEntity<List<QualityCheckResponse>> findByLocDateBetween(@RequestParam("startDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		List<QualityCheckResponse> items = qualityCheckService.findByLocDateBetween(startDate, endDate);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-status-and-date-between")
	public ResponseEntity<List<QualityCheckResponse>> findByStatusAndLocDateBetween(@RequestParam("status") QualityCheckStatus status,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		List<QualityCheckResponse> items = qualityCheckService.findByStatusAndLocDateBetween(status, startDate, endDate);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-check-type-and-date-range")
	public ResponseEntity<List<QualityCheckResponse>> findByCheckTypeAndLocDateBetween(@RequestParam("checkType") QualityCheckType checkType,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		List<QualityCheckResponse> items = qualityCheckService.findByCheckTypeAndLocDateBetween(checkType, startDate, endDate);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/by-reference-type-and-date-range")
	public ResponseEntity<List<QualityCheckResponse>> findByReferenceTypeAndLocDateBetween(@RequestParam("referenceType") ReferenceType referenceType,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		List<QualityCheckResponse> items = qualityCheckService.findByReferenceTypeAndLocDateBetween(referenceType, startDate, endDate);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/reference-id-order-by-date-desc")
	public ResponseEntity<List<QualityCheckResponse>> findByReferenceIdOrderByLocDateDesc(@RequestParam("referenceId") Long referenceId){
		List<QualityCheckResponse> items = qualityCheckService.findByReferenceIdOrderByLocDateDesc(referenceId);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/inspector/{inspectorId}/order-by-date-desc")
	public ResponseEntity<List<QualityCheckResponse>> findByInspectorIdOrderByLocDateDesc(@PathVariable Long inspectorId){
		List<QualityCheckResponse> items = qualityCheckService.findByInspectorIdOrderByLocDateDesc(inspectorId);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/exists/reference-id-reference-type-status")
	public ResponseEntity<Boolean> existsByReferenceIdAndReferenceTypeAndStatus(@RequestParam("referenceId") Long referenceId,
			@RequestParam("referenceType") ReferenceType referenceType,@RequestParam("status") QualityCheckStatus status){
		Boolean items = qualityCheckService.existsByReferenceIdAndReferenceTypeAndStatus(referenceId, referenceType, status);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/exists/inspector/{inspectorId}/date-between")
	public ResponseEntity<Boolean> existsByInspectorIdAndLocDateBetween(@PathVariable Long inspectorId,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		Boolean items = qualityCheckService.existsByInspectorIdAndLocDateBetween(inspectorId, startDate, endDate);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/count-status")
	public ResponseEntity<Long>  countByStatus(@RequestParam("status") QualityCheckStatus status){
		Long items = qualityCheckService.countByStatus(status);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/count-check-type")
	public ResponseEntity<Long> countByCheckType(@RequestParam("checkType") QualityCheckType checkType){
		Long items = qualityCheckService.countByCheckType(checkType);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/count-reference-type")
	public ResponseEntity<Long> countByReferenceType(@RequestParam("referenceType") ReferenceType referenceType){
		Long items = qualityCheckService.countByReferenceType(referenceType);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/count-inspector/{inspectorId}")
	public ResponseEntity<Long> countByInspectorId(@PathVariable Long inspectorId){
		Long items = qualityCheckService.countByInspectorId(inspectorId);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/count-reference-type-and-status")
	public ResponseEntity<Long> countByReferenceTypeAndStatus(@RequestParam("referenceType") ReferenceType referenceType,@RequestParam("status") QualityCheckStatus status){
		Long items = qualityCheckService.countByReferenceTypeAndStatus(referenceType, status);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/count-date-between")
	public ResponseEntity<Long> countByLocDateBetween(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		Long items = qualityCheckService.countByLocDateBetween(startDate, endDate);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/inspector/{inspectorId}")
	public ResponseEntity<List<QualityCheckResponse>> findByInspectorId(@PathVariable Long inspectorId){
		List<QualityCheckResponse> items = qualityCheckService.findByInspectorId(inspectorId);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/inspector-email")
	public ResponseEntity<List<QualityCheckResponse>> findByInspectorEmailLikeIgnoreCase(@RequestParam("email") String email){
		List<QualityCheckResponse> items = qualityCheckService.findByInspectorEmailLikeIgnoreCase(email);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/inspector-phone-number")
	public ResponseEntity<List<QualityCheckResponse>> findByInspectorPhoneNumberLikeIgnoreCase(@RequestParam("phoneNumber") String phoneNumber){
		List<QualityCheckResponse> items = qualityCheckService.findByInspectorPhoneNumberLikeIgnoreCase(phoneNumber);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/inspector-full-name")
	public ResponseEntity<List<QualityCheckResponse>> findByInspector_FirstNameContainingIgnoreCaseAndInspector_LastNameContainingIgnoreCase(@RequestParam("firstName") String firstName
			,@RequestParam("lastName") String lastName){
		List<QualityCheckResponse> items = qualityCheckService.findByInspector_FirstNameContainingIgnoreCaseAndInspector_LastNameContainingIgnoreCase(firstName, lastName);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/inspector/{inspectorId}/status")
	public ResponseEntity<List<QualityCheckResponse>> findByInspectorIdAndStatus(@PathVariable  Long inspectorId,@RequestParam("status") QualityCheckStatus status){
		List<QualityCheckResponse> items = qualityCheckService.findByInspectorIdAndStatus(inspectorId, status);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/inspector/{inspectorId}/check-type")
	public ResponseEntity<List<QualityCheckResponse>> findByInspectorIdAndCheckType(@PathVariable  Long inspectorId,@RequestParam("checkType") QualityCheckType checkType){
		List<QualityCheckResponse> items = qualityCheckService.findByInspectorIdAndCheckType(inspectorId, checkType);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/inspector/{inspectorId}/reference-type")
	public ResponseEntity<List<QualityCheckResponse>> findByInspectorIdAndReferenceType(@PathVariable Long inspectorId,@RequestParam("referenceType") ReferenceType referenceType){
		List<QualityCheckResponse> items = qualityCheckService.findByInspectorIdAndReferenceType(inspectorId, referenceType);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/count-by-status-grouped")
	public ResponseEntity<List<StatusCountResponse>> countByStatusGrouped(){
		List<StatusCountResponse> items = qualityCheckService.countByStatusGrouped();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/count-by-check-type-grouped")
	public ResponseEntity<List<CheckTypeCountResponse>> countByCheckTypeGrouped(){
		List<CheckTypeCountResponse> items = qualityCheckService.countByCheckTypeGrouped();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/count-by-reference-type-grouped")
	public ResponseEntity<List<ReferenceTypeCountResponse>>  countByReferenceTypeGrouped(){
		List<ReferenceTypeCountResponse> items = qualityCheckService.countByReferenceTypeGrouped();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/count-by-inspector-grouped")
	public ResponseEntity<List<InspectorCountResponse>> countByInspectorGrouped(){
		List<InspectorCountResponse> items = qualityCheckService.countByInspectorGrouped();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/count-by-inspector-name-grouped")
	public ResponseEntity<List<InspectorNameCountResponse>> countByInspectorNameGrouped(){
		List<InspectorNameCountResponse> items = qualityCheckService.countByInspectorNameGrouped();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/search/count-by-date-grouped")
	public ResponseEntity<List<DateCountResponse>> countByDateGrouped(){
		List<DateCountResponse> items = qualityCheckService.countByDateGrouped();
		return ResponseEntity.ok(items);
	} 
	
}
