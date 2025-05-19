package com.jovan.erp_v1.controller;

import java.util.List;

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
		return ResponseEntity.ok(shiftReportMapper.toDto(report));
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<ShiftReportResponse> updateReport(@PathVariable Long id, @Valid @RequestBody ShiftReportRequest request){
		ShiftReport updated = iShiftReportService.update(id, request);
		return ResponseEntity.ok(shiftReportMapper.toDto(updated));
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
	
	
}

