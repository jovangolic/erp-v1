package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.InboundDeliveryStatus;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.request.InboundDeliveryRequest;
import com.jovan.erp_v1.response.InboundDeliveryResponse;
import com.jovan.erp_v1.save_as.InboundDeliverySaveAsRequest;
import com.jovan.erp_v1.search_request.InboundDeliverySearchRequest;
import com.jovan.erp_v1.service.InterfejsInboundDeliveryService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inboundDeliveries")
@PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
public class InboundDeliveryController {

    private InterfejsInboundDeliveryService inboundDeliveryService;

    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_FULL_ACCESS)
    @PostMapping("/create/new-inboundDelivery")
    public ResponseEntity<InboundDeliveryResponse> create(@Valid @RequestBody InboundDeliveryRequest request) {
        InboundDeliveryResponse response = inboundDeliveryService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<InboundDeliveryResponse> update(@PathVariable Long id,
            @Valid @RequestBody InboundDeliveryRequest request) {
        InboundDeliveryResponse response = inboundDeliveryService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inboundDeliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<InboundDeliveryResponse> findOne(@PathVariable Long id) {
        InboundDeliveryResponse response = inboundDeliveryService.findByOneId(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<InboundDeliveryResponse>> findAll() {
        List<InboundDeliveryResponse> responses = inboundDeliveryService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/status")
    public ResponseEntity<List<InboundDeliveryResponse>> findByStatus(@RequestParam("status") DeliveryStatus status) {
        List<InboundDeliveryResponse> responses = inboundDeliveryService.findByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/date-range")
    public ResponseEntity<List<InboundDeliveryResponse>> findByDeliveryDateBetween(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to) {
        List<InboundDeliveryResponse> responses = inboundDeliveryService.findByDeliveryDateBetween(from, to);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/supply/{supplyId}")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupplyId(@PathVariable Long supplyId) {
        List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupplyId(supplyId);
        return ResponseEntity.ok(responses);
    }

    // Bulk create - kreira više InboundDelivery odjednom
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_FULL_ACCESS)
    @PostMapping("/bulk")
    public ResponseEntity<List<InboundDeliveryResponse>> createAll(@RequestBody List<InboundDeliveryRequest> requests) {
        List<InboundDeliveryResponse> responses = inboundDeliveryService.createAll(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    // Bulk delete - briše više InboundDelivery po ID-jevima
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_FULL_ACCESS)
    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAllByIds(@RequestBody List<Long> ids) {
        inboundDeliveryService.deleteAllByIds(ids);
        return ResponseEntity.noContent().build();
    }
    
    //nove metode
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/supply/storage/{storageId}")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_Storage_Id(@PathVariable Long storageId){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_Storage_Id(storageId);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-name")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_Storage_NameContainingIgnoreCase(@RequestParam("storageName") String storageName){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_Storage_NameContainingIgnoreCase(storageName);
    	  return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-location")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_Storage_LocationContainingIgnoreCase(@RequestParam("storageLocation") String storageLocation){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_Storage_LocationContainingIgnoreCase(storageLocation);
    	 return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-capacity")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_StorageCapacity(@RequestParam("storageCapacity") BigDecimal storageCapacity){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_StorageCapacity(storageCapacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-capacity-greater-than")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_StorageCapacityGreaterThan(@RequestParam("storageCapacity") BigDecimal storageCapacity){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_StorageCapacityGreaterThan(storageCapacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-capacity-less-than")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_StorageCapacityLessThan(@RequestParam("storageCapacity") BigDecimal storageCapacity){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_StorageCapacityLessThan(storageCapacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-type")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_StorageType(@RequestParam("type") StorageType type){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_StorageType(type);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-status")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_StorageStatus(@RequestParam("status") StorageStatus status){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_StorageStatus(status);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-name-and-type")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_StorageNameContainingIgnoreCaseAndType(@RequestParam("storageName") String storageName,
    		@RequestParam("type") StorageType type){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_StorageNameContainingIgnoreCaseAndType(storageName, type);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-name-and-status")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_StorageNameContainingIgnoreCaseAndStatus(@RequestParam("storageName") String storageName,
    		@RequestParam("status") StorageStatus status){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_StorageNameContainingIgnoreCaseAndStatus(storageName, status);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-location-type")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_StorageLocationContainingIgnoreCaseAndType(@RequestParam("storageLocation") String storageLocation,
    		@RequestParam("type") StorageType type){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_StorageLocationContainingIgnoreCaseAndType(storageLocation, type);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-location-status")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_StorageLocationContainingIgnoreCaseAndStatus(@RequestParam("storageLocation") String storagLocation,
    		@RequestParam("status") StorageStatus status){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_StorageLocationContainingIgnoreCaseAndStatus(storagLocation, status);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-name-capacity")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_StorageNameContainingIgnoreCaseAndCapacity(@RequestParam("storageName") String storageName,
    		@RequestParam("capacity") BigDecimal capacity){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_StorageNameContainingIgnoreCaseAndCapacity(storageName, capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-name-capacity-greater-than")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_StorageNameContainingIgnoreCaseAndCapacityGreaterThan(@RequestParam("storageName") String storageName,
    		@RequestParam("capacity") BigDecimal capacity){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_StorageNameContainingIgnoreCaseAndCapacityGreaterThan(storageName, capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-name-capacity-less-than")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_StorageNameContainingIgnoreCaseAndCapacityLessThan(@RequestParam("") String storageName,
    		@RequestParam("capacity") BigDecimal capacity){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_StorageNameContainingIgnoreCaseAndCapacityLessThan(storageName, capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-name-and-location")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_StorageNameContainingIgnoreCaseAndLocationContainingIgnoreCase(@RequestParam("storageName") String storageName,
    		@RequestParam("") String storageLocation){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_StorageNameContainingIgnoreCaseAndLocationContainingIgnoreCase(storageName, storageLocation);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-location-capacity")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_StorageLocationContainingIgnoreCaseAndCapacity(@RequestParam("storageLocation") String storageLocation,
    		@RequestParam("capacity") BigDecimal capacity){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_StorageLocationContainingIgnoreCaseAndCapacity(storageLocation, capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-location-capacity-greater-than")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_StorageLocationContainingIgnoreCaseAndCapacityGreaterThan(@RequestParam("storageLocation") String storageLocation,
    		@RequestParam("capacity") BigDecimal capacity){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_StorageLocationContainingIgnoreCaseAndCapacityGreaterThan(storageLocation, capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-location-capacity-less-than")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_StorageLocationContainingIgnoreCaseAndCapacityLessThan(@RequestParam("storageLocation") String storageLocation,
    		@RequestParam("capacity") BigDecimal capacity){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_StorageLocationContainingIgnoreCaseAndCapacityLessThan(storageLocation, capacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply/storage-without-shelves")
    public ResponseEntity<List<InboundDeliveryResponse>> findByStorageWithoutShelvesOrUnknown(){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findByStorageWithoutShelvesOrUnknown();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply-quantity")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_Quantity(@RequestParam("quantity") BigDecimal quantity){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_Quantity(quantity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply-quantity-greater-than")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_QuantityGreaterThan(@RequestParam("quantity") BigDecimal quantity){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_QuantityGreaterThan(quantity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply-quantity-less-than")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_QuantityLessThan(@RequestParam("quantity") BigDecimal quantity){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_QuantityLessThan(quantity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply-quantity-between")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_QuantityBetween(@RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_QuantityBetween(min, max);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply-updates")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_Updates(@RequestParam("updates") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updates){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_Updates(updates);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply-updates-after")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_UpdatesAfter(@RequestParam("updates") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updates){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_UpdatesAfter(updates);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply-updates-before")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_UpdatesBefore(@RequestParam("updates") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updates){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_UpdatesBefore(updates);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/search/supply-updates-between")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupply_UpdatesBetween(@RequestParam("updatesFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatesFrom,
    		@RequestParam("updatesTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatesTo){
    	List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupply_UpdatesBetween(updatesFrom, updatesTo);
    	return ResponseEntity.ok(responses);
    }
    
    // primer za createAll JSON
    /*
     * [
     * {
     * "id": null,
     * "deliveryDate": "2025-06-01",
     * "supplyId": 10,
     * "status": "DELIVERED",
     * "itemRequest": [ //lista DeliveryItemRequest objekata ]
     * },
     * {
     * "id": null,
     * "deliveryDate": "2025-06-02",
     * "supplyId": 11,
     * "status": "PENDING",
     * "itemRequest": [ //.. ]
     * }
     * ]
     */

    // primer za deleteAllByIds -> [1,2,3,4]
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_READ_ACCESS)
    @GetMapping("/track/{id}")
    public ResponseEntity<InboundDeliveryResponse> trackInboundDelivery(Long id){
    	InboundDeliveryResponse items = inboundDeliveryService.trackInboundDelivery(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_FULL_ACCESS)
    @PostMapping("/{id}/confirm")
    public ResponseEntity<InboundDeliveryResponse> confirmInboundDelivery(Long id){
    	InboundDeliveryResponse items = inboundDeliveryService.confirmInboundDelivery(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_FULL_ACCESS)
    @PostMapping("/{id}/cancel")
    public ResponseEntity<InboundDeliveryResponse> cancelInboundDelivery(Long id){
    	InboundDeliveryResponse items = inboundDeliveryService.cancelInboundDelivery(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_FULL_ACCESS)
    @PostMapping("/{id}/close")
    public ResponseEntity<InboundDeliveryResponse> closeInboundDelivery(Long id){
    	InboundDeliveryResponse items = inboundDeliveryService.closeInboundDelivery(id);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_FULL_ACCESS)
    @PostMapping("/{id}/status/{status}")
    public ResponseEntity<InboundDeliveryResponse> changeStatus(Long id, InboundDeliveryStatus status){
    	InboundDeliveryResponse items = inboundDeliveryService.changeStatus(id, status);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_FULL_ACCESS)
    @PostMapping("/save")
    public ResponseEntity<InboundDeliveryResponse> saveInboundDelivery(InboundDeliveryRequest request){
    	InboundDeliveryResponse items = inboundDeliveryService.saveInboundDelivery(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_FULL_ACCESS)
    @PostMapping("/save-as")
    public ResponseEntity<InboundDeliveryResponse> saveAs(InboundDeliverySaveAsRequest request){
    	InboundDeliveryResponse items = inboundDeliveryService.saveAs(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_FULL_ACCESS)
    @PostMapping("/save-all")
    public ResponseEntity<List<InboundDeliveryResponse>> saveAll(List<InboundDeliveryRequest> request){
    	List<InboundDeliveryResponse> items = inboundDeliveryService.saveAll(request);
    	return ResponseEntity.ok(items);
    }
    
    @PreAuthorize(RoleGroups.INBOUND_DELIVERY_FULL_ACCESS)
    @PostMapping("/general-search")
    public ResponseEntity<List<InboundDeliveryResponse>> generalSearch(InboundDeliverySearchRequest request){
    	List<InboundDeliveryResponse> items = inboundDeliveryService.generalSearch(request);
    	return ResponseEntity.ok(items);
    }
}

