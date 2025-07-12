package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
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

import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.TransferStatus;
import com.jovan.erp_v1.request.StockTransferRequest;
import com.jovan.erp_v1.response.StockTransferResponse;
import com.jovan.erp_v1.service.IStockTransferService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stockTransfers")
@CrossOrigin("http://localhost:5173")
@PreAuthorize("hasRole('SUPERADMIN','ADMIN')")
public class StockTransferController {

    private IStockTransferService stockTransferService;

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'STORAGE_FOREMAN')")
    @PostMapping("/create/new-stockTransfer")
    public ResponseEntity<StockTransferResponse> create(@Valid @RequestBody StockTransferRequest request) {
        StockTransferResponse response = stockTransferService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<StockTransferResponse> update(@PathVariable Long id,
            @Valid @RequestBody StockTransferRequest request) {
        StockTransferResponse response = stockTransferService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('SUPERADMIN','ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stockTransferService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/find-one/{id}")
    public ResponseEntity<StockTransferResponse> findOne(@PathVariable Long id) {
        StockTransferResponse response = stockTransferService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/find-all")
    public ResponseEntity<List<StockTransferResponse>> findAll() {
        List<StockTransferResponse> responses = stockTransferService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/by-status")
    public ResponseEntity<List<StockTransferResponse>> findByStatus(@RequestParam("status") TransferStatus status) {
        List<StockTransferResponse> responses = stockTransferService.findByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/transfer-date")
    public ResponseEntity<List<StockTransferResponse>> findByTransferDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date) {
        List<StockTransferResponse> responses = stockTransferService.findByTransferDate(date);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/transfer-date-range")
    public ResponseEntity<List<StockTransferResponse>> findByTransferDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate end) {
        List<StockTransferResponse> responses = stockTransferService.findByTransferDateBetween(start, end);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/storage/{fromStorageId}")
    public ResponseEntity<List<StockTransferResponse>> findByFromStorageId(@PathVariable Long fromStorageId) {
        List<StockTransferResponse> responses = stockTransferService.findByFromStorageId(fromStorageId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/storage/{toStorageId}")
    public ResponseEntity<List<StockTransferResponse>> findByToStorageId(@PathVariable Long toStorageId) {
        List<StockTransferResponse> responses = stockTransferService.findByToStorageId(toStorageId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/fromStorageName")
    public ResponseEntity<List<StockTransferResponse>> findByFromStorage_Name(
            @RequestParam("fromStorageName") String fromStorageName) {
        List<StockTransferResponse> responses = stockTransferService.findByFromStorage_Name(fromStorageName);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/fromLocation")
    public ResponseEntity<List<StockTransferResponse>> findByFromStorage_Location(
            @RequestParam("fromLocation") String fromLocation) {
        List<StockTransferResponse> responses = stockTransferService.findByFromStorage_Location(fromLocation);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/toStorageName")
    public ResponseEntity<List<StockTransferResponse>> findByToStorage_Name(
            @RequestParam("toStorageName") String toStorageName) {
        List<StockTransferResponse> responses = stockTransferService.findByToStorage_Name(toStorageName);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/toLocation")
    public ResponseEntity<List<StockTransferResponse>> findByToStorage_Location(
            @RequestParam("toLocation") String toLocation) {
        List<StockTransferResponse> responses = stockTransferService.findByToStorage_Location(toLocation);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/fromStorageType")
    public ResponseEntity<List<StockTransferResponse>> findByFromStorage_Type(
            @RequestParam("fromStorageType") StorageType fromStorageType) {
        List<StockTransferResponse> responses = stockTransferService.findByFromStorage_Type(fromStorageType);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/toStorageType")
    public ResponseEntity<List<StockTransferResponse>> findByToStorage_Type(
            @RequestParam("toStorageType") StorageType toStorageType) {
        List<StockTransferResponse> responses = stockTransferService.findByToStorage_Type(toStorageType);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/status-and-date-range")
    public ResponseEntity<List<StockTransferResponse>> findByStatusAndDateRange(
            @RequestParam("status") TransferStatus status,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate end) {
        List<StockTransferResponse> responses = stockTransferService.findByStatusAndDateRange(status, start, end);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/fromType-toType")
    public ResponseEntity<List<StockTransferResponse>> findByFromAndToStorageType(
            @RequestParam("fromType") StorageType fromType,
            @RequestParam("toType") StorageType toType) {
        List<StockTransferResponse> responses = stockTransferService.findByFromAndToStorageType(fromType, toType);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/name-and-location")
    public ResponseEntity<List<StockTransferResponse>> searchFromStorageByNameAndLocation(
            @RequestParam("name") String name,
            @RequestParam("location") String location) {
        List<StockTransferResponse> responses = stockTransferService.searchFromStorageByNameAndLocation(name, location);
        return ResponseEntity.ok(responses);
    }
    
    //nove metode
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/search/from-storage-capacity")
    public ResponseEntity<List<StockTransferResponse>> findByFromStorage_Capacity(@RequestParam("capacity") BigDecimal capacity){
    	List<StockTransferResponse> responses = stockTransferService.findByFromStorage_Capacity(capacity);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/search/to-storage-capacity")
    public ResponseEntity<List<StockTransferResponse>> findByToStorage_Capacity(@RequestParam("capacity") BigDecimal capacity){
    	List<StockTransferResponse> responses = stockTransferService.findByToStorage_Capacity(capacity);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/search/from-storage-capacity-greater-than")
    public ResponseEntity<List<StockTransferResponse>> findByFromStorage_CapacityGreaterThan(@RequestParam("capacity") BigDecimal capacity){
    	List<StockTransferResponse> responses = stockTransferService.findByFromStorage_CapacityGreaterThan(capacity);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/search/to-storage-capacity-greater-than")
    public ResponseEntity<List<StockTransferResponse>> findByToStorage_CapacityGreaterThan(@RequestParam("capacity") BigDecimal capacity){
    	List<StockTransferResponse> responses = stockTransferService.findByToStorage_CapacityGreaterThan(capacity);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/search/from-storage-capacity-less-than")
    public ResponseEntity<List<StockTransferResponse>> findByFromStorage_CapacityLessThan(@RequestParam("capacity") BigDecimal capacity){
    	List<StockTransferResponse> responses = stockTransferService.findByFromStorage_CapacityLessThan(capacity);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/search/to-storage-capacity-less-than")
    public ResponseEntity<List<StockTransferResponse>> findByToStorage_CapacityLessThan(@RequestParam("capacity") BigDecimal capacity){
    	List<StockTransferResponse> responses = stockTransferService.findByToStorage_CapacityLessThan(capacity);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/search/from-storage/capacity-and-type")
    public ResponseEntity<List<StockTransferResponse>> findByFromStorage_CapacityAndType(@RequestParam("capacity") BigDecimal capacity,@RequestParam("type")  StorageType type){
    	List<StockTransferResponse> responses = stockTransferService.findByFromStorage_CapacityAndType(capacity, type);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/search/to-storage/capacity-and-type")
    public ResponseEntity<List<StockTransferResponse>> findByToStorage_CapacityAndType(@RequestParam("capacity") BigDecimal capacity,@RequestParam("type")  StorageType type){
    	List<StockTransferResponse> responses = stockTransferService.findByToStorage_CapacityAndType(capacity, type);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/search/from-storage/capacity-greater-than-and-type")
    public ResponseEntity<List<StockTransferResponse>> findByFromStorage_CapacityGreaterThanAndType(@RequestParam("capacity") BigDecimal capacity,@RequestParam("type") StorageType type){
    	List<StockTransferResponse> responses = stockTransferService.findByFromStorage_CapacityGreaterThanAndType(capacity, type);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/search/to-storage/capacity-greater-than-and-type")
    public ResponseEntity<List<StockTransferResponse>> findByToStorage_CapacityGreaterThanAndType(@RequestParam("capacity")  BigDecimal capacity,@RequestParam("type")  StorageType type){
    	List<StockTransferResponse> responses = stockTransferService.findByToStorage_CapacityGreaterThanAndType(capacity, type);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/search/from-storage/capacity-less-than-and-type")
    public ResponseEntity<List<StockTransferResponse>> findByFromStorage_CapacityLessThanAndType(@RequestParam("capacity")  BigDecimal capacity,@RequestParam("type")  StorageType type){
    	List<StockTransferResponse> responses = stockTransferService.findByFromStorage_CapacityLessThanAndType(capacity, type);
    	return ResponseEntity.ok(responses);	
    }
    
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN', 'STORAGE_FOREMAN', 'STORAGE_EMPLOYEE')")
    @GetMapping("/search/to-storage/capacity-less-than-and-type")
    public ResponseEntity<List<StockTransferResponse>> findByToStorage_CapacityLessThanAndType(@RequestParam("capacity") BigDecimal capacity,@RequestParam("type") StorageType type){
    	List<StockTransferResponse> responses = stockTransferService.findByToStorage_CapacityLessThanAndType(capacity, type);
    	return ResponseEntity.ok(responses);	
    }
    
}
