package com.jovan.erp_v1.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.exception.NoSuchShiftReportFoundException;
import com.jovan.erp_v1.mapper.ShiftReportMapper;
import com.jovan.erp_v1.model.ShiftReport;
import com.jovan.erp_v1.request.ShiftReportRequest;
import com.jovan.erp_v1.response.ShiftReportResponse;
import com.jovan.erp_v1.service.IShiftReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/shiftReports")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5371")
public class ShiftReportController {

	private final ShiftReportMapper shiftReportMapper;
	private final IShiftReportService iShiftReportService;
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PostMapping("/new/create-shift-report")
	public ResponseEntity<ShiftReportResponse> createReport(@Valid @RequestBody ShiftReportRequest request){
		ShiftReport report = iShiftReportService.save(request);
		return ResponseEntity.ok(shiftReportMapper.toResponse(report));
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<ShiftReportResponse> updateReport(@PathVariable Long id, @Valid @RequestBody ShiftReportRequest request){
		ShiftReport updated = iShiftReportService.update(id, request);
		return ResponseEntity.ok(shiftReportMapper.toResponse(updated));
	}
	
	@GetMapping("/shift-report/{id}")
	public ResponseEntity<ShiftReportResponse> getReportById(@PathVariable Long id){
		return ResponseEntity.ok(iShiftReportService.getById(id));
	}
	
	
	@GetMapping("/get-all-shift-reports")
	public ResponseEntity<List<ShiftReportResponse>> getAllReports(){
		List<ShiftReportResponse> lista = iShiftReportService.getAll();
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/reports/{shiftId}")
    public ResponseEntity<List<ShiftReportResponse>> getReportsByShiftId(@PathVariable Long shiftId) {
        return ResponseEntity.ok(iShiftReportService.getByShiftId(shiftId));
    }

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        iShiftReportService.delete(id);
        return ResponseEntity.noContent().build();
    }
	
	//nove metode
	
	@GetMapping("/search/description")
	public ResponseEntity<List<ShiftReportResponse>> findByDescription(@RequestParam("description") String description){
		List<ShiftReportResponse> lista = iShiftReportService.findByDescription(description);
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/search/createdBy-date-range")
	public ResponseEntity<List<ShiftReportResponse>> findByCreatedAtBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<ShiftReportResponse> lista = iShiftReportService.findByCreatedAtBetween(start, end);
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/search/createdBy-after")
	public ResponseEntity<List<ShiftReportResponse>> findByCreatedAtAfterOrEqual(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
		List<ShiftReportResponse> lista = iShiftReportService.findByCreatedAtAfterOrEqual(date);
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/search/createdBy-email")
	public ResponseEntity<List<ShiftReportResponse>> findByCreatedBy_EmailLikeIgnoreCase(@RequestParam("email") String email){
		List<ShiftReportResponse> lista = iShiftReportService.findByCreatedBy_EmailLikeIgnoreCase(email);
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/search/createdBy-phone-number")
	public ResponseEntity<List<ShiftReportResponse>> findByCreatedBy_PhoneNumberLikeIgnoreCase(@RequestParam("phoneNumber") String phoneNumber){
		List<ShiftReportResponse> lista = iShiftReportService.findByCreatedBy_PhoneNumberLikeIgnoreCase(phoneNumber);
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/search/createdBy-fullName")
	public ResponseEntity<List<ShiftReportResponse>> findByCreatedBy_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(@RequestParam("firstName") String firstName,@RequestParam("lastName") String lastName){
		List<ShiftReportResponse> lista = iShiftReportService.findByCreatedBy_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(firstName, lastName);
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/search/related-shift/end-time-before")
	public ResponseEntity<List<ShiftReportResponse>>  findByRelatedShift_EndTimeBefore(@RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time){
		List<ShiftReportResponse> lista = iShiftReportService.findByRelatedShift_EndTimeBefore(time);
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/search/related-shift/end-time-after")
	public ResponseEntity<List<ShiftReportResponse>> findByRelatedShift_EndTimeAfter(@RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time){
		List<ShiftReportResponse> lista = iShiftReportService.findByRelatedShift_EndTimeAfter(time);
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/search/related-shift/start-time-after")
	public ResponseEntity<List<ShiftReportResponse>> findByRelatedShift_StartTimeAfter(@RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time){
		List<ShiftReportResponse> lista = iShiftReportService.findByRelatedShift_StartTimeAfter(time);
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/search/related-shift/start-time-before")
	public ResponseEntity<List<ShiftReportResponse>> findByRelatedShift_StartTimeBefore(@RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time){
		List<ShiftReportResponse> lista = iShiftReportService.findByRelatedShift_StartTimeBefore(time);
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/search/related-shift/end-time-between")
	public ResponseEntity<List<ShiftReportResponse>> findByRelatedShift_EndTimeBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<ShiftReportResponse> lista = iShiftReportService.findByRelatedShift_EndTimeBetween(start, end);
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/search/related-shift/active")
	public ResponseEntity<List<ShiftReportResponse>> findRelatedShift_ActiveShifts(){
		List<ShiftReportResponse> lista = iShiftReportService.findRelatedShift_ActiveShifts();
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/search/related-shift/end-time-null")
	public ResponseEntity<List<ShiftReportResponse>> findByRelatedShift_EndTimeIsNull(){
		List<ShiftReportResponse> lista = iShiftReportService.findByRelatedShift_EndTimeIsNull();
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/search/related-shift/supervisor/{supervisorId}/start-time-range")
	public ResponseEntity<List<ShiftReportResponse>> findByRelatedShift_ShiftSupervisorIdAndStartTimeBetween(@PathVariable Long supervisorId, @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<ShiftReportResponse> lista = iShiftReportService.findByRelatedShift_ShiftSupervisorIdAndStartTimeBetween(supervisorId, start, end);
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/search/related-shift/supervisor/{supervisorId}/end-time-null")
	public ResponseEntity<List<ShiftReportResponse>> findByRelatedShift_ShiftSupervisorIdAndEndTimeIsNull(@PathVariable Long supervisorId){
		List<ShiftReportResponse> lista = iShiftReportService.findByRelatedShift_ShiftSupervisorIdAndEndTimeIsNull(supervisorId);
		return ResponseEntity.ok(lista);
	}
}

