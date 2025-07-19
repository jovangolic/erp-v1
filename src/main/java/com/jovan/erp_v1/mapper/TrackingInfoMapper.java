package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.ShipmentNotFoundException;
import com.jovan.erp_v1.exception.TrackingInfoErrorException;
import com.jovan.erp_v1.model.Shipment;
import com.jovan.erp_v1.model.TrackingInfo;
import com.jovan.erp_v1.repository.ShipmentRepository;
import com.jovan.erp_v1.request.TrackingInfoRequest;
import com.jovan.erp_v1.response.TrackingInfoResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TrackingInfoMapper {

    private final ShipmentRepository shipmentRepository;

    /**
     * Kreira novi TrackingInfo entitet na osnovu TrackingInfoRequest objekta.
     * Koristi se prilikom kreiranja nove pošiljke ili direktnog kreiranja
     * TrackingInfo entiteta.
     */
    public TrackingInfo toEntity(TrackingInfoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("TrackingInfoRequest is null");
        }
        TrackingInfo info = new TrackingInfo();
        info.setTrackingNumber(request.trackingNumber());
        info.setCurrentLocation(request.currentLocation());
        info.setEstimatedDelivery(request.estimatedDelivery());
        info.setCurrentStatus(request.currentStatus());
        Shipment ship = shipmentRepository.findById(request.shipmentId())
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found " + request.shipmentId()));
        info.setShipment(ship);
        return info;
    }

    /**
     * Ažurira postojeći TrackingInfo entitet na osnovu novog TrackingInfoRequest
     * objekta.
     * Ova metoda ne menja `id`, `createdAt` i `shipment` reference – koristi se u
     * update operacijama.
     */
    public void updateEntity(TrackingInfo existing, TrackingInfoRequest request) {
        if (existing == null || request == null) {
            throw new TrackingInfoErrorException("TrackingInfo or request is null");
        }
        existing.setTrackingNumber(request.trackingNumber());
        existing.setCurrentLocation(request.currentLocation());
        existing.setEstimatedDelivery(request.estimatedDelivery());
        existing.setCurrentStatus(request.currentStatus());
        // Ako sme da se menja shipment (retko), moze se ovo dodati:
        /*
         * if (!existing.getShipment().getId().equals(request.shipmentId())) {
         * Shipment newShip = shipmentRepository.findById(request.shipmentId())
         * .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found " +
         * request.shipmentId()));
         * existing.setShipment(newShip);
         * }
         */
    }

    /**
     * Kreira TrackingInfoResponse DTO na osnovu entiteta.
     */
    public TrackingInfoResponse toResponse(TrackingInfo info) {
        if (info == null) {
            return null;
        }
        return new TrackingInfoResponse(info);
    }

    /**
     * Kreira listu TrackingInfoResponse DTO-ova na osnovu liste entiteta.
     */
    public List<TrackingInfoResponse> toResponseList(List<TrackingInfo> infos) {
    	if(infos == null || infos.isEmpty()) {
    		return Collections.emptyList();
    	}
        return infos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
