package com.jovan.erp_v1.response;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.model.Shipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentResponse {

    private Long id;
    private LocalDate shipmentDate;
    private ShipmentStatus status;
    private LogisticsProviderResponse logisticsProviderResponse;
    private OutboundDeliveryResponse outboundDeliveryResponse;
    private TrackingInfoBasicResponse trackingInfoBasicResponse;
    private StorageBasicResponse storageBasicResponse;

    public ShipmentResponse(Shipment ship) {
        this.id = ship.getId();
        this.shipmentDate = ship.getShipmentDate();
        this.status = ship.getStatus();
        this.logisticsProviderResponse = ship.getProvider() != null ? new LogisticsProviderResponse(ship.getProvider()) : null;
        this.outboundDeliveryResponse = ship.getOutboundDelivery() != null ? new OutboundDeliveryResponse(ship.getOutboundDelivery()) : null;
        this.trackingInfoBasicResponse = ship.getTrackingInfo() != null ? new TrackingInfoBasicResponse(ship.getTrackingInfo()) : null;
        this.storageBasicResponse = ship.getOriginStorage() != null ? new StorageBasicResponse(ship.getOriginStorage()) : null;
    }
}
