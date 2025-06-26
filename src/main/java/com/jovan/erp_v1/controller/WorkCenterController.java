package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.util.List;

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

import com.jovan.erp_v1.request.WorkCenterRequest;
import com.jovan.erp_v1.response.WorkCenterResponse;
import com.jovan.erp_v1.service.IWorkCenterService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workCenters")
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','MANAGER','STORAGE_FOREMAN')")
public class WorkCenterController {

    private final IWorkCenterService workCenterService;

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','MANAGER','STORAGE_FOREMAN')")
    @PostMapping("/create/new-workCenter")
    public ResponseEntity<WorkCenterResponse> create(@Valid @RequestBody WorkCenterRequest request) {
        WorkCenterResponse response = workCenterService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','MANAGER','STORAGE_FOREMAN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<WorkCenterResponse> update(@PathVariable Long id,
            @Valid @RequestBody WorkCenterRequest request) {
        WorkCenterResponse response = workCenterService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','MANAGER','STORAGE_FOREMAN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workCenterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','MANAGER','STORAGE_FOREMAN')")
    @GetMapping("/find-one/{id}")
    public ResponseEntity<WorkCenterResponse> findOne(@PathVariable Long id) {
        WorkCenterResponse response = workCenterService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','MANAGER','STORAGE_FOREMAN')")
    @GetMapping("/find-all")
    public ResponseEntity<List<WorkCenterResponse>> findAll() {
        List<WorkCenterResponse> responses = workCenterService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','MANAGER','STORAGE_FOREMAN')")
    @GetMapping("/by-name")
    public ResponseEntity<List<WorkCenterResponse>> findByName(@RequestParam("name") String name) {
        List<WorkCenterResponse> responses = workCenterService.findByName(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','MANAGER','STORAGE_FOREMAN')")
    @GetMapping("/by-capacity")
    public ResponseEntity<List<WorkCenterResponse>> findByCapacity(@RequestParam("capacity") BigDecimal capacity) {
        List<WorkCenterResponse> responses = workCenterService.findByCapacity(capacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','MANAGER','STORAGE_FOREMAN')")
    @GetMapping("/by-location")
    public ResponseEntity<List<WorkCenterResponse>> findByLocation(@RequestParam("location") String location) {
        List<WorkCenterResponse> responses = workCenterService.findByLocation(location);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','MANAGER','STORAGE_FOREMAN')")
    @GetMapping("/by-name-location")
    public ResponseEntity<List<WorkCenterResponse>> findByNameAndLocation(@RequestParam("name") String name,
            @RequestParam("location") String location) {
        List<WorkCenterResponse> responses = workCenterService.findByNameAndLocation(name, location);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','MANAGER','STORAGE_FOREMAN')")
    @GetMapping("/capacityGreaterThan")
    public ResponseEntity<List<WorkCenterResponse>> findByCapacityGreaterThan(
            @RequestParam("capacity") BigDecimal capacity) {
        List<WorkCenterResponse> responses = workCenterService.findByCapacityGreaterThan(capacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','MANAGER','STORAGE_FOREMAN')")
    @GetMapping("/capacityLessThan")
    public ResponseEntity<List<WorkCenterResponse>> findByCapacityLessThan(
            @RequestParam("capacity") BigDecimal capacity) {
        List<WorkCenterResponse> responses = workCenterService.findByCapacityLessThan(capacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','MANAGER','STORAGE_FOREMAN')")
    @GetMapping("/search-by-name")
    public ResponseEntity<List<WorkCenterResponse>> findByNameContainingIgnoreCase(@RequestParam("name") String name) {
        List<WorkCenterResponse> responses = workCenterService.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','MANAGER','STORAGE_FOREMAN')")
    @GetMapping("/search-by-location")
    public ResponseEntity<List<WorkCenterResponse>> findByLocationContainingIgnoreCase(
            @RequestParam("location") String location) {
        List<WorkCenterResponse> responses = workCenterService.findByLocationContainingIgnoreCase(location);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','MANAGER','STORAGE_FOREMAN')")
    @GetMapping("/capacity-between")
    public ResponseEntity<List<WorkCenterResponse>> findByCapacityBetween(@RequestParam("min") BigDecimal min,
            @RequestParam("max") BigDecimal max) {
        List<WorkCenterResponse> responses = workCenterService.findByCapacityBetween(min, max);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','MANAGER','STORAGE_FOREMAN')")
    @GetMapping("/by-locationOrder-desc")
    public ResponseEntity<List<WorkCenterResponse>> findByLocationOrderByCapacityDesc(
            @RequestParam("location") String location) {
        List<WorkCenterResponse> responses = workCenterService.findByLocationOrderByCapacityDesc(location);
        return ResponseEntity.ok(responses);
    }
}
