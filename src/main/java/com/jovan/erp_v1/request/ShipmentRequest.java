package com.jovan.erp_v1.request;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.ShipmentStatus;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ShipmentRequest(
                Long id,
                @NotNull LocalDate shipmentDate,
                @NotNull ShipmentStatus status,
                @NotNull Long providerId,
                @NotNull Long outboundDeliveryId,
                @NotNull TrackingInfoRequest trackingInfo, // složen objekat
                @NotNull Long originStorageId,
                @NotNull @Valid List<EventLogRequest> eventLogRequest) {
}