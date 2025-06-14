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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.request.TrackingInfoRequest;
import com.jovan.erp_v1.response.TrackingInfoResponse;
import com.jovan.erp_v1.service.ITrackingInfoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trackingInfos")
@CrossOrigin("http://localhost:5173")
@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
public class TrackingInfoController {

    private ITrackingInfoService trackingInfoService;

    @PostMapping("/create/new-trackingInfo")
    public ResponseEntity<TrackingInfoResponse> create(@Valid @RequestBody TrackingInfoRequest request) {
        TrackingInfoResponse response = trackingInfoService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TrackingInfoResponse> update(@PathVariable Long id,
            @Valid @RequestBody TrackingInfoRequest request) {
        TrackingInfoResponse response = trackingInfoService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        trackingInfoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/find-one/{id}")
    public ResponseEntity<TrackingInfoResponse> findOne(@PathVariable Long id) {
        TrackingInfoResponse response = trackingInfoService.findOneById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/find-all")
    public ResponseEntity<List<TrackingInfoResponse>> findAll() {
        List<TrackingInfoResponse> responses = trackingInfoService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/trackingNumber")
    public ResponseEntity<TrackingInfoResponse> findByTrackingNumber(
            @RequestParam("trackingNumber") String trackingNumber) {
        TrackingInfoResponse response = trackingInfoService.findByTrackingNumber(trackingNumber);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/shipment/{shipmentId}")
    public ResponseEntity<TrackingInfoResponse> findByShipmentId(@PathVariable Long shipmentId) {
        TrackingInfoResponse response = trackingInfoService.findByShipmentId(shipmentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estimated-time-delivery")
    public ResponseEntity<List<TrackingInfoResponse>> findByEstimatedDeliveryBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate end) {
        List<TrackingInfoResponse> responses = trackingInfoService.findByEstimatedDeliveryBetween(start, end);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/by-location-status")
    public ResponseEntity<List<TrackingInfoResponse>> findByCurrentLocationAndCurrentStatus(
            @RequestParam("location") String location,
            @RequestParam("status") ShipmentStatus status) {
        List<TrackingInfoResponse> responses = trackingInfoService.findByCurrentLocationAndCurrentStatus(location,
                status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/estimated-delivery")
    public ResponseEntity<List<TrackingInfoResponse>> findByEstimatedDelivery(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date) {
        List<TrackingInfoResponse> responses = trackingInfoService.findByEstimatedDelivery(date);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/orderByEstimatedDeliveryAsc")
    public ResponseEntity<List<TrackingInfoResponse>> findAllByOrderByEstimatedDeliveryAsc() {
        List<TrackingInfoResponse> responses = trackingInfoService.findAllByOrderByEstimatedDeliveryAsc();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("create-date-between")
    public ResponseEntity<List<TrackingInfoResponse>> findByCreatedAtBetween(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        List<TrackingInfoResponse> responses = trackingInfoService.findByCreatedAtBetween(from, to);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/update-between")
    public ResponseEntity<List<TrackingInfoResponse>> findByUpdatedAtBetween(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        List<TrackingInfoResponse> responses = trackingInfoService.findByUpdatedAtBetween(from, to);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("update-after")
    public ResponseEntity<List<TrackingInfoResponse>> findByUpdatedAtAfter(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<TrackingInfoResponse> responses = trackingInfoService.findByUpdatedAtAfter(date);
        return ResponseEntity.ok(responses);
    }
}
