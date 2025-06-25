package com.jovan.erp_v1.controller;

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

    @PostMapping("/create/new-workCenter")
    public ResponseEntity<WorkCenterResponse> create(@Valid @RequestBody WorkCenterRequest request) {
        WorkCenterResponse response = workCenterService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<WorkCenterResponse> update(@PathVariable Long id,
            @Valid @RequestBody WorkCenterRequest request) {
        WorkCenterResponse response = workCenterService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workCenterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-one/{id}")
    public ResponseEntity<WorkCenterResponse> findOne(@PathVariable Long id) {
        WorkCenterResponse response = workCenterService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<WorkCenterResponse>> findAll() {
        List<WorkCenterResponse> responses = workCenterService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<WorkCenterResponse>> findByName(@RequestParam("name") String name) {
        List<WorkCenterResponse> responses = workCenterService.findByName(name);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-capacity")
    public ResponseEntity<List<WorkCenterResponse>> findByCapacity(@RequestParam("capacity") Integer capacity) {
        List<WorkCenterResponse> responses = workCenterService.findByCapacity(capacity);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-location")
    public ResponseEntity<List<WorkCenterResponse>> findByLocation(@RequestParam("location") String location) {
        List<WorkCenterResponse> responses = workCenterService.findByLocation(location);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-name-location")
    public ResponseEntity<List<WorkCenterResponse>> findByNameAndLocation(@RequestParam("name") String name,
            @RequestParam("location") String location) {
        List<WorkCenterResponse> responses = workCenterService.findByNameAndLocation(name, location);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/capacityGreaterThan")
    public ResponseEntity<List<WorkCenterResponse>> findByCapacityGreaterThan(
            @RequestParam("capacity") Integer capacity) {
        List<WorkCenterResponse> responses = workCenterService.findByCapacityGreaterThan(capacity);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/capacityLessThan")
    public ResponseEntity<List<WorkCenterResponse>> findByCapacityLessThan(@RequestParam("capacity") Integer capacity) {
        List<WorkCenterResponse> responses = workCenterService.findByCapacityLessThan(capacity);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<List<WorkCenterResponse>> findByNameContainingIgnoreCase(@RequestParam("name") String name) {
        List<WorkCenterResponse> responses = workCenterService.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search-by-location")
    public ResponseEntity<List<WorkCenterResponse>> findByLocationContainingIgnoreCase(
            @RequestParam("location") String location) {
        List<WorkCenterResponse> responses = workCenterService.findByLocationContainingIgnoreCase(location);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/capacity-between")
    public ResponseEntity<List<WorkCenterResponse>> findByCapacityBetween(@RequestParam("min") Integer min,
            @RequestParam("max") Integer max) {
        List<WorkCenterResponse> responses = workCenterService.findByCapacityBetween(min, max);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-locationOrder-desc")
    public ResponseEntity<List<WorkCenterResponse>> findByLocationOrderByCapacityDesc(
            @RequestParam("location") String location) {
        List<WorkCenterResponse> responses = workCenterService.findByLocationOrderByCapacityDesc(location);
        return ResponseEntity.ok(responses);
    }
}
