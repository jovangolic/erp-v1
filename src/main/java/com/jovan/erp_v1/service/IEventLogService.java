package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.jovan.erp_v1.request.EventLogRequest;
import com.jovan.erp_v1.response.EventLogResponse;

public interface IEventLogService {

	EventLogResponse addEvent(EventLogRequest request);
    List<EventLogResponse> getEventsForShipment(Long shipmentId);
    EventLogResponse update(Long id, EventLogRequest request);
    void delete(Long id);
    EventLogResponse findOne(Long id);
    List<EventLogResponse> findAll();
    List<EventLogResponse> findByShipmentId(Long shipmentId);
    Optional<EventLogResponse> findLatestForShipment(Long shipmentId);
    List<EventLogResponse> findByTimestampAfter(LocalDateTime date);
    List<EventLogResponse> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<EventLogResponse> findByDescriptionContaining(String text);
    List<EventLogResponse> findByShipmentIdAndTimestampBetween(Long shipmentId, LocalDateTime from, LocalDateTime to);
    EventLogResponse findTopByShipmentIdOrderByTimestampDesc(Long shipmentId);
}
