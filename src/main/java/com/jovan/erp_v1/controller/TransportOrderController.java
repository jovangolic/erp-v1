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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.enumeration.TransportStatus;
import com.jovan.erp_v1.request.TransportOrderRequest;
import com.jovan.erp_v1.response.TransportOrderResponse;
import com.jovan.erp_v1.service.ITransportOrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transportOrders")
@CrossOrigin("http://localhost:5173")
@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN')")
public class TransportOrderController {

    private final ITransportOrderService transportOrderService;

    @PostMapping("/create/new-transportOrder")
    public ResponseEntity<TransportOrderResponse> create(@Valid @RequestBody TransportOrderRequest request) {
        TransportOrderResponse response = transportOrderService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TransportOrderResponse> update(@PathVariable Long id,
            @Valid @RequestBody TransportOrderRequest request) {
        TransportOrderResponse response = transportOrderService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transportOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/find-one/{id}")
    public ResponseEntity<TransportOrderResponse> findOne(@PathVariable Long id) {
        TransportOrderResponse response = transportOrderService.findOne(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/find-all")
    public ResponseEntity<List<TransportOrderResponse>> findAll() {
        List<TransportOrderResponse> responses = transportOrderService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/vehicle-model")
    public ResponseEntity<TransportOrderResponse> findByVehicle_Model(@RequestParam("model") String model) {
        TransportOrderResponse response = transportOrderService.findByVehicle_Model(model);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/driver-name")
    public ResponseEntity<TransportOrderResponse> findByDriver_Name(@RequestParam("name") String name) {
        TransportOrderResponse response = transportOrderService.findByDriver_Name(name);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<TransportOrderResponse> findByVehicleId(@PathVariable Long vehicleId) {
        TransportOrderResponse response = transportOrderService.findByVehicleId(vehicleId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/driver/{driversId}")
    public ResponseEntity<TransportOrderResponse> findByDriverId(@PathVariable Long driversId) {
        TransportOrderResponse response = transportOrderService.findByDriverId(driversId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/transport-status")
    public ResponseEntity<List<TransportOrderResponse>> findByStatus(@RequestParam("status") TransportStatus status) {
        List<TransportOrderResponse> responses = transportOrderService.findByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/outboundDelivery/{outboundDeliveryId}")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_Id(
            @PathVariable Long outboundDeliveryId) {
        List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_Id(outboundDeliveryId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/delivery-status")
    public ResponseEntity<List<TransportOrderResponse>> findByOutboundDelivery_Status(
            @RequestParam("status") DeliveryStatus status) {
        List<TransportOrderResponse> responses = transportOrderService.findByOutboundDelivery_Status(status);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
    @GetMapping("/date-range")
    public ResponseEntity<List<TransportOrderResponse>> findByScheduledDateBetween(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to) {
        List<TransportOrderResponse> responses = transportOrderService.findByScheduledDateBetween(from, to);
        return ResponseEntity.ok(responses);
    }

}
