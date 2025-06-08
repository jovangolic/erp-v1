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
    private Long providerId;
    private Long outboundDeliveryId;
    private Long trackingInfoId;
    private Long originStorageId;

    public ShipmentResponse(Shipment ship) {
        this.id = ship.getId();
        this.shipmentDate = ship.getShipmentDate();
        this.status = ship.getStatus();
        this.providerId = ship.getProvider().getId();
        this.outboundDeliveryId = ship.getOutboundDelivery().getId();
        this.trackingInfoId = ship.getTrackingInfo().getId();
        this.originStorageId = ship.getOriginStorage().getId();
    }
}
