package com.jovan.erp_v1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.jovan.erp_v1.request.EventLogRequest;
import com.jovan.erp_v1.response.EventLogResponse;
import com.jovan.erp_v1.service.IEventLogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/eventLogs")
@RequiredArgsConstructor
public class EventLogController {

    private final IEventLogService eventLogService;

    @PostMapping
    public ResponseEntity<EventLogResponse> create(@RequestBody @Valid EventLogRequest request) {
        return ResponseEntity.ok(eventLogService.addEvent(request));
    }

    @GetMapping("/shipment/{shipmentId}")
    public ResponseEntity<List<EventLogResponse>> getByShipment(@PathVariable Long shipmentId) {
        return ResponseEntity.ok(eventLogService.getEventsForShipment(shipmentId));
    }
}