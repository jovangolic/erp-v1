package com.jovan.erp_v1.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.request.ShiftRequest;
import com.jovan.erp_v1.response.ShiftResponse;
import com.jovan.erp_v1.service.IShiftService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/shifts")
@CrossOrigin("http://localhost:5371")
@RequiredArgsConstructor
@PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
public class ShiftController {

	private final IShiftService iShiftService;

	@PreAuthorize(RoleGroups.SHIFT_FULL_ACCESS)
	@PostMapping("/new/create-shift")
	public ResponseEntity<ShiftResponse> createShift(@Valid @RequestBody ShiftRequest request){
		ShiftResponse shift = iShiftService.save(request);
		return ResponseEntity.ok(shift);
	}
	
	@PreAuthorize(RoleGroups.SHIFT_FULL_ACCESS)
	@PutMapping("/update/{id}")
	public ResponseEntity<ShiftResponse> updateShift(@PathVariable Long id, @Valid @RequestBody ShiftRequest request){
		ShiftResponse updated = iShiftService.update(id, request);
		return ResponseEntity.ok(updated);
	}
	
	@PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/get-one/{id}")
    public ResponseEntity<ShiftResponse> getShiftById(@PathVariable Long id) {
        return ResponseEntity.ok(iShiftService.getById(id));
    }

	@PreAuthorize(RoleGroups.SHIFT_FULL_ACCESS)
    @DeleteMapping("/delete/shift{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        iShiftService.delete(id);
        return ResponseEntity.noContent().build();
    }
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/get-all")
	public ResponseEntity<List<ShiftResponse>> getAllShifts() {
		List<ShiftResponse> allShift = iShiftService.getAll();
		return ResponseEntity.ok(allShift);
	}
	
	//nove metode
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/end-time-before")
	public ResponseEntity<List<ShiftResponse>> findByEndTimeBefore(@RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time){
		List<ShiftResponse> allShift = iShiftService.findByEndTimeBefore(time);
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/end-time-after")
	public ResponseEntity<List<ShiftResponse>> findByEndTimeAfter(@RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time){
		List<ShiftResponse> allShift = iShiftService.findByEndTimeAfter(time);
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/start-time-after")
	public ResponseEntity<List<ShiftResponse>> findByStartTimeAfter(@RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time){
		List<ShiftResponse> allShift = iShiftService.findByStartTimeAfter(time);
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/start-time-before")
	public ResponseEntity<List<ShiftResponse>> findByStartTimeBefore(@RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time){
		List<ShiftResponse> allShift = iShiftService.findByStartTimeBefore(time);
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/end-time-between")
	public ResponseEntity<List<ShiftResponse>> findByEndTimeBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<ShiftResponse> allShift = iShiftService.findByEndTimeBetween(start, end);
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/active-shifts")
	public ResponseEntity<List<ShiftResponse>> findActiveShifts(){
		List<ShiftResponse> allShift = iShiftService.findActiveShifts();
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/end-time-null")
	public ResponseEntity<List<ShiftResponse>> findByEndTimeIsNull(){
		List<ShiftResponse> allShift = iShiftService.findByEndTimeIsNull();
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/supervisor/{supervisorId}/start-between")
	public ResponseEntity<List<ShiftResponse>> findByShiftSupervisorIdAndStartTimeBetween(@PathVariable Long supervisorId,
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<ShiftResponse> allShift = iShiftService.findByShiftSupervisorIdAndStartTimeBetween(supervisorId, start, end);
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/supervisor/{supervisorId}/end-time-null")
	public ResponseEntity<List<ShiftResponse>> findByShiftSupervisorIdAndEndTimeIsNull(@PathVariable Long supervisorId){
		List<ShiftResponse> allShift = iShiftService.findByShiftSupervisorIdAndEndTimeIsNull(supervisorId);
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/count-start-date")
	public ResponseEntity<Integer> countShiftsByStartDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
		Integer allShift = iShiftService.countShiftsByStartDate(date);
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/count-end-date")
	public ResponseEntity<Integer> countShiftsByEndDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
		Integer allShift = iShiftService.countShiftsByEndDate(date);
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/supervisor/{supervisorId}/current")
	public ResponseEntity<List<ShiftResponse>> findCurrentShiftBySupervisor(@PathVariable Long supervisorId){
		List<ShiftResponse> allShift = iShiftService.findCurrentShiftBySupervisor(supervisorId);
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/shift-longer-than")
	public ResponseEntity<List<ShiftResponse>> findShiftsLongerThan(@RequestParam("hours") Integer hours){
		List<ShiftResponse> allShift = iShiftService.findShiftsLongerThan(hours);
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/overlapping-shifts")
	public ResponseEntity<List<ShiftResponse>> findShiftsOverlappingPeriod(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
		List<ShiftResponse> allShift = iShiftService.findShiftsOverlappingPeriod(start, end);
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/night-shifts")
	public ResponseEntity<List<ShiftResponse>> findNightShifts(){
		List<ShiftResponse> allShift = iShiftService.findNightShifts();
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/future-shifts")
	public ResponseEntity<List<ShiftResponse>> findFutureShifts(@RequestParam("now") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime now){
		List<ShiftResponse> allShift = iShiftService.findFutureShifts(now);
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/future")
	public ResponseEntity<List<ShiftResponse>> findAllFutureShifts(){
		List<ShiftResponse> allShift = iShiftService.findAllFutureShifts();
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/supervisor/email")
	public ResponseEntity<List<ShiftResponse>> findByShiftSupervisor_EmailLikeIgnoreCase(@RequestParam("email") String email){
		List<ShiftResponse> allShift = iShiftService.findByShiftSupervisor_EmailLikeIgnoreCase(email);
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/supervisor/phone-number")
	public ResponseEntity<List<ShiftResponse>> findByShiftSupervisorPhoneNumberLikeIgnoreCase(@RequestParam("phoneNumber") String phoneNumber){
		List<ShiftResponse> allShift = iShiftService.findByShiftSupervisorPhoneNumberLikeIgnoreCase(phoneNumber);
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/search/supervisor/fullName")
	public ResponseEntity<List<ShiftResponse>> findByShiftSupervisor_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName){
		List<ShiftResponse> allShift = iShiftService.findByShiftSupervisor_FirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(firstName, lastName);
		return ResponseEntity.ok(allShift);
	}
	
    @PreAuthorize(RoleGroups.SHIFT_READ_ACCESS)
	@GetMapping("/active/{supervisorId}")
	public ResponseEntity<Boolean> hasActiveShift(@PathVariable Long supervisorId){
		Boolean allShift = iShiftService.hasActiveShift(supervisorId);
		return ResponseEntity.ok(allShift);
	}
	
}
