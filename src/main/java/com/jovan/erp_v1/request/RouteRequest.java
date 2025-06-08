package com.jovan.erp_v1.request;

import jakarta.validation.constraints.NotNull;

public record RouteRequest(
        Long id,
        @NotNull String origin,
        @NotNull String destination,
        @NotNull Double distanceKm) {
}
