package com.jovan.erp_v1.mapper;

import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import com.jovan.erp_v1.model.EventLog;
import com.jovan.erp_v1.model.Shipment;
import com.jovan.erp_v1.request.EventLogRequest;
import com.jovan.erp_v1.response.EventLogResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class EventLogMapper extends AbstractMapper<EventLogRequest> {

	public EventLog toEntity(EventLogRequest request, Shipment sh) {
		Objects.requireNonNull(request, "EventLogRequest must not be null");
		Objects.requireNonNull(sh, "Shipment must not be null");
		validateIdForCreate(request, EventLogRequest::id);
		EventLog log = new EventLog();
		log.setId(request.id());
		log.setDescription(request.description());
		log.setShipment(sh);
		return log;
	}
	
	public EventLog toUpdateEntity(EventLog log, EventLogRequest request, Shipment sh) {
		Objects.requireNonNull(request, "EventLogRequest must not be null");
		Objects.requireNonNull(log, "EventLog must not be null");
		Objects.requireNonNull(sh, "Shipment must not be null");
		validateIdForUpdate(request, EventLogRequest::id);
		return buildEventLogFromRequest(log, request,sh);
	}
	
	private EventLog buildEventLogFromRequest(EventLog log, EventLogRequest request, Shipment sh) {
		log.setDescription(request.description());
		log.setShipment(sh);
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
}
