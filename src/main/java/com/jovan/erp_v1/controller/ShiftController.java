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

import com.jovan.erp_v1.exception.NoSuchShiftErrorException;
import com.jovan.erp_v1.mapper.ShiftMapper;
import com.jovan.erp_v1.model.Shift;
import com.jovan.erp_v1.request.ShiftRequest;
import com.jovan.erp_v1.response.ShiftResponse;
import com.jovan.erp_v1.service.IShiftService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/shifts")
@CrossOrigin("http://localhost:5371")
@RequiredArgsConstructor
public class ShiftController {

	
	private final IShiftService iShiftService;
	private final ShiftMapper shiftMapper;
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PostMapping("/new/create-shift")
	public ResponseEntity<ShiftResponse> createShift(@Valid @RequestBody ShiftRequest request){
		Shift shift = iShiftService.save(request);
		return ResponseEntity.ok(shiftMapper.toDto(shift));
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<ShiftResponse> updateShift(@PathVariable Long id, @Valid @RequestBody ShiftRequest request){
		Shift updated = iShiftService.update(id, request);
		return ResponseEntity.ok(shiftMapper.toDto(updated));
	}
	
	
	@GetMapping("/get-one/{id}")
    public ResponseEntity<ShiftResponse> getShiftById(@PathVariable Long id) {
        return ResponseEntity.ok(iShiftService.getById(id));
    }

	@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
    @DeleteMapping("/delete/shift{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        iShiftService.delete(id);
        return ResponseEntity.noContent().build();
    }
	
	@GetMapping("/get-all")
	public ResponseEntity<List<ShiftResponse>> getAllShifts() {
		List<ShiftResponse> allShift = iShiftService.getAll();
		return ResponseEntity.ok(allShift);
	}
	
}
