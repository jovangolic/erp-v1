package com.jovan.erp_v1.request;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.TripStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TripRequest(
		Long id,
        @NotBlank String startLocation,
        @NotBlank String endLocation,
        @NotNull LocalDateTime startTime,
        LocalDateTime endTime,
        @NotNull TripStatus status,
        @NotNull Long driverId,
        Boolean confirmed
		) {

}
