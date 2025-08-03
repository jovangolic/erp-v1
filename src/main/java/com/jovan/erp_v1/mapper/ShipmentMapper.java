package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.EventLog;
import com.jovan.erp_v1.model.LogisticsProvider;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.model.Shipment;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.TrackingInfo;
import com.jovan.erp_v1.request.ShipmentRequest;
import com.jovan.erp_v1.request.TrackingInfoRequest;
import com.jovan.erp_v1.response.ShipmentResponse;
import com.jovan.erp_v1.util.AbstractMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShipmentMapper extends AbstractMapper<ShipmentRequest> {

    private final EventLogMapper eventLogMapper;
    private final TrackingInfoMapper trackingInfoMapper;

    public Shipment toEntity(ShipmentRequest request, LogisticsProvider provider, OutboundDelivery outboundDelivery, Storage originStorage) {
    	Objects.requireNonNull(request, "ShipmentRequest must not be null");
    	Objects.requireNonNull(outboundDelivery, "OutboundDelivery must not be null");
    	Objects.requireNonNull(originStorage, "Storage must not be null");
    	validateIdForCreate(request, ShipmentRequest::id);
		Shipment sh = new Shipment();
		sh.setId(request.id());
		sh.setStatus(request.status());
		sh.setProvider(provider);
		sh.setOutboundDelivery(outboundDelivery);
		TrackingInfoRequest infoReq = request.trackingInfo();
	    TrackingInfo info = new TrackingInfo();
	    info.setTrackingNumber(infoReq.trackingNumber());
	    info.setCurrentLocation(infoReq.currentLocation());
	    info.setCurrentStatus(infoReq.currentStatus());
	    info.setShipment(sh); //vazno: povezivanje shipment i trackingInfo, dvosmerno veza
	    sh.setTrackingInfo(info);
		sh.setOriginStorage(originStorage);
		List<EventLog> logs = request.eventLogRequest().stream()
			    .map(logReq -> eventLogMapper.toEntity(logReq, sh))
			    .collect(Collectors.toList());
		sh.setEventLogs(logs);
		return sh;
	}
    
    public Shipment toEntityUpdate(Shipment ship, ShipmentRequest request, LogisticsProvider provider, OutboundDelivery outboundDelivery,TrackingInfo info, Storage originStorage) {
    	Objects.requireNonNull(ship, "Shipment must not be null");
    	Objects.requireNonNull(request, "ShipmentRequest must not be null");
    	Objects.requireNonNull(outboundDelivery, "OutboundDelivery must not be null");
    	Objects.requireNonNull(originStorage, "Storage must not be null");
    	ship.setStatus(request.status());
    	ship.setProvider(provider);
    	ship.setOutboundDelivery(outboundDelivery);
    	TrackingInfo existing = ship.getTrackingInfo();
    	TrackingInfoRequest infoReq = request.trackingInfo();
    	trackingInfoMapper.updateEntity(existing, infoReq);
    	ship.setOriginStorage(originStorage);
    	List<EventLog> logs = request.eventLogRequest().stream()
			    .map(logReq -> eventLogMapper.toEntity(logReq, ship))
			    .collect(Collectors.toList());
		ship.setEventLogs(logs);
    	return ship;
    }
    
    public void updateEntity(TrackingInfo info, TrackingInfoRequest req) {
        info.setTrackingNumber(req.trackingNumber());
        info.setCurrentLocation(req.currentLocation());
        info.setCurrentStatus(req.currentStatus());
    }

    public ShipmentResponse toResponse(Shipment ship) {
    	Objects.requireNonNull(ship, "Shipment must not be null");
        return new ShipmentResponse(ship);
    }

    public List<ShipmentResponse> toResponseList(List<Shipment> s) {
    	if(s == null || s.isEmpty()) {
    		return Collections.emptyList();
    	}
        return s.stream().map(this::toResponse).collect(Collectors.toList());
    }
 
}
