package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.exception.LogisticsProviderErrorException;
import com.jovan.erp_v1.exception.OutboundDeliveryErrorException;
import com.jovan.erp_v1.exception.StorageEmployeeNotFoundException;
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
        LogisticsProvider provider = logisticsProviderRepository.findById(request.providerId())
                .orElseThrow(() -> new LogisticsProviderErrorException("Logistics-provider not found"));
        sh.setProvider(provider);
        OutboundDelivery delivery = out.findById(request.outboundDeliveryId())
                .orElseThrow(() -> new OutboundDeliveryErrorException("OuboundDelivery not found"));
        sh.setOutboundDelivery(delivery);
        TrackingInfoRequest infoReq = request.trackingInfo();
        TrackingInfo info = new TrackingInfo();
        info.setTrackingNumber(infoReq.trackingNumber());
        info.setCurrentLocation(infoReq.currentLocation());
        info.setEstimatedDelivery(infoReq.estimatedDelivery());
        info.setCurrentStatus(infoReq.currentStatus());
        info.setShipment(sh); // važno: povezivanje shipment i trackingInfo dvosmerno
        sh.setTrackingInfo(info);
        Storage store = storageRepository.findById(request.originStorageId())
                .orElseThrow(() -> new StorageEmployeeNotFoundException("Storage not found"));
        sh.setOriginStorage(store);
        return sh;
    }

    public ShipmentResponse toResponse(Shipment ship) {
        return new ShipmentResponse(ship);
    }

    public List<ShipmentResponse> toResponseList(List<Shipment> s) {
        return s.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
