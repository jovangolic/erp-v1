package com.jovan.erp_v1.controller;

import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDateTime;
import java.util.List;

import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.request.EventLogRequest;
import com.jovan.erp_v1.response.EventLogResponse;
import com.jovan.erp_v1.service.IEventLogService;
import com.jovan.erp_v1.util.RoleGroups;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/eventLogs")
@RequiredArgsConstructor
@PreAuthorize(RoleGroups.EVENT_LOG_READ_ACCESS)
public class EventLogController {

    private final IEventLogService eventLogService;

    @PreAuthorize(RoleGroups.EVENT_LOG_FULL_ACCESS)
    @PostMapping("/create-event-log")
    public ResponseEntity<EventLogResponse> create(@RequestBody @Valid EventLogRequest request) {
        return ResponseEntity.ok(eventLogService.addEvent(request));
    }

    @PreAuthorize(RoleGroups.EVENT_LOG_READ_ACCESS)
    @GetMapping("/shipment/{shipmentId}")
    public ResponseEntity<List<EventLogResponse>> getByShipment(@PathVariable Long shipmentId) {
        return ResponseEntity.ok(eventLogService.getEventsForShipment(shipmentId));
    }
    
    @PreAuthorize(RoleGroups.EVENT_LOG_FULL_ACCESS)
    @PutMapping("/update/{id}")
    public ResponseEntity<EventLogResponse> update(@PathVariable Long id,@Valid @RequestBody EventLogRequest request){
    	EventLogResponse response = eventLogService.update(id, request);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize(RoleGroups.EVENT_LOG_FULL_ACCESS)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
    	eventLogService.delete(id);
    	return ResponseEntity.noContent().build();
    }
    
    @PreAuthorize(RoleGroups.EVENT_LOG_READ_ACCESS)
    @GetMapping("/find-one/{id}")
    public ResponseEntity<EventLogResponse> findOne(@PathVariable Long id){
    	EventLogResponse response = eventLogService.findOne(id);
    	return ResponseEntity.ok(response);
    }
    
    @PreAuthorize(RoleGroups.EVENT_LOG_READ_ACCESS)
    @GetMapping("/find-all")
    public ResponseEntity<List<EventLogResponse>> findAll(){
    	List<EventLogResponse> responses = eventLogService.findAll();
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.EVENT_LOG_READ_ACCESS)
    @GetMapping("/find-by-shipment/{shipmentId}")
    public ResponseEntity<List<EventLogResponse>> findByShipmentId(@PathVariable Long shipmentId){
    	List<EventLogResponse> responses = eventLogService.findByShipmentId(shipmentId);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.EVENT_LOG_READ_ACCESS)
    @GetMapping("/search/latest-shipment/{shipmentId}")
    public ResponseEntity<EventLogResponse> findLatestForShipment(@PathVariable Long shipmentId){
    	EventLogResponse responses = eventLogService.findLatestForShipment(shipmentId)
    			.orElseThrow(() -> new ValidationException("Latest shipment is not found"));
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.EVENT_LOG_READ_ACCESS)
    @GetMapping("/timestamp-after")
    public ResponseEntity<List<EventLogResponse>> findByTimestampAfter(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
    	List<EventLogResponse> responses = eventLogService.findByTimestampAfter(date);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.EVENT_LOG_READ_ACCESS)
    @GetMapping("/timestamp-between")
    public ResponseEntity<List<EventLogResponse>> findByTimestampBetween(@RequestParam("start")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  LocalDateTime start,
    		@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
    	List<EventLogResponse> responses = eventLogService.findByTimestampBetween(start, end);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.EVENT_LOG_READ_ACCESS)
    @GetMapping("/description")
    public ResponseEntity<List<EventLogResponse>> findByDescriptionContaining(@RequestParam("text") String text){
    	List<EventLogResponse> responses = eventLogService.findByDescriptionContaining(text);
    	return ResponseEntity.ok(responses);
    }
    
    @PreAuthorize(RoleGroups.EVENT_LOG_READ_ACCESS)
    @GetMapping("/search/{shipmentId}/timestamp-between")
    public ResponseEntity<List<EventLogResponse>> findByShipmentIdAndTimestampBetween(@PathVariable Long shipmentId,
    		@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
    		@RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to){
    	List<EventLogResponse> responses = eventLogService.findByShipmentIdAndTimestampBetween(shipmentId, from, to);
    	return ResponseEntity.ok(responses);
    }
     
    @PreAuthorize(RoleGroups.EVENT_LOG_READ_ACCESS)
    @GetMapping("/search/shipment-order-by-timestamp-desc/{shipmentId}")
    public ResponseEntity<EventLogResponse> findTopByShipmentIdOrderByTimestampDesc(@PathVariable Long shipmentId){
    	EventLogResponse responses = eventLogService.findTopByShipmentIdOrderByTimestampDesc(shipmentId);
    	return ResponseEntity.ok(responses);
    }
}