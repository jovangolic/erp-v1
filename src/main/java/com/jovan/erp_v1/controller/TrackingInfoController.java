package com.jovan.erp_v1.controller;

import java.math.BigDecimal;
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

import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.enumeration.StorageStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.request.TrackingInfoRequest;
import com.jovan.erp_v1.response.TrackingInfoResponse;
import com.jovan.erp_v1.service.ITrackingInfoService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trackingInfos")
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
public class TrackingInfoController {

    private final ITrackingInfoService trackingInfoService;

    @PreAuthorize(RoleGroups.TRACKING_INFO_FULL_ACCESS)
    @PostMapping("/create/new-trackingInfo")
    public ResponseEntity<TrackingInfoResponse> create(@Valid @RequestBody TrackingInfoRequest request) {
        TrackingInfoResponse response = trackingInfoService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.TRACKING_INFO_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<TrackingInfoResponse> update(@PathVariable Long id,
            @Valid @RequestBody TrackingInfoRequest request) {
        TrackingInfoResponse response = trackingInfoService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.TRACKING_INFO_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        trackingInfoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<TrackingInfoResponse> findOne(@PathVariable Long id) {
        TrackingInfoResponse response = trackingInfoService.findOneById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<TrackingInfoResponse>> findAll() {
        List<TrackingInfoResponse> responses = trackingInfoService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/trackingNumber")
    public ResponseEntity<TrackingInfoResponse> findByTrackingNumber(
            @RequestParam("trackingNumber") String trackingNumber) {
        TrackingInfoResponse response = trackingInfoService.findByTrackingNumber(trackingNumber);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/shipment/{shipmentId}")
    public ResponseEntity<TrackingInfoResponse> findByShipmentId(@PathVariable Long shipmentId) {
        TrackingInfoResponse response = trackingInfoService.findByShipmentId(shipmentId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/estimated-time-delivery")
    public ResponseEntity<List<TrackingInfoResponse>> findByEstimatedDeliveryBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate end) {
        List<TrackingInfoResponse> responses = trackingInfoService.findByEstimatedDeliveryBetween(start, end);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/by-location-status")
    public ResponseEntity<List<TrackingInfoResponse>> findByCurrentLocationAndCurrentStatus(
            @RequestParam("location") String location,
            @RequestParam("status") ShipmentStatus status) {
        List<TrackingInfoResponse> responses = trackingInfoService.findByCurrentLocationAndCurrentStatus(location,
                status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/estimated-delivery")
    public ResponseEntity<List<TrackingInfoResponse>> findByEstimatedDelivery(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date) {
        List<TrackingInfoResponse> responses = trackingInfoService.findByEstimatedDelivery(date);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/orderByEstimatedDeliveryAsc")
    public ResponseEntity<List<TrackingInfoResponse>> findAllByOrderByEstimatedDeliveryAsc() {
        List<TrackingInfoResponse> responses = trackingInfoService.findAllByOrderByEstimatedDeliveryAsc();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("create-date-between")
    public ResponseEntity<List<TrackingInfoResponse>> findByCreatedAtBetween(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        List<TrackingInfoResponse> responses = trackingInfoService.findByCreatedAtBetween(from, to);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/update-between")
    public ResponseEntity<List<TrackingInfoResponse>> findByUpdatedAtBetween(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        List<TrackingInfoResponse> responses = trackingInfoService.findByUpdatedAtBetween(from, to);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("update-after")
    public ResponseEntity<List<TrackingInfoResponse>> findByUpdatedAtAfter(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<TrackingInfoResponse> responses = trackingInfoService.findByUpdatedAtAfter(date);
        return ResponseEntity.ok(responses);
    }
    
    //nove metode
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/search/by-pending")
    public ResponseEntity<List<TrackingInfoResponse>> findByPending(){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByPending();
    	return ResponseEntity.ok(responses);		
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/search/by-shipped")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipped(){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipped();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/search/by-in-transit")
    public ResponseEntity<List<TrackingInfoResponse>> findByIn_Transit(){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByIn_Transit();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/search/by-delivered")
    public ResponseEntity<List<TrackingInfoResponse>> findByDelivered(){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByDelivered();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/search/by-delayed")
    public ResponseEntity<List<TrackingInfoResponse>> findByDelayed(){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByDelayed();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/search/by-cancelled")
    public ResponseEntity<List<TrackingInfoResponse>> findByCancelled(){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByCancelled();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/search/shipment-date")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipment_ShipmentDate(@RequestParam("shipmentDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate shipmentDate){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipment_ShipmentDate(shipmentDate);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/search/shipment-date-range")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipment_ShipmentDateBetween(@RequestParam("shipmentDateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate shipmentDateStart,
    		@RequestParam("shipmentDateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate shipmentDateEnd){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipment_ShipmentDateBetween(shipmentDateStart, shipmentDateEnd);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/shipment/provider/{providerId}")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipment_Provider_Id(@PathVariable Long providerId){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipment_Provider_Id(providerId);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/shipment/outbound-delivery/{outboundDeliveryId}")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipment_OutboundDelivery_Id(@PathVariable Long outboundDeliveryId){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipment_OutboundDelivery_Id(outboundDeliveryId);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/shipment/origin-storage/{originStorageId}")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipment_OriginStorage_Id(@PathVariable Long originStorageId){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipment_OriginStorage_Id(originStorageId);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/shipment/tracking-number-and-current-location")
    public ResponseEntity<List<TrackingInfoResponse>> findByTrackingNumberAndCurrentLocation(@RequestParam("trackingNumber") String trackingNumber,
    		@RequestParam("currentLocation") String currentLocation){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByTrackingNumberAndCurrentLocation(trackingNumber, currentLocation);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/search/exists-by-tracking-number")
    public ResponseEntity<Boolean> existsByTrackingNumber(@RequestParam("trackingInfo") String trackingNumber){
    	Boolean responses = trackingInfoService.existsByTrackingNumber(trackingNumber);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/shipment/by-provider-name")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipment_Provider_NameContainingIgnoreCase(@RequestParam("providerName") String providerName){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipment_Provider_NameContainingIgnoreCase(providerName);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/shipment/by-contact-phone")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipment_Provider_ContactPhoneLikeIgnoreCase(@RequestParam("contactPhone") String contactPhone){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipment_Provider_ContactPhoneLikeIgnoreCase(contactPhone);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/shipment/by-provider-email")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipment_Provider_EmailLikeIgnoreCase(@RequestParam("providerEmail") String providerEmail){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipment_Provider_EmailLikeIgnoreCase(providerEmail);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/shipment/by-provider-website")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipment_Provider_Website(@RequestParam("providerWebsite") String providerWebsite){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipment_Provider_Website(providerWebsite);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/shipment/by-storage-name")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipment_OriginStorage_NameContainingIgnoreCase(@RequestParam("storageName") String storageName){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipment_OriginStorage_NameContainingIgnoreCase(storageName);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/shipment/by-storage-location")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipment_OriginStorage_LocationContainingIgnoreCase(@RequestParam("storageLocation") String storageLocation){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipment_OriginStorage_LocationContainingIgnoreCase(storageLocation);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/shipment/origin-storage-capacity")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipment_OriginStorage_Capacity(@RequestParam("storageCapacity") BigDecimal storageCapacity){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipment_OriginStorage_Capacity(storageCapacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/shipment/origin-storage-capacity-greater-than")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipment_OriginStorage_CapacityGreaterThan(@RequestParam("storageCapacity") BigDecimal storageCapacity){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipment_OriginStorage_CapacityGreaterThan(storageCapacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/shipment/origin-storage-capacity-less-than")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipment_OriginStorage_CapacityLessThan(@RequestParam("storageCapacity") BigDecimal storageCapacity){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipment_OriginStorage_CapacityLessThan(storageCapacity);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/shipment/origin-storage-type")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipment_OriginStorage_Type(@RequestParam("type") StorageType type){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipment_OriginStorage_Type(type);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/shipment/origin-storage-status")
    public ResponseEntity<List<TrackingInfoResponse>> findByShipment_OriginStorage_Status(@RequestParam("status") StorageStatus status){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByShipment_OriginStorage_Status(status);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/storage-type-and-status")
    public ResponseEntity<List<TrackingInfoResponse>> findByStorageTypeAndStatus(@RequestParam("type") StorageType type,@RequestParam("status") StorageStatus status){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByStorageTypeAndStatus(type, status);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.TRACKING_INFO_READ_ACCESS)
    @GetMapping("/search-by-type-and-status")
    public ResponseEntity<List<TrackingInfoResponse>> findByTypeAndStatus(@RequestParam("type") StorageType type,@RequestParam("status") StorageStatus status){
    	List<TrackingInfoResponse> responses = trackingInfoService.findByTypeAndStatus(type, status);
    	return ResponseEntity.ok(responses);
    }
    
}
