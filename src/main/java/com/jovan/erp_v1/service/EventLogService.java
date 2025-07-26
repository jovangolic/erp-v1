package com.jovan.erp_v1.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.EventLogMapper;
import com.jovan.erp_v1.model.EventLog;
import com.jovan.erp_v1.model.Shipment;
import com.jovan.erp_v1.repository.EventLogRepository;
import com.jovan.erp_v1.repository.ShipmentRepository;
import com.jovan.erp_v1.request.EventLogRequest;
import com.jovan.erp_v1.response.EventLogResponse;
import com.jovan.erp_v1.util.DateValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventLogService implements IEventLogService {

    private final EventLogRepository eventLogRepository;
    private final ShipmentRepository shipmentRepository;
    private final EventLogMapper eventLogMapper;

    @Override
    public EventLogResponse addEvent(EventLogRequest request) {
    	DateValidator.validateNotNull(request.timestamp(), "Timestamp");
    	validateString(request.description());
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
    	validateShipmentId(shipmentId);
    	List<EventLog> logs = eventLogRepository.findByShipmentId(shipmentId);
    	if(logs.isEmpty()) {
    		String msg = String.format("No EventLog for shipment %d is found", shipmentId);
    		throw new NoDataFoundException(msg);
    	}
        return logs
                .stream()
                .map(EventLogResponse::new)
                .toList();
    }

    @Override
    public EventLogResponse update(Long id, EventLogRequest request) {
        EventLog eventLog = eventLogRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("EventLog not found"));
        validateEventLogRequest(request);
        eventLog.setTimestamp(request.timestamp());
        eventLog.setDescription(request.description());
        Shipment shipment = eventLog.getShipment();
        // opciono: update shipment ako request ima shipmentId razlicit
        if (!eventLog.getShipment().getId().equals(request.shipmentId())) {
            shipment = validateShipmentId(request.shipmentId());
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
    	List<EventLog> logs = eventLogRepository.findAll();
    	if(logs.isEmpty()) {
    		throw new NoDataFoundException("EventLog list is empty");
    	}
        return logs
            .stream()
            .map(EventLogResponse::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<EventLogResponse> findByShipmentId(Long shipmentId) {
    	validateShipmentId(shipmentId);
    	List<EventLog> logs = eventLogRepository.findByShipmentId(shipmentId);
    	if(logs.isEmpty()) {
    		String msg = String.format("No EventLog for shipment-id %d is found", shipmentId);
    		throw new NoDataFoundException(msg);
    	}
        return logs
            .stream()
            .map(EventLogResponse::new)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<EventLogResponse> findLatestForShipment(Long shipmentId) {
    	validateShipmentId(shipmentId);
        return eventLogRepository.findTopByShipmentIdOrderByTimestampDesc(shipmentId)
            .map(EventLogResponse::new);
    }

	@Override
	public List<EventLogResponse> findByTimestampAfter(LocalDateTime date) {
		DateValidator.validateNotInPast(date, "Date after");
		List<EventLog> logs = eventLogRepository.findByTimestampAfter(date);
		if(logs.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No EventLog for timestamp after %s is found", date.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return logs.stream().map(eventLogMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<EventLogResponse> findByTimestampBetween(LocalDateTime start, LocalDateTime end) {
		DateValidator.validateRange(start, end);
		List<EventLog> logs = eventLogRepository.findByTimestampBetween(start, end);
		if(logs.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No EventLog for timestamp between %s and %s is found", 
					start.format(formatter), end.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return logs.stream().map(eventLogMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<EventLogResponse> findByDescriptionContaining(String text) {
		validateString(text);
		List<EventLog> logs = eventLogRepository.findByDescriptionContaining(text);
		if(logs.isEmpty()) {
			String msg = String.format("No EventLog for description containing text %s is found", text);
			throw new NoDataFoundException(msg);
		}
		return logs.stream().map(eventLogMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<EventLogResponse> findByShipmentIdAndTimestampBetween(Long shipmentId, LocalDateTime from,
			LocalDateTime to) {
		validateShipmentId(shipmentId);
		DateValidator.validateRange(from, to);
		List<EventLog> logs = eventLogRepository.findByShipmentIdAndTimestampBetween(shipmentId, from, to);
		if(logs.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String msg = String.format("No EvenLog for shipmentId %d and timpstamp between %s and %s is found", 
					shipmentId,from.format(formatter),to.format(formatter));
			throw new NoDataFoundException(msg);
		}
		return logs.stream().map(eventLogMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public EventLogResponse findTopByShipmentIdOrderByTimestampDesc(Long shipmentId) {
		validateShipmentId(shipmentId);
		EventLog log = eventLogRepository.findTopByShipmentIdOrderByTimestampDesc(shipmentId).orElseThrow(() -> new ValidationException("Log for shipmentId is not found"));
		return new EventLogResponse(log);
	}
	
	private void validateString(String str) {
		if(str == null ||str.trim().isEmpty()) {
			throw new ValidationException("String must not be null nor empty");
		}
	}
	
	private Shipment validateShipmentId(Long shipId) {
		if(shipId == null) {
			throw new ValidationException("Shipment ID must not be null");
		}
		return shipmentRepository.findById(shipId).orElseThrow(() -> new ValidationException("Shipment not found with id "+shipId));
	}
	
	private void validateEventLogRequest(EventLogRequest request) {
		if(request == null) {
			throw new ValidationException("EventLogRequest request must not be null");
		}
		DateValidator.validateNotNull(request.timestamp(), "Timestamp");
		validateShipmentId(request.shipmentId());
	}
}