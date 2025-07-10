package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.exception.LogisticsProviderErrorException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.exception.StorageNotFoundException;
import com.jovan.erp_v1.model.LogisticsProvider;
import com.jovan.erp_v1.model.OutboundDelivery;
import com.jovan.erp_v1.model.Shipment;
import com.jovan.erp_v1.model.Storage;
import com.jovan.erp_v1.model.TrackingInfo;
import com.jovan.erp_v1.repository.LogisticsProviderRepository;
import com.jovan.erp_v1.repository.OutboundDeliveryRepository;
import com.jovan.erp_v1.repository.StorageRepository;
import com.jovan.erp_v1.request.ShipmentRequest;
import com.jovan.erp_v1.request.TrackingInfoRequest;
import com.jovan.erp_v1.response.ShipmentResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShipmentMapper {

    private final LogisticsProviderRepository logisticsProviderRepository;
    private final OutboundDeliveryRepository out;
    private final StorageRepository storageRepository;

    public Shipment toEntity(ShipmentRequest request) {
		Shipment sh = new Shipment();
		sh.setShipmentDate(request.shipmentDate());
		sh.setStatus(request.status());
		LogisticsProvider provider = fetchLogisticsProviderId(request.providerId());
		sh.setProvider(provider);
		OutboundDelivery delivery = fetchOutboundDeliveryId(request.outboundDeliveryId());
		sh.setOutboundDelivery(delivery);
		TrackingInfoRequest infoReq = request.trackingInfo();
	    TrackingInfo info = new TrackingInfo();
	    info.setTrackingNumber(infoReq.trackingNumber());
	    info.setCurrentLocation(infoReq.currentLocation());
	    info.setEstimatedDelivery(infoReq.estimatedDelivery());
	    info.setCurrentStatus(infoReq.currentStatus());
	    info.setShipment(sh); //važno: povezivanje shipment i trackingInfo dvosmerno
	    sh.setTrackingInfo(info);
		Storage store = fetchStorageId(request.originStorageId());
		sh.setOriginStorage(store);
		return sh;
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
    
    private OutboundDelivery fetchOutboundDeliveryId(Long outId) {
    	if(outId == null) {
    		throw new OutboundDeliveryErrorException("OutboundDelivery ID must not be null");
    	}
    	return out.findById(outId).orElseThrow(() -> new OutboundDeliveryErrorException("OuboundDelivery not found with id "+outId));
    }
    
    private Storage fetchStorageId(Long storageId) {
    	if(storageId == null) {
    		throw new StorageNotFoundException("Storage ID must not be null");
    	}
    	return storageRepository.findById(storageId).orElseThrow(() -> new StorageNotFoundException("Storage not found with id "+storageId));
    }
    
    private LogisticsProvider fetchLogisticsProviderId(Long logisticsId) {
    	if(logisticsId == null) {
    		throw new LogisticsProviderErrorException("Logistics-provider ID must not be null");
    	}
    	return logisticsProviderRepository.findById(logisticsId).orElseThrow(() -> new LogisticsProviderErrorException("Logistics-provider not found with id "+logisticsId));
    }
}
