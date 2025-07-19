package com.jovan.erp_v1.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.ShipmentStatus;
import com.jovan.erp_v1.model.TrackingInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingInfoResponse {

    private Long id;
    private String trackingNumber;
    private String currentLocation;
    private LocalDate estimatedDelivery;
    private ShipmentStatus currentStatus;
    private ShipmentResponse shipment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TrackingInfoResponse(TrackingInfo info) {
        this.id = info.getId();
        this.trackingNumber = info.getTrackingNumber();
        this.currentLocation = info.getCurrentLocation();
        this.estimatedDelivery = info.getEstimatedDelivery();
        this.currentStatus = info.getCurrentStatus();
        this.shipment = info.getShipment() != null ? new ShipmentResponse(info.getShipment()) : null;
        this.createdAt = info.getCreatedAt();
        this.updatedAt = info.getUpdatedAt();
    }
}
