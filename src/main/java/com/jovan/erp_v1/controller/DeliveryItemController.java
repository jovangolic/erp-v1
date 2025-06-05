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

import com.jovan.erp_v1.request.DeliveryItemRequest;
import com.jovan.erp_v1.response.DeliveryItemResponse;
import com.jovan.erp_v1.service.IDeliveryItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery-items")
@CrossOrigin("http://localhost:5173")
@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
public class DeliveryItemController {

    private final IDeliveryItemService deliveryItemService;

    @PostMapping("/create/new-delivery-item")
    public ResponseEntity<DeliveryItemResponse> create(@Valid @RequestBody DeliveryItemRequest request) {
        DeliveryItemResponse response = deliveryItemService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DeliveryItemResponse> update(@PathVariable Long id,
            @Valid @RequestBody DeliveryItemRequest request) {
        DeliveryItemResponse response = deliveryItemService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-one/{id}")
    public ResponseEntity<DeliveryItemResponse> findById(@PathVariable Long id) {
        DeliveryItemResponse response = deliveryItemService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<DeliveryItemResponse>> findAll() {
        List<DeliveryItemResponse> responses = deliveryItemService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/by-quantity")
    public ResponseEntity<List<DeliveryItemResponse>> findByQuantity(@RequestParam("quantity") Double quantity) {
        List<DeliveryItemResponse> responses = deliveryItemService.findByQuantity(quantity);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("quantity-greater-than")
    public ResponseEntity<List<DeliveryItemResponse>> findByQuantityGreaterThan(
            @RequestParam("quantity") Double quantity) {
        List<DeliveryItemResponse> responses = deliveryItemService.findByQuantityGreaterThan(quantity);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("quantity-less-than")
    public ResponseEntity<List<DeliveryItemResponse>> findByQuantityLessThan(
            @RequestParam("quantity") Double quantity) {
        List<DeliveryItemResponse> responses = deliveryItemService.findByQuantityLessThan(quantity);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/inbound-date-range")
    public ResponseEntity<List<DeliveryItemResponse>> findByInboundDelivery_DeliveryDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate end) {
        List<DeliveryItemResponse> responses = deliveryItemService.findByInboundDelivery_DeliveryDateBetween(start,
                end);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/outbound-date-range")
    public ResponseEntity<List<DeliveryItemResponse>> findByOutboundDelivery_DeliveryDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate end) {
        List<DeliveryItemResponse> responses = deliveryItemService.findByOutboundDelivery_DeliveryDateBetween(start,
                end);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<DeliveryItemResponse> findByProduct(@PathVariable Long productId) {
        DeliveryItemResponse response = deliveryItemService.findByProduct(productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inbound/{inboundId}")
    public ResponseEntity<List<DeliveryItemResponse>> findByInboundDeliveryId(@PathVariable Long inboundId) {
        List<DeliveryItemResponse> responses = deliveryItemService.findByInboundDeliveryId(inboundId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/outbound/{outboundId}")
    public ResponseEntity<List<DeliveryItemResponse>> findByOutboundDeliveryId(@PathVariable Long outboundId) {
        List<DeliveryItemResponse> responses = deliveryItemService.findByOutboundDeliveryId(outboundId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<DeliveryItemResponse>> findByProduct_Name(@PathVariable String name) {
        List<DeliveryItemResponse> responses = deliveryItemService.findByProduct_Name(name);
        return ResponseEntity.ok(responses);
    }
}
