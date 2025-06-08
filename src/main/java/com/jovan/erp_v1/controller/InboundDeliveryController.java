package com.jovan.erp_v1.controller;

import java.time.LocalDate;
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
import com.jovan.erp_v1.request.InboundDeliveryRequest;
import com.jovan.erp_v1.response.InboundDeliveryResponse;
import com.jovan.erp_v1.service.InterfejsInboundDeliveryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inboundDeliveries")
@PreAuthorize("hasAnyRole('ADMIN','STORAGE_FOREMAN','STORAGE_EMPLOYEE')")
public class InboundDeliveryController {

    private InterfejsInboundDeliveryService inboundDeliveryService;

    @PostMapping("/create/new-inboundDelivery")
    public ResponseEntity<InboundDeliveryResponse> create(@Valid @RequestBody InboundDeliveryRequest request) {
        InboundDeliveryResponse response = inboundDeliveryService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<InboundDeliveryResponse> update(@PathVariable Long id,
            @Valid @RequestBody InboundDeliveryRequest request) {
        InboundDeliveryResponse response = inboundDeliveryService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inboundDeliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-one/{id}")
    public ResponseEntity<InboundDeliveryResponse> findOne(@PathVariable Long id) {
        InboundDeliveryResponse response = inboundDeliveryService.findByOneId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<InboundDeliveryResponse>> findAll() {
        List<InboundDeliveryResponse> responses = inboundDeliveryService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status")
    public ResponseEntity<List<InboundDeliveryResponse>> findByStatus(@RequestParam("status") DeliveryStatus status) {
        List<InboundDeliveryResponse> responses = inboundDeliveryService.findByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<InboundDeliveryResponse>> findByDeliveryDateBetween(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to) {
        List<InboundDeliveryResponse> responses = inboundDeliveryService.findByDeliveryDateBetween(from, to);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/supply/{supplyId}")
    public ResponseEntity<List<InboundDeliveryResponse>> findBySupplyId(@PathVariable Long supplyId) {
        List<InboundDeliveryResponse> responses = inboundDeliveryService.findBySupplyId(supplyId);
        return ResponseEntity.ok(responses);
    }

    // Bulk create - kreira više InboundDelivery odjednom
    @PostMapping("/bulk")
    public ResponseEntity<List<InboundDeliveryResponse>> createAll(@RequestBody List<InboundDeliveryRequest> requests) {
        List<InboundDeliveryResponse> responses = inboundDeliveryService.createAll(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    // Bulk delete - briše više InboundDelivery po ID-jevima
    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteAllByIds(@RequestBody List<Long> ids) {
        inboundDeliveryService.deleteAllByIds(ids);
        return ResponseEntity.noContent().build();
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
}

