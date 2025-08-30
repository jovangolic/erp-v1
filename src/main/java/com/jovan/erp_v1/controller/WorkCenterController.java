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

import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.request.WorkCenterRequest;
import com.jovan.erp_v1.response.CountWorkCenterCapacityResponse;
import com.jovan.erp_v1.response.CountWorkCenterResultResponse;
import com.jovan.erp_v1.response.CountWorkCentersByStorageStatusResponse;
import com.jovan.erp_v1.response.CountWorkCentersByStorageTypeResponse;
import com.jovan.erp_v1.response.WorkCenterResponse;
import com.jovan.erp_v1.service.IWorkCenterService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workCenters")
@PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
public class WorkCenterController {

    private final IWorkCenterService workCenterService;

    @PreAuthorize(RoleGroups.WORK_CENTER_FULL_ACCESS)
    @PostMapping("/create/new-workCenter")
    public ResponseEntity<WorkCenterResponse> create(@Valid @RequestBody WorkCenterRequest request) {
        WorkCenterResponse response = workCenterService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<WorkCenterResponse> update(@PathVariable Long id,
            @Valid @RequestBody WorkCenterRequest request) {
        WorkCenterResponse response = workCenterService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workCenterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<WorkCenterResponse> findOne(@PathVariable Long id) {
        WorkCenterResponse response = workCenterService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<WorkCenterResponse>> findAll() {
        List<WorkCenterResponse> responses = workCenterService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/by-name")
    public ResponseEntity<List<WorkCenterResponse>> findByName(@RequestParam("name") String name) {
        List<WorkCenterResponse> responses = workCenterService.findByName(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/by-capacity")
    public ResponseEntity<List<WorkCenterResponse>> findByCapacity(@RequestParam("capacity") BigDecimal capacity) {
        List<WorkCenterResponse> responses = workCenterService.findByCapacity(capacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/by-location")
    public ResponseEntity<List<WorkCenterResponse>> findByLocation(@RequestParam("location") String location) {
        List<WorkCenterResponse> responses = workCenterService.findByLocation(location);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/by-name-location")
    public ResponseEntity<List<WorkCenterResponse>> findByNameAndLocation(@RequestParam("name") String name,
            @RequestParam("location") String location) {
        List<WorkCenterResponse> responses = workCenterService.findByNameAndLocation(name, location);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/capacityGreaterThan")
    public ResponseEntity<List<WorkCenterResponse>> findByCapacityGreaterThan(
            @RequestParam("capacity") BigDecimal capacity) {
        List<WorkCenterResponse> responses = workCenterService.findByCapacityGreaterThan(capacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/capacityLessThan")
    public ResponseEntity<List<WorkCenterResponse>> findByCapacityLessThan(
            @RequestParam("capacity") BigDecimal capacity) {
        List<WorkCenterResponse> responses = workCenterService.findByCapacityLessThan(capacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search-by-name")
    public ResponseEntity<List<WorkCenterResponse>> findByNameContainingIgnoreCase(@RequestParam("name") String name) {
        List<WorkCenterResponse> responses = workCenterService.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search-by-location")
    public ResponseEntity<List<WorkCenterResponse>> findByLocationContainingIgnoreCase(
            @RequestParam("location") String location) {
        List<WorkCenterResponse> responses = workCenterService.findByLocationContainingIgnoreCase(location);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/capacity-between")
    public ResponseEntity<List<WorkCenterResponse>> findByCapacityBetween(@RequestParam("min") BigDecimal min,
            @RequestParam("max") BigDecimal max) {
        List<WorkCenterResponse> responses = workCenterService.findByCapacityBetween(min, max);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/by-locationOrder-desc")
    public ResponseEntity<List<WorkCenterResponse>> findByLocationOrderByCapacityDesc(
            @RequestParam("location") String location) {
        List<WorkCenterResponse> responses = workCenterService.findByLocationOrderByCapacityDesc(location);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/localStorage/{localStorageId}")
    public ResponseEntity<List<WorkCenterResponse>> findByLocalStorage_Id(@PathVariable Long localStorageId) {
        List<WorkCenterResponse> responses = workCenterService.findByLocalStorage_Id(localStorageId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/by-localStorageName")
    public ResponseEntity<List<WorkCenterResponse>> findByLocalStorage_NameContainingIgnoreCase(
            @RequestParam("localStorageName") String localStorageName) {
        List<WorkCenterResponse> responses = workCenterService
                .findByLocalStorage_NameContainingIgnoreCase(localStorageName);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/by-localStorageLocation")
    public ResponseEntity<List<WorkCenterResponse>> findByLocalStorage_LocationContainingIgnoreCase(
            @RequestParam("localStorageLocation") String localStorageLocation) {
        List<WorkCenterResponse> responses = workCenterService
                .findByLocalStorage_LocationContainingIgnoreCase(localStorageLocation);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/localStorage-capacity")
    public ResponseEntity<List<WorkCenterResponse>> findByLocalStorage_Capacity(
            @RequestParam("capacity") BigDecimal capacity) {
        List<WorkCenterResponse> responses = workCenterService.findByLocalStorage_Capacity(capacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/localStorage-capacity-less-than")
    public ResponseEntity<List<WorkCenterResponse>> findByLocalStorage_CapacityLessThan(
            @RequestParam("capacity") BigDecimal capacity) {
        List<WorkCenterResponse> responses = workCenterService.findByLocalStorage_CapacityLessThan(capacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/localStorage-capacity-greater-than")
    public ResponseEntity<List<WorkCenterResponse>> findByLocalStorage_CapacityGreaterThan(
            @RequestParam("capacity") BigDecimal capacity) {
        List<WorkCenterResponse> responses = workCenterService.findByLocalStorage_CapacityGreaterThan(capacity);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/localStorage-type")
    public ResponseEntity<List<WorkCenterResponse>> findByLocalStorage_Type(
            @RequestParam("localStorageType") StorageType localStorageType) {
        List<WorkCenterResponse> responses = workCenterService.findByLocalStorage_Type(localStorageType);
        return ResponseEntity.ok(responses);
    }
    
    //nove metode
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/filter")
    public ResponseEntity<List<WorkCenterResponse>> filter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) BigDecimal capacityMin,
            @RequestParam(required = false) BigDecimal capacityMax,
            @RequestParam(required = false) StorageType type,
            @RequestParam(required = false) StorageStatus status
    ) {
        List<WorkCenterResponse> result = workCenterService.filterWorkCenters(name, location, capacityMin, capacityMax, type, status);
        return ResponseEntity.ok(result);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/storage-type-production")
    public ResponseEntity<List<WorkCenterResponse>> findByTypeProduction(){
    	List<WorkCenterResponse> responses = workCenterService.findByTypeProduction();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/storage-type-distribution")
    public ResponseEntity<List<WorkCenterResponse>> findByTypeDistribution(){
    	List<WorkCenterResponse> responses = workCenterService.findByTypeDistribution();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/storage-type-open")
    public ResponseEntity<List<WorkCenterResponse>> findByTypeOpen(){
    	List<WorkCenterResponse> responses = workCenterService.findByTypeOpen();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/storage-type-closed")
    public ResponseEntity<List<WorkCenterResponse>> findByTypeClosed(){
    	List<WorkCenterResponse> responses = workCenterService.findByTypeClosed();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/storage-type-interim")
    public ResponseEntity<List<WorkCenterResponse>> findByTypeInterim(){
    	List<WorkCenterResponse> responses = workCenterService.findByTypeInterim();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/storage-type-available")
    public ResponseEntity<List<WorkCenterResponse>> findByTypeAvailable(){
    	List<WorkCenterResponse> responses = workCenterService.findByTypeAvailable();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/storage-status")
    public ResponseEntity<List<WorkCenterResponse>> findByLocalStorage_Status(@RequestParam("status") StorageStatus status){
    	List<WorkCenterResponse> responses = workCenterService.findByLocalStorage_Status(status);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/storage-status-active")
    public ResponseEntity<List<WorkCenterResponse>> findByStatusActive(){
    	List<WorkCenterResponse> responses = workCenterService.findByStatusActive();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/storage-status-under-maintenance")
    public ResponseEntity<List<WorkCenterResponse>> findByStatusUnder_Maintenance(){
    	List<WorkCenterResponse> responses = workCenterService.findByStatusUnder_Maintenance();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/storage-status-decommissioned")
    public ResponseEntity<List<WorkCenterResponse>> findByStatusDecommissioned(){
    	List<WorkCenterResponse> responses = workCenterService.findByStatusDecommissioned();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/storage-status-reserved")
    public ResponseEntity<List<WorkCenterResponse>> findByStatusReserved(){
    	List<WorkCenterResponse> responses = workCenterService.findByStatusReserved();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/storage-status-temporary")
    public ResponseEntity<List<WorkCenterResponse>> findByStatusTemporary(){
    	List<WorkCenterResponse> responses = workCenterService.findByStatusTemporary();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/storage-status-full")
    public ResponseEntity<List<WorkCenterResponse>> findByStatusFull(){
    	List<WorkCenterResponse> responses = workCenterService.findByStatusFull();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/location-and-capacity-greater-than")
    public ResponseEntity<List<WorkCenterResponse>> findByLocationAndCapacityGreaterThan(@RequestParam("location") String location,@RequestParam("capacity") BigDecimal capacity){
    	List<WorkCenterResponse> responses = workCenterService.findByLocationAndCapacityGreaterThan(location, capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/name-and-location")
    public ResponseEntity<List<WorkCenterResponse>> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(@RequestParam("name") String name,@RequestParam("location") String location){
    	List<WorkCenterResponse> responses = workCenterService.findByNameContainingIgnoreCaseAndLocationContainingIgnoreCase(name, location);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/type-and-status")
    public ResponseEntity<List<WorkCenterResponse>> findByLocalStorage_TypeAndLocalStorage_Status(@RequestParam("type") StorageType type,@RequestParam("status") StorageStatus status){
    	List<WorkCenterResponse> responses = workCenterService.findByLocalStorage_TypeAndLocalStorage_Status(type, status);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/ids-between")
    public ResponseEntity<List<WorkCenterResponse>> findByIdBetween(@RequestParam("startId") Long startId,@RequestParam("endId") Long endId){
    	List<WorkCenterResponse> responses = workCenterService.findByIdBetween(startId, endId);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/local-storage-is-null")
    public ResponseEntity<List<WorkCenterResponse>> findByLocalStorageIsNull(){
    	List<WorkCenterResponse> responses = workCenterService.findByLocalStorageIsNull();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/local-storage-is-not-null")
    public ResponseEntity<List<WorkCenterResponse>> findByLocalStorageIsNotNull(){
    	List<WorkCenterResponse> responses = workCenterService.findByLocalStorageIsNotNull();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/order-by-capacity-asc")
    public ResponseEntity<List<WorkCenterResponse>> findAllByOrderByCapacityAsc(){
    	List<WorkCenterResponse> responses = workCenterService.findAllByOrderByCapacityAsc();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/order-by-capacity-desc")
    public ResponseEntity<List<WorkCenterResponse>> findAllByOrderByCapacityDesc(){
    	List<WorkCenterResponse> responses = workCenterService.findAllByOrderByCapacityDesc();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/location-in")
    public ResponseEntity<List<WorkCenterResponse>> findByLocationIn(@RequestParam("locations") List<String> locations){
    	List<WorkCenterResponse> responses = workCenterService.findByLocationIn(locations);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/name-and-storage-status")
    public ResponseEntity<List<WorkCenterResponse>> findByNameContainingIgnoreCaseAndLocalStorage_Status(@RequestParam("status") String name, StorageStatus status){
    	List<WorkCenterResponse> responses = workCenterService.findByNameContainingIgnoreCaseAndLocalStorage_Status(name, status);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/location-and-storage-type")
    public ResponseEntity<List<WorkCenterResponse>> findByLocationContainingIgnoreCaseAndLocalStorage_Type(@RequestParam("location") String location,@RequestParam("type") StorageType type){
    	List<WorkCenterResponse> responses = workCenterService.findByLocationContainingIgnoreCaseAndLocalStorage_Type(location, type);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/count-work-centers-by-capacity")
    public ResponseEntity<List<CountWorkCenterCapacityResponse>>  countWorkCentersByCapacity(){
    	List<CountWorkCenterCapacityResponse> responses = workCenterService.countWorkCentersByCapacity();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/count-work-centers-by-capacity-less-than")
    public ResponseEntity<Long> countWorkCentersByCapacityLessThan(@RequestParam("capacity") BigDecimal capacity){
    	Long responses = workCenterService.countWorkCentersByCapacityLessThan(capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/count-work-centers-by-capacity-greater-than")
    public ResponseEntity<Long> countWorkCentersByCapacityGreaterThan(@RequestParam("capacity") BigDecimal capacity){
    	Long responses = workCenterService.countWorkCentersByCapacityGreaterThan(capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/count-by-work-centers-location")
    public ResponseEntity<List<CountWorkCenterResultResponse>>  countWorkCentersByLocation(){
    	List<CountWorkCenterResultResponse> responses = workCenterService.countWorkCentersByLocation();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/count-by-work-centers-storage-status")
    public ResponseEntity<List<CountWorkCentersByStorageStatusResponse>> countWorkCentersByStorageStatus(){
    	List<CountWorkCentersByStorageStatusResponse> responses = workCenterService.countWorkCentersByStorageStatus();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.WORK_CENTER_READ_ACCESS)
    @GetMapping("/search/count-by-work-centers-storage-type")
    public ResponseEntity<List<CountWorkCentersByStorageTypeResponse>> countWorkCentersByStorageType(){
    	List<CountWorkCentersByStorageTypeResponse> responses = workCenterService.countWorkCentersByStorageType();
    	return ResponseEntity.ok(responses);
    }
}
