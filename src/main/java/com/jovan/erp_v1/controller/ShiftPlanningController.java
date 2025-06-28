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

import com.jovan.erp_v1.enumeration.ShiftType;
import com.jovan.erp_v1.request.ShiftPlanningRequest;
import com.jovan.erp_v1.response.ShiftPlanningResponse;
import com.jovan.erp_v1.service.IShiftPlanningService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shiftPlannings")
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','STORAGE_FOREMAN')")
public class ShiftPlanningController {

    private final IShiftPlanningService shiftPlanningService;

    @PostMapping("/create/new-shiftPlanning")
    public ResponseEntity<ShiftPlanningResponse> create(@Valid @RequestBody ShiftPlanningRequest request) {
        ShiftPlanningResponse response = shiftPlanningService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ShiftPlanningResponse> update(@PathVariable Long id,
            @Valid @RequestBody ShiftPlanningRequest request) {
        ShiftPlanningResponse response = shiftPlanningService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        shiftPlanningService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-one/{id}")
    public ResponseEntity<ShiftPlanningResponse> findOne(@PathVariable Long id) {
        ShiftPlanningResponse response = shiftPlanningService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<ShiftPlanningResponse>> findAll() {
        List<ShiftPlanningResponse> response = shiftPlanningService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/workCenter-name")
    public ResponseEntity<List<ShiftPlanningResponse>> findByWorkCenter_NameContainingIgnoreCase(
            @RequestParam("name") String name) {
        List<ShiftPlanningResponse> response = shiftPlanningService.findByWorkCenter_NameContainingIgnoreCase(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/workCenter-capacity")
    public ResponseEntity<List<ShiftPlanningResponse>> findByWorkCenter_Capacity(
            @RequestParam("capacity") BigDecimal capacity) {
        List<ShiftPlanningResponse> response = shiftPlanningService.findByWorkCenter_Capacity(capacity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/workCenter-location")
    public ResponseEntity<List<ShiftPlanningResponse>> findByWorkCenter_LocationContainingIgnoreCase(
            @RequestParam("location") String location) {
        List<ShiftPlanningResponse> response = shiftPlanningService
                .findByWorkCenter_LocationContainingIgnoreCase(location);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{id}/all")
    public ResponseEntity<List<ShiftPlanningResponse>> findByEmployee_Id(@PathVariable Long id) {
        List<ShiftPlanningResponse> response = shiftPlanningService.findByEmployee_Id(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-email")
    public ResponseEntity<List<ShiftPlanningResponse>> findByEmployee_Email(@RequestParam("email") String email) {
        List<ShiftPlanningResponse> response = shiftPlanningService.findByEmployee_Email(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-username")
    public ResponseEntity<List<ShiftPlanningResponse>> findByEmployee_UsernameContainingIgnoreCase(
            @RequestParam("username") String username) {
        List<ShiftPlanningResponse> response = shiftPlanningService
                .findByEmployee_UsernameContainingIgnoreCase(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-phone-number")
    public ResponseEntity<List<ShiftPlanningResponse>> findByEmployee_PhoneNumber(
            @RequestParam("phoneNumber") String phoneNumber) {
        List<ShiftPlanningResponse> response = shiftPlanningService.findByEmployee_PhoneNumber(phoneNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<ShiftPlanningResponse>> findByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ShiftPlanningResponse> response = shiftPlanningService.findByDate(date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ShiftPlanningResponse>> findByDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<ShiftPlanningResponse> response = shiftPlanningService.findByDateBetween(start, end);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-greater-than")
    public ResponseEntity<List<ShiftPlanningResponse>> findByDateGreaterThanEqual(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ShiftPlanningResponse> response = shiftPlanningService.findByDateGreaterThanEqual(date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders-after-date")
    public ResponseEntity<List<ShiftPlanningResponse>> findOrdersWithStartDateAfterOrEqual(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ShiftPlanningResponse> response = shiftPlanningService.findOrdersWithStartDateAfterOrEqual(date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-shiftType")
    public ResponseEntity<List<ShiftPlanningResponse>> findByShiftType(@RequestParam("shiftType") ShiftType shiftType) {
        List<ShiftPlanningResponse> response = shiftPlanningService.findByShiftType(shiftType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-assigned")
    public ResponseEntity<List<ShiftPlanningResponse>> findByAssigned(@RequestParam("assigned") boolean assigned) {
        List<ShiftPlanningResponse> response = shiftPlanningService.findByAssigned(assigned);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{employeeIds}/assigned")
    public ResponseEntity<List<ShiftPlanningResponse>> findByEmployee_IdAndAssignedTrue(
            @PathVariable Long employeeIds) {
        List<ShiftPlanningResponse> response = shiftPlanningService.findByEmployee_IdAndAssignedTrue(employeeIds);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{employeeId}/shiftType")
    public ResponseEntity<List<ShiftPlanningResponse>> findByEmployee_IdAndShiftType(@PathVariable Long employeeId,
            @RequestParam("shiftType") ShiftType shiftType) {
        List<ShiftPlanningResponse> response = shiftPlanningService.findByEmployee_IdAndShiftType(employeeId,
                shiftType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/workCenter/{workcenterIds}/date")
    public ResponseEntity<List<ShiftPlanningResponse>> findByWorkCenter_IdAndDateAfterAndAssignedFalse(
            @PathVariable Long workCenterIds,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ShiftPlanningResponse> response = shiftPlanningService
                .findByWorkCenter_IdAndDateAfterAndAssignedFalse(workCenterIds, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{employeeId}/dateRange")
    public ResponseEntity<List<ShiftPlanningResponse>> findShiftsForEmployeeBetweenDates(@PathVariable Long employeeId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<ShiftPlanningResponse> response = shiftPlanningService.findShiftsForEmployeeBetweenDates(employeeId, start,
                end);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{employeeId}/date-shiftType")
    public ResponseEntity<Boolean> existsByEmployee_IdAndDateAndShiftType(Long employeeId, LocalDate date,
            @RequestParam("shiftType") ShiftType shiftType) {
        Boolean response = shiftPlanningService.existsByEmployee_IdAndDateAndShiftType(employeeId, date, shiftType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-employee-firstLastName")
    public ResponseEntity<List<ShiftPlanningResponse>> findByEmployeeFirstAndLastName(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName) {
        List<ShiftPlanningResponse> response = shiftPlanningService.findByEmployeeFirstAndLastName(firstName,
                lastName);
        return ResponseEntity.ok(response);
    }
}
