package com.jovan.erp_v1.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jovan.erp_v1.model.EventLog;
import com.jovan.erp_v1.model.Shipment;
import com.jovan.erp_v1.repository.EventLogRepository;
import com.jovan.erp_v1.repository.ShipmentRepository;
import com.jovan.erp_v1.request.EventLogRequest;
import com.jovan.erp_v1.response.EventLogResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventLogService implements IEventLogService {

    private final EventLogRepository eventLogRepository;
    private final ShipmentRepository shipmentRepository;

    @Override
    public EventLogResponse addEvent(EventLogRequest request) {
        Shipment shipment = shipmentRepository.findById(request.shipmentId())
            .orElseThrow(() -> new RuntimeException("Shipment not found"));

        EventLog log = new EventLog();
        log.setTimestamp(request.timestamp());
        log.setDescription(request.description());
        log.setShipment(shipment);

        EventLog saved = eventLogRepository.save(log);
        return new EventLogResponse(saved);
    }

    @Override
    public List<EventLogResponse> getEventsForShipment(Long shipmentId) {
        return eventLogRepository.findByShipmentId(shipmentId)
                .stream()
                .map(EventLogResponse::new)
                .toList();
    }

    @Override
    public EventLogResponse update(Long id, EventLogRequest request) {
        EventLog eventLog = eventLogRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("EventLog not found"));

        eventLog.setTimestamp(request.timestamp());
        eventLog.setDescription(request.description());

        // opciono: update shipment ako request ima shipmentId različit
        if (!eventLog.getShipment().getId().equals(request.shipmentId())) {
            Shipment shipment = shipmentRepository.findById(request.shipmentId())
                .orElseThrow(() -> new EntityNotFoundException("Shipment not found"));
            eventLog.setShipment(shipment);
        }

        eventLog = eventLogRepository.save(eventLog);
        return new EventLogResponse(eventLog);
    }

    @Override
    public void delete(Long id) {
        if (!eventLogRepository.existsById(id)) {
            throw new EntityNotFoundException("EventLog not found");
        }
        eventLogRepository.deleteById(id);
    }

    @Override
    public EventLogResponse findOne(Long id) {
        return eventLogRepository.findById(id)
            .map(EventLogResponse::new)
            .orElseThrow(() -> new EntityNotFoundException("EventLog not found"));
    }

    @Override
    public List<EventLogResponse> findAll() {
        return eventLogRepository.findAll()
            .stream()
            .map(EventLogResponse::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<EventLogResponse> findByShipmentId(Long shipmentId) {
        return eventLogRepository.findByShipmentId(shipmentId)
            .stream()
            .map(EventLogResponse::new)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<EventLogResponse> findLatestForShipment(Long shipmentId) {
        return eventLogRepository.findTopByShipmentIdOrderByTimestampDesc(shipmentId)
            .map(EventLogResponse::new);
    }

	@Override
	public List<EventLogResponse> findByTimestampAfter(LocalDateTime date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventLogResponse> findByTimestampBetween(LocalDateTime start, LocalDateTime end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventLogResponse> findByDescriptionContaining(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EventLogResponse> findByShipmentIdAndTimestampBetween(Long shipmentId, LocalDateTime from,
			LocalDateTime to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventLogResponse findTopByShipmentIdOrderByTimestampDesc(Long shipmentId) {
		// TODO Auto-generated method stub
		return null;
	}
}