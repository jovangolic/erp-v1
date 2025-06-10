package com.jovan.erp_v1.request;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.TransportStatus;

import jakarta.validation.constraints.NotNull;

public record TransportOrderRequest(
                Long id,
                @NotNull LocalDate scheduledDate,
                @NotNull Long vehicleId,
                @NotNull Long driversId,
                @NotNull TransportStatus status,
                @NotNull Long outboundDeliveryId) {
}
