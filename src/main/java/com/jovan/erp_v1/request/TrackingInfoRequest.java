package com.jovan.erp_v1.request;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.ShipmentStatus;

import jakarta.validation.constraints.NotNull;

public record TrackingInfoRequest(
                Long id,
                @NotNull
                String trackingNumber,
                @NotNull
                String currentLocation,
                LocalDate estimatedDelivery,
                @NotNull
                ShipmentStatus currentStatus,
                @NotNull
                Long shipmentId) {
}
