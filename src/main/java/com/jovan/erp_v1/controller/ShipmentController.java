package com.jovan.erp_v1.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.request.ShipmentRequest;
import com.jovan.erp_v1.response.ShipmentResponse;
import com.jovan.erp_v1.service.IShipmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shipments")
@CrossOrigin("http://localhost:5173")
@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
public class ShipmentController {

    private final IShipmentService shipmentService;

    @PostMapping("/create/new-shipment")
    public ResponseEntity<ShipmentResponse> create(@Valid @RequestParam ShipmentRequest request) {
        ShipmentResponse response = shipmentService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ShipmentResponse> update(@PathVariable Long id,
            @Valid @RequestParam ShipmentRequest request) {
        ShipmentResponse response = shipmentService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        shipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/find-one/{id}")
    public ResponseEntity<ShipmentResponse> findOne(@PathVariable Long id) {
        ShipmentResponse response = shipmentService.findByOneId(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/find-all")
    public ResponseEntity<List<ShipmentResponse>> findAll() {
        List<ShipmentResponse> responses = shipmentService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/outboundDelivery/{outboundId}")
    public ResponseEntity<ShipmentResponse> findByOutboundDeliveryId(@PathVariable Long outboundId) {
        ShipmentResponse response = shipmentService.findByOutboundDeliveryId(outboundId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/shipment-status")
    public ResponseEntity<List<ShipmentResponse>> findByStatus(@RequestParam("status") ShipmentStatus status) {
        List<ShipmentResponse> responses = shipmentService.findByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/date-ranges")
    public ResponseEntity<List<ShipmentResponse>> findByShipmentDateBetween(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to) {
        List<ShipmentResponse> responses = shipmentService.findByShipmentDateBetween(from, to);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<ShipmentResponse>> findByProviderId(@PathVariable Long providerId) {
        List<ShipmentResponse> responses = shipmentService.findByProviderId(providerId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/provider-name")
    public ResponseEntity<List<ShipmentResponse>> findByProvider_NameContainingIgnoreCase(
            @RequestParam("name") String name) {
        List<ShipmentResponse> responses = shipmentService.findByProvider_NameContainingIgnoreCase(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/trackingInfo-status")
    public ResponseEntity<List<ShipmentResponse>> findByTrackingInfo_CurrentStatus(
            @RequestParam("status") ShipmentStatus status) {
        List<ShipmentResponse> responses = shipmentService.findByTrackingInfo_CurrentStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/trackingInfo/{trackingInfoId}")
    public ResponseEntity<List<ShipmentResponse>> findByTrackingInfoId(@PathVariable Long trackingInfoId) {
        List<ShipmentResponse> responses = shipmentService.findByTrackingInfoId(trackingInfoId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/storage/{storageId}")
    public ResponseEntity<List<ShipmentResponse>> findByOriginStorageId(@PathVariable Long storageId) {
        List<ShipmentResponse> responses = shipmentService.findByOriginStorageId(storageId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/storage-name")
    public ResponseEntity<List<ShipmentResponse>> findByOriginStorage_Name(@RequestParam("name") String name) {
        List<ShipmentResponse> responses = shipmentService.findByOriginStorage_Name(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/storage-location")
    public ResponseEntity<List<ShipmentResponse>> findByOriginStorage_Location(
            @RequestParam("location") String location) {
        List<ShipmentResponse> responses = shipmentService.findByOriginStorage_Location(location);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/storage-type")
    public ResponseEntity<List<ShipmentResponse>> findByOriginStorage_Type(@RequestParam("type") StorageType type) {
        List<ShipmentResponse> responses = shipmentService.findByOriginStorage_Type(type);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/storage-and-status")
    public ResponseEntity<List<ShipmentResponse>> findByOriginStorageIdAndStatus(@RequestParam Long storageId,
            @RequestParam ShipmentStatus status) {
        List<ShipmentResponse> responses = shipmentService.findByOriginStorageIdAndStatus(storageId, status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/by-storage-name")
    public ResponseEntity<List<ShipmentResponse>> searchByOriginStorageName(@RequestParam("name") String name) {
        List<ShipmentResponse> responses = shipmentService.searchByOriginStorageName(name);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/tracking-info-location")
    public ResponseEntity<List<ShipmentResponse>> findByTrackingInfo_CurrentLocationContainingIgnoreCase(
            @RequestParam("location") String location) {
        List<ShipmentResponse> responses = shipmentService
                .findByTrackingInfo_CurrentLocationContainingIgnoreCase(location);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/overdue-delayed")
    public ResponseEntity<List<ShipmentResponse>> findOverdueDelayedShipments() {
        List<ShipmentResponse> responses = shipmentService.findOverdueDelayedShipments();
        return ResponseEntity.ok(responses);
    }
}
