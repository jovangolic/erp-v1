package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *A response object for generating a driver report
 */
public record DriverReportResponse(
		@NotNull Long driverId,
        @NotBlank String fullName,
        @NotBlank String phone,
        @NotNull Long totalTrips,
        @NotNull Long completedTrips,
        @NotNull Long cancelledTrips,
        @NotNull Long activeTrips,
        BigDecimal averageDurationInHours,
        BigDecimal totalRevenue
		) {
}
