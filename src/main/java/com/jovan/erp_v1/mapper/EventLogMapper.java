package com.jovan.erp_v1.mapper;

import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.jovan.erp_v1.exception.ShipmentNotFoundException;
import com.jovan.erp_v1.model.EventLog;
import com.jovan.erp_v1.model.Shipment;
import com.jovan.erp_v1.repository.ShipmentRepository;
import com.jovan.erp_v1.request.EventLogRequest;
import com.jovan.erp_v1.response.EventLogResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventLogMapper extends AbstractMapper<EventLogRequest> {
	
	private final ShipmentRepository shipmentRepository;

	public EventLog toEntity(EventLogRequest request, Shipment sh) {
		Objects.requireNonNull(request, "EventLogRequest must not be null");
		validateIdForCreate(request, EventLogRequest::id);
		EventLog log = new EventLog();
		if (request.timestamp() != null) {
		    log.setTimestamp(request.timestamp());
		} else {
		    log.setTimestamp(LocalDateTime.now());
		}
		log.setDescription(request.description());
		log.setShipment(sh);
		return log;
	}
	
	public EventLog toUpdateEntity(EventLog log, EventLogRequest request) {
		Objects.requireNonNull(request, "EventLogRequest must not be null");
		Objects.requireNonNull(log, "EventLog must not be null");
		validateIdForUpdate(request, EventLogRequest::id);
		return buildEventLogFromRequest(log, request);
	}
	
	private EventLog buildEventLogFromRequest(EventLog log, EventLogRequest request) {
		log.setTimestamp(request.timestamp());
		log.setDescription(request.description());
		log.setShipment(fetchShipmentId(request.shipmentId()));
		return log;
	}
	
	public EventLogResponse toResponse(EventLog log) {
		Objects.requireNonNull(log, "EventLog must not be null");
		return new EventLogResponse(log);
	}
	
	public List<EventLogResponse> toResponseList(List<EventLog> logs){
		if(logs == null || logs.isEmpty()) {
			return Collections.emptyList();
		}
		return logs.stream().map(this::toResponse).collect(Collectors.toList());
	}
	
	private Shipment fetchShipmentId(Long shipmentId) {
		if(shipmentId == null) {
			throw new ShipmentNotFoundException("Shipment ID must not be null");
		}
		return shipmentRepository.findById(shipmentId).orElseThrow(() -> new ShipmentNotFoundException("Shipment not found wit hid "+shipmentId));
	}
}
