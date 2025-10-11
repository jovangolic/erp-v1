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

import com.jovan.erp_v1.enumeration.CapacityPlanningStatus;
import com.jovan.erp_v1.request.CapacityPlanningRequest;
import com.jovan.erp_v1.response.CapacityPlanningResponse;
import com.jovan.erp_v1.save_as.CapacityPlanningSaveAsRequest;
import com.jovan.erp_v1.search_request.CapacityPlanningSearchRequest;
import com.jovan.erp_v1.service.ICapacityPlanningService;
import com.jovan.erp_v1.statistics.capacity_planning.CapacityPlanningAvailableCapacityStatDTO;
import com.jovan.erp_v1.statistics.capacity_planning.CapacityPlanningMonthlyStatDTO;
import com.jovan.erp_v1.statistics.capacity_planning.CapacityPlanningPlannedLoadStatDTO;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/capacityPlannings")
@PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
public class CapacityPlanningController {

    private final ICapacityPlanningService capacityPlanningService;

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_FULL_ACCESS)
    @PostMapping("/create/new-capacityPlanning")
    public ResponseEntity<CapacityPlanningResponse> create(@Valid @RequestBody CapacityPlanningRequest request) {
        CapacityPlanningResponse response = capacityPlanningService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<CapacityPlanningResponse> update(@PathVariable Long id,
            @Valid @RequestBody CapacityPlanningRequest request) {
        CapacityPlanningResponse response = capacityPlanningService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        capacityPlanningService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<CapacityPlanningResponse> findOne(@PathVariable Long id) {
        CapacityPlanningResponse response = capacityPlanningService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<CapacityPlanningResponse>> findAll() {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/workCenter/{workCenterId}")
    public ResponseEntity<List<CapacityPlanningResponse>> findByWorkCenter_Id(@PathVariable Long workCenterId) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByWorkCenter_Id(workCenterId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/work-center-name")
    public ResponseEntity<List<CapacityPlanningResponse>> findByWorkCenter_NameContainingIgnoreCase(
            @RequestParam("name") String name) {
        List<CapacityPlanningResponse> responses = capacityPlanningService
                .findByWorkCenter_NameContainingIgnoreCase(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/work-center-location")
    public ResponseEntity<List<CapacityPlanningResponse>> findByWorkCenter_LocationContainingIgnoreCase(
            @RequestParam("location") String location) {
        List<CapacityPlanningResponse> responses = capacityPlanningService
                .findByWorkCenter_LocationContainingIgnoreCase(location);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/date-range")
    public ResponseEntity<List<CapacityPlanningResponse>> findByDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByDateBetween(start, end);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/by-date")
    public ResponseEntity<List<CapacityPlanningResponse>> findByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByDate(date);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/date-greater-than")
    public ResponseEntity<List<CapacityPlanningResponse>> findByDateGreaterThanEqual(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByDateGreaterThanEqual(date);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/by-available-capacity")
    public ResponseEntity<List<CapacityPlanningResponse>> findByAvailableCapacity(
            @RequestParam("availableCapacity") BigDecimal availableCapacity) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByAvailableCapacity(availableCapacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/available-capacity-greater-than")
    public ResponseEntity<List<CapacityPlanningResponse>> findByAvailableCapacityGreaterThan(
            @RequestParam("availableCapacity") BigDecimal availableCapacity) {
        List<CapacityPlanningResponse> responses = capacityPlanningService
                .findByAvailableCapacityGreaterThan(availableCapacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/available-capacity-less-than")
    public ResponseEntity<List<CapacityPlanningResponse>> findByAvailableCapacityLessThan(
            @RequestParam("availableCapacity") BigDecimal availableCapacity) {
        List<CapacityPlanningResponse> responses = capacityPlanningService
                .findByAvailableCapacityLessThan(availableCapacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/by-planned-load")
    public ResponseEntity<List<CapacityPlanningResponse>> findByPlannedLoad(
            @RequestParam("plannedLoad") BigDecimal plannedLoad) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByPlannedLoad(plannedLoad);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/planned-load-greater-than")
    public ResponseEntity<List<CapacityPlanningResponse>> findByPlannedLoadGreaterThan(
            @RequestParam("plannedLoad") BigDecimal plannedLoad) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByPlannedLoadGreaterThan(plannedLoad);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/planned-load-less-than")
    public ResponseEntity<List<CapacityPlanningResponse>> findByPlannedLoadLessThan(
            @RequestParam("plannedLoad") BigDecimal plannedLoad) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByPlannedLoadLessThan(plannedLoad);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/planned-load-available-capacity")
    public ResponseEntity<List<CapacityPlanningResponse>> findByPlannedLoadAndAvailableCapacity(
            @RequestParam("plannedLoad") BigDecimal plannedLoad,
            @RequestParam("availableCapacity") BigDecimal availableCapacity) {
        List<CapacityPlanningResponse> responses = capacityPlanningService
                .findByPlannedLoadAndAvailableCapacity(plannedLoad, availableCapacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/remaining-capacity")
    public ResponseEntity<List<CapacityPlanningResponse>> findByRemainingCapacity(
            @RequestParam("remainingCapacity") BigDecimal remainingCapacity) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByRemainingCapacity(remainingCapacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/remaining-less-than")
    public ResponseEntity<List<CapacityPlanningResponse>> findWhereRemainingCapacityIsLessThanAvailableCapacity() {
        List<CapacityPlanningResponse> responses = capacityPlanningService
                .findWhereRemainingCapacityIsLessThanAvailableCapacity();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/remaining-greater-than")
    public ResponseEntity<List<CapacityPlanningResponse>> findWhereRemainingCapacityIsGreaterThanAvailableCapacity() {
        List<CapacityPlanningResponse> responses = capacityPlanningService
                .findWhereRemainingCapacityIsGreaterThanAvailableCapacity();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/all-orderBy-utilization-desc")
    public ResponseEntity<List<CapacityPlanningResponse>> findAllOrderByUtilizationDesc() {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findAllOrderByUtilizationDesc();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/load-exceeds-capacity")
    public ResponseEntity<List<CapacityPlanningResponse>> findWhereLoadExceedsCapacity() {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findWhereLoadExceedsCapacity();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/utilization-greater-than")
    public ResponseEntity<List<CapacityPlanningResponse>> findByUtilizationGreaterThan(
            @RequestParam("threshold") BigDecimal threshold) {
        List<CapacityPlanningResponse> responses = capacityPlanningService.findByUtilizationGreaterThan(threshold);
        return ResponseEntity.ok(responses);
    }

    //nove metode
    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/count/by-planned-load")
    public ResponseEntity<List<CapacityPlanningPlannedLoadStatDTO>> countCapacityPlanningByPlannedLoad(){
    	List<CapacityPlanningPlannedLoadStatDTO> items = capacityPlanningService.countCapacityPlanningByPlannedLoad();
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/count/by-available-capacity")
    public ResponseEntity<List<CapacityPlanningAvailableCapacityStatDTO>> countCapacityPlanningByAvailableCapacity(){
    	List<CapacityPlanningAvailableCapacityStatDTO> items = capacityPlanningService.countCapacityPlanningByAvailableCapacity();
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/count/by-year-and-month")
    public ResponseEntity<List<CapacityPlanningMonthlyStatDTO>> countCapacityPlanningsByYearAndMonth(){
    	List<CapacityPlanningMonthlyStatDTO> items = capacityPlanningService.countCapacityPlanningsByYearAndMonth();
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_READ_ACCESS)
    @GetMapping("/track/{id}")
    public ResponseEntity<CapacityPlanningResponse> trackCapacityPlanning(Long id){
    	CapacityPlanningResponse items = capacityPlanningService.trackCapacityPlanning(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_FULL_ACCESS)
    @PostMapping("/{id}/confirm")
    public ResponseEntity<CapacityPlanningResponse> confirmCapacityPlanning(Long id){
    	CapacityPlanningResponse items = capacityPlanningService.confirmCapacityPlanning(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_FULL_ACCESS)
    @PostMapping("/{id}/close")
    public ResponseEntity<CapacityPlanningResponse> closeCapacityPlanning(Long id){
    	CapacityPlanningResponse items = capacityPlanningService.closeCapacityPlanning(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_FULL_ACCESS)
    @PostMapping("/{id}/cancel")
    public ResponseEntity<CapacityPlanningResponse> cancelCapacityPlanning(Long id){
    	CapacityPlanningResponse items = capacityPlanningService.cancelCapacityPlanning(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_FULL_ACCESS)
    @PostMapping("/{id}/status/{status}")
    public ResponseEntity<CapacityPlanningResponse> changeStatus(Long id, CapacityPlanningStatus status){
    	CapacityPlanningResponse items = capacityPlanningService.changeStatus(id, status);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_FULL_ACCESS)
    @PostMapping("/save")
    public ResponseEntity<CapacityPlanningResponse> saveCapacityPlanning(CapacityPlanningRequest request){
    	CapacityPlanningResponse items = capacityPlanningService.saveCapacityPlanning(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_FULL_ACCESS)
    @PostMapping("/save-as")
    public ResponseEntity<CapacityPlanningResponse> saveAs(CapacityPlanningSaveAsRequest request){
    	CapacityPlanningResponse items = capacityPlanningService.saveAs(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_FULL_ACCESS)
    @PostMapping("/save-all")
    public ResponseEntity<List<CapacityPlanningResponse>> saveAll(List<CapacityPlanningRequest> requests){
    	List<CapacityPlanningResponse> items = capacityPlanningService.saveAll(requests);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.CAPACITY_PLANNING_FULL_ACCESS)
    @PostMapping("/general-search")
    public ResponseEntity<List<CapacityPlanningResponse>> generalSearch(CapacityPlanningSearchRequest request){
    	List<CapacityPlanningResponse> items = capacityPlanningService.generalSearch(request);
    	return ResponseEntity.ok(items);
    }
}
