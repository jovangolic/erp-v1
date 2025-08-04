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

import com.jovan.erp_v1.request.CapacityPlanningRequest;
import com.jovan.erp_v1.response.CapacityPlanningResponse;
import com.jovan.erp_v1.service.ICapacityPlanningService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/capacityPlannings")
@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN', 'ROLE_PRODUCTION_PLANNER')")
public class CapacityPlanningController {

    private final ICapacityPlanningService capacityPlanningService;

    @PostMapping("/create/new-capacityPlanning")
    public ResponseEntity<CapacityPlanningResponse> create(@Valid @RequestBody CapacityPlanningRequest request) {
        CapacityPlanningResponse response = capacityPlanningService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CapacityPlanningResponse> update(@PathVariable Long id,
            @Valid @RequestBody CapacityPlanningRequest request) {
        CapacityPlanningResponse response = capacityPlanningService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        capacityPlanningService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-one/{id}")
    public ResponseEntity<CapacityPlanningResponse> findOne(@PathVariable Long id) {
        CapacityPlanningResponse response = capacityPlanningService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<CapacityPlanningResponse>> findAll() {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/workCenter/{workCenterId}")
    public ResponseEntity<List<CapacityPlanningResponse>> findByWorkCenter_Id(@PathVariable Long workCenterId) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByWorkCenter_Id(workCenterId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/workCenterName")
    public ResponseEntity<List<CapacityPlanningResponse>> findByWorkCenter_NameContainingIgnoreCase(
            @RequestParam("name") String name) {
        List<CapacityPlanningResponse> responses = capacityPlanningService
                .findByWorkCenter_NameContainingIgnoreCase(name);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/workCenterLocation")
    public ResponseEntity<List<CapacityPlanningResponse>> findByWorkCenter_LocationContainingIgnoreCase(
            @RequestParam("location") String location) {
        List<CapacityPlanningResponse> responses = capacityPlanningService
                .findByWorkCenter_LocationContainingIgnoreCase(location);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<CapacityPlanningResponse>> findByDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByDateBetween(start, end);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<CapacityPlanningResponse>> findByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByDate(date);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/date-greaterThan")
    public ResponseEntity<List<CapacityPlanningResponse>> findByDateGreaterThanEqual(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByDateGreaterThanEqual(date);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-availableCapacity")
    public ResponseEntity<List<CapacityPlanningResponse>> findByAvailableCapacity(
            @RequestParam("availableCapacity") BigDecimal availableCapacity) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByAvailableCapacity(availableCapacity);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/availableCapacityGreaterThan")
    public ResponseEntity<List<CapacityPlanningResponse>> findByAvailableCapacityGreaterThan(
            @RequestParam("availableCapacity") BigDecimal availableCapacity) {
        List<CapacityPlanningResponse> responses = capacityPlanningService
                .findByAvailableCapacityGreaterThan(availableCapacity);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/availableCapacityLessThan")
    public ResponseEntity<List<CapacityPlanningResponse>> findByAvailableCapacityLessThan(
            @RequestParam("availableCapacity") BigDecimal availableCapacity) {
        List<CapacityPlanningResponse> responses = capacityPlanningService
                .findByAvailableCapacityLessThan(availableCapacity);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-plannedLoad")
    public ResponseEntity<List<CapacityPlanningResponse>> findByPlannedLoad(
            @RequestParam("plannedLoad") BigDecimal plannedLoad) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByPlannedLoad(plannedLoad);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/plannedLoadGreaterThan")
    public ResponseEntity<List<CapacityPlanningResponse>> findByPlannedLoadGreaterThan(
            @RequestParam("plannedLoad") BigDecimal plannedLoad) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByPlannedLoadGreaterThan(plannedLoad);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/plannedLoadLessThan")
    public ResponseEntity<List<CapacityPlanningResponse>> findByPlannedLoadLessThan(
            @RequestParam("plannedLoad") BigDecimal plannedLoad) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByPlannedLoadLessThan(plannedLoad);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/plannedLoad-availableCapacity")
    public ResponseEntity<List<CapacityPlanningResponse>> findByPlannedLoadAndAvailableCapacity(
            @RequestParam("plannedLoad") BigDecimal plannedLoad,
            @RequestParam("availableCapacity") BigDecimal availableCapacity) {
        List<CapacityPlanningResponse> responses = capacityPlanningService
                .findByPlannedLoadAndAvailableCapacity(plannedLoad, availableCapacity);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/remainingCapacity")
    public ResponseEntity<List<CapacityPlanningResponse>> findByRemainingCapacity(
            @RequestParam("remainingCapacity") BigDecimal remainingCapacity) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByRemainingCapacity(remainingCapacity);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/remainingLessThan")
    public ResponseEntity<List<CapacityPlanningResponse>> findWhereRemainingCapacityIsLessThanAvailableCapacity() {
        List<CapacityPlanningResponse> responses = capacityPlanningService
                .findWhereRemainingCapacityIsLessThanAvailableCapacity();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/remainingGreaterThan")
    public ResponseEntity<List<CapacityPlanningResponse>> findWhereRemainingCapacityIsGreaterThanAvailableCapacity() {
        List<CapacityPlanningResponse> responses = capacityPlanningService
                .findWhereRemainingCapacityIsGreaterThanAvailableCapacity();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/allOrderByUtilizationDesc")
    public ResponseEntity<List<CapacityPlanningResponse>> findAllOrderByUtilizationDesc() {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findAllOrderByUtilizationDesc();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/loadExceedsCapacity")
    public ResponseEntity<List<CapacityPlanningResponse>> findWhereLoadExceedsCapacity() {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findWhereLoadExceedsCapacity();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/utilizationGreaterThan")
    public ResponseEntity<List<CapacityPlanningResponse>> findByUtilizationGreaterThan(
            @RequestParam("threshold") BigDecimal threshold) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByUtilizationGreaterThan(threshold);
        return ResponseEntity.ok(responses);
    }

}
