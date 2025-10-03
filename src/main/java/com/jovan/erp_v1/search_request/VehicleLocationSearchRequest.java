package com.jovan.erp_v1.search_request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VehicleLocationSearchRequest(
		Long id,
		Long vehicleId,
		Long vehicleIdFrom,
		Long vehicleIdTo,
		BigDecimal latitude,
		BigDecimal longitude,
		LocalDateTime recordedAtFrom,
		LocalDateTime recordedAtTo
		) {

}
