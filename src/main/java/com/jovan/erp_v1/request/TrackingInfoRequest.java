package com.jovan.erp_v1.request;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.ShipmentStatus;

public record TrackingInfoRequest(
        Long id,
        String trackingNumber,
        String currentLocation,
        LocalDate estimatedDelivery,
        ShipmentStatus currentStatus) {
}
