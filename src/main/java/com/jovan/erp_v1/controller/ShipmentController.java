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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.request.ShipmentRequest;
import com.jovan.erp_v1.response.ShipmentResponse;
import com.jovan.erp_v1.service.IShipmentService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shipments")
@CrossOrigin("http://localhost:5173")
@PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
public class ShipmentController {

    private final IShipmentService shipmentService;

    @PreAuthorize(RoleGroups.SHIPMENT_FULL_ACCESS)
    @PostMapping("/create/new-shipment")
    public ResponseEntity<ShipmentResponse> create(@Valid @RequestParam ShipmentRequest request) {
        ShipmentResponse response = shipmentService.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<ShipmentResponse> update(@PathVariable Long id,
            @Valid @RequestParam ShipmentRequest request) {
        ShipmentResponse response = shipmentService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        shipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<ShipmentResponse> findOne(@PathVariable Long id) {
        ShipmentResponse response = shipmentService.findByOneId(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<ShipmentResponse>> findAll() {
        List<ShipmentResponse> responses = shipmentService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/outboundDelivery/{outboundId}")
    public ResponseEntity<ShipmentResponse> findByOutboundDeliveryId(@PathVariable Long outboundId) {
        ShipmentResponse response = shipmentService.findByOutboundDeliveryId(outboundId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/shipment-status")
    public ResponseEntity<List<ShipmentResponse>> findByStatus(@RequestParam("status") ShipmentStatus status) {
        List<ShipmentResponse> responses = shipmentService.findByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/date-ranges")
    public ResponseEntity<List<ShipmentResponse>> findByShipmentDateBetween(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<ShipmentResponse> responses = shipmentService.findByShipmentDateBetween(from, to);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<ShipmentResponse>> findByProviderId(@PathVariable Long providerId) {
        List<ShipmentResponse> responses = shipmentService.findByProviderId(providerId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/provider-name")
    public ResponseEntity<List<ShipmentResponse>> findByProvider_NameContainingIgnoreCase(
            @RequestParam("name") String name) {
        List<ShipmentResponse> responses = shipmentService.findByProvider_NameContainingIgnoreCase(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/trackingInfo-status")
    public ResponseEntity<List<ShipmentResponse>> findByTrackingInfo_CurrentStatus(
            @RequestParam("status") ShipmentStatus status) {
        List<ShipmentResponse> responses = shipmentService.findByTrackingInfo_CurrentStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/trackingInfo/{trackingInfoId}")
    public ResponseEntity<List<ShipmentResponse>> findByTrackingInfoId(@PathVariable Long trackingInfoId) {
        List<ShipmentResponse> responses = shipmentService.findByTrackingInfoId(trackingInfoId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/storage/{storageId}")
    public ResponseEntity<List<ShipmentResponse>> findByOriginStorageId(@PathVariable Long storageId) {
        List<ShipmentResponse> responses = shipmentService.findByOriginStorageId(storageId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/storage-name")
    public ResponseEntity<List<ShipmentResponse>> findByOriginStorage_Name(@RequestParam("name") String name) {
        List<ShipmentResponse> responses = shipmentService.findByOriginStorage_Name(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/storage-location")
    public ResponseEntity<List<ShipmentResponse>> findByOriginStorage_Location(
            @RequestParam("location") String location) {
        List<ShipmentResponse> responses = shipmentService.findByOriginStorage_Location(location);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/storage-type")
    public ResponseEntity<List<ShipmentResponse>> findByOriginStorage_Type(@RequestParam("type") StorageType type) {
        List<ShipmentResponse> responses = shipmentService.findByOriginStorage_Type(type);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/storage-and-status")
    public ResponseEntity<List<ShipmentResponse>> findByOriginStorageIdAndStatus(@RequestParam Long storageId,
            @RequestParam ShipmentStatus status) {
        List<ShipmentResponse> responses = shipmentService.findByOriginStorageIdAndStatus(storageId, status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/by-storage-name")
    public ResponseEntity<List<ShipmentResponse>> searchByOriginStorageName(@RequestParam("name") String name) {
        List<ShipmentResponse> responses = shipmentService.searchByOriginStorageName(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/tracking-info-location")
    public ResponseEntity<List<ShipmentResponse>> findByTrackingInfo_CurrentLocationContainingIgnoreCase(
            @RequestParam("location") String location) {
        List<ShipmentResponse> responses = shipmentService
                .findByTrackingInfo_CurrentLocationContainingIgnoreCase(location);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/overdue-delayed")
    public ResponseEntity<List<ShipmentResponse>> findOverdueDelayedShipments() {
        List<ShipmentResponse> responses = shipmentService.findOverdueDelayedShipments();
        return ResponseEntity.ok(responses);
    }
    //nove metode
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/tracking-number")
    public ResponseEntity<List<ShipmentResponse>> findByTrackingInfo_TrackingNumber(@RequestParam("trackingNumber") String trackingNumber){
    	List<ShipmentResponse> responses = shipmentService.findByTrackingInfo_TrackingNumber(trackingNumber);
    	return ResponseEntity.ok(responses);
    }
     
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/estimated-delivery")
    public ResponseEntity<List<ShipmentResponse>> findByTrackingInfo_EstimatedDelivery(@RequestParam("estimatedDelivery") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate estimatedDelivery){
    	List<ShipmentResponse> responses = shipmentService.findByTrackingInfo_EstimatedDelivery(estimatedDelivery);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/estimated-delivery-between")
    public ResponseEntity<List<ShipmentResponse>> findByTrackingInfo_EstimatedDeliveryBetween(@RequestParam("estimatedDeliveryStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate estimatedDeliveryStart,
            @RequestParam("estimatedDeliveryEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate estimatedDeliveryEnd){
    	List<ShipmentResponse> responses = shipmentService.findByTrackingInfo_EstimatedDeliveryBetween(estimatedDeliveryStart, estimatedDeliveryEnd);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/provider-email")
    public ResponseEntity<List<ShipmentResponse>> findByProvider_EmailLikeIgnoreCase(@RequestParam("email") String email){
    	List<ShipmentResponse> responses = shipmentService.findByProvider_EmailLikeIgnoreCase(email);
    	 return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/provider-website")
    public ResponseEntity<List<ShipmentResponse>> findByProvider_WebsiteContainingIgnoreCase(@RequestParam("website") String website){
    	List<ShipmentResponse> responses = shipmentService.findByProvider_WebsiteContainingIgnoreCase(website);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/seach/provider-phone-number")
    public ResponseEntity<List<ShipmentResponse>> findByProvider_ContactPhoneLikeIgnoreCase(@RequestParam("phoneNumber") String phoneNumber){
    	List<ShipmentResponse> responses = shipmentService.findByProvider_ContactPhoneLikeIgnoreCase(phoneNumber);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/outboundDelivery-delivery-date")
    public ResponseEntity<List<ShipmentResponse>> findByOutboundDelivery_DeliveryDate(@RequestParam("deliveryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDate){
    	List<ShipmentResponse> responses = shipmentService.findByOutboundDelivery_DeliveryDate(deliveryDate);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/outboundDelivery-delivery-date-range")
    public ResponseEntity<List<ShipmentResponse>> findByOutboundDelivery_DeliveryDateBetween(@RequestParam("deliveryDateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDateStart,
            @RequestParam("deliveryDateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDateEnd){
    	List<ShipmentResponse> responses = shipmentService.findByOutboundDelivery_DeliveryDateBetween(deliveryDateStart, deliveryDateEnd);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/outboundDelivery-delivery-status")
    public ResponseEntity<List<ShipmentResponse>> findByOutboundDelivery_Status(@RequestParam("status") DeliveryStatus status){
    	List<ShipmentResponse> responses = shipmentService.findByOutboundDelivery_Status(status);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/outboundDelivery/buyer/{buyerId}")
    public ResponseEntity<List<ShipmentResponse>> findByOutboundDelivery_Buyer_Id(@PathVariable Long buyerId){
    	List<ShipmentResponse> responses = shipmentService.findByOutboundDelivery_Buyer_Id(buyerId);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/late-shipments")
    public ResponseEntity<List<ShipmentResponse>> findLateShipments(){
    	List<ShipmentResponse> responses = shipmentService.findLateShipments();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/shipments-due-soon")
    public ResponseEntity<List<ShipmentResponse>> findShipmentsDueSoon(@RequestParam("futureDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate futureDate){
    	List<ShipmentResponse> responses = shipmentService.findShipmentsDueSoon(futureDate);
    	 return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/tracking-info-is-null")
    public ResponseEntity<List<ShipmentResponse>> findByTrackingInfoIsNull(){
    	List<ShipmentResponse> responses = shipmentService.findByTrackingInfoIsNull();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/outboundDelivery/status-delivery-date-range")
    public ResponseEntity<List<ShipmentResponse>> findByOutboundDelivery_StatusAndOutboundDelivery_DeliveryDateBetween(@RequestParam("status") DeliveryStatus status,
    		@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to){
    	List<ShipmentResponse> responses = shipmentService.findByOutboundDelivery_StatusAndOutboundDelivery_DeliveryDateBetween(status, from, to);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/storage/{storageId}/trackingInfo-estimated-delivery-date-range")
    public ResponseEntity<List<ShipmentResponse>> findByOriginStorageIdAndTrackingInfo_EstimatedDeliveryBetween(@PathVariable Long storageId, 
    		@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to){
    	List<ShipmentResponse> responses = shipmentService.findByOriginStorageIdAndTrackingInfo_EstimatedDeliveryBetween(storageId, from, to);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/trackingInfo-status-location")
    public ResponseEntity<List<ShipmentResponse>> findByTrackingInfo_CurrentStatusAndTrackingInfo_CurrentLocationContainingIgnoreCase(@RequestParam("status") ShipmentStatus status
    		,@RequestParam("location") String location){
    	List<ShipmentResponse> responses = shipmentService.findByTrackingInfo_CurrentStatusAndTrackingInfo_CurrentLocationContainingIgnoreCase(status, location);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/buyer-name")
    public ResponseEntity<List<ShipmentResponse>> findByBuyerCompanyNameContainingIgnoreCase(@RequestParam("buyerName") String buyerName){
    	List<ShipmentResponse> responses = shipmentService.findByBuyerCompanyNameContainingIgnoreCase(buyerName);
    	return ResponseEntity.ok(responses);
    }
     
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/recently-delivered-shipments")
    public ResponseEntity<List<ShipmentResponse>> findRecentlyDeliveredShipments(@RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate){
    	List<ShipmentResponse> responses = shipmentService.findRecentlyDeliveredShipments(fromDate);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/cancelled-shipments")
    public ResponseEntity<List<ShipmentResponse>> findCancelledShipments(){
    	List<ShipmentResponse> responses = shipmentService.findCancelledShipments();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/cancelled-shipments-date-range")
    public ResponseEntity<List<ShipmentResponse>> findCancelledShipmentsBetweenDates(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
    		@RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to){
    	List<ShipmentResponse> responses = shipmentService.findCancelledShipmentsBetweenDates(from, to);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/delayed-shipments")
    public ResponseEntity<List<ShipmentResponse>> findDelayedShipments(){
    	List<ShipmentResponse> responses = shipmentService.findDelayedShipments();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/in-transit-shipments")
    public ResponseEntity<List<ShipmentResponse>> findInTransitShipments(){
    	List<ShipmentResponse> responses = shipmentService.findInTransitShipments();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/delivered-shipments")
    public ResponseEntity<List<ShipmentResponse>> findDeliveredShipments(){
    	List<ShipmentResponse> responses = shipmentService.findDeliveredShipments();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/shipments-by-fixed-status")
    public ResponseEntity<List<ShipmentResponse>> findShipmentsByFixedStatus(@RequestParam("status") ShipmentStatus status){
    	List<ShipmentResponse> responses = shipmentService.findShipmentsByFixedStatus(status);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/pending-deliveries")
    public ResponseEntity<List<ShipmentResponse>> findPendingDeliveries(){
    	List<ShipmentResponse> responses = shipmentService.findPendingDeliveries();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/in-transit-deliveries")
    public ResponseEntity<List<ShipmentResponse>> findInTransitDeliveries(){
    	List<ShipmentResponse> responses = shipmentService.findInTransitDeliveries();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/delivered-deliveries")
    public ResponseEntity<List<ShipmentResponse>> findDeliveredDeliveries(){
    	List<ShipmentResponse> responses = shipmentService.findDeliveredDeliveries();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/cancelled-deliveries")
    public ResponseEntity<List<ShipmentResponse>> findCancelledDeliveries(){
    	List<ShipmentResponse> responses = shipmentService.findCancelledDeliveries();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.SHIPMENT_READ_ACCESS)
    @GetMapping("/search/transit-shipments-with-transit-delivery")
    public ResponseEntity<List<ShipmentResponse>> findInTransitShipmentsWithInTransitDelivery(){
    	List<ShipmentResponse> responses = shipmentService.findInTransitShipmentsWithInTransitDelivery();
    	return ResponseEntity.ok(responses);
    }
}
