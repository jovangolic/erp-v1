package com.jovan.erp_v1.search_request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.CapacityPlanningStatus;

public record CapacityPlanningSearchRequest(
		Long id,
		Long idFrom,
		Long idTo,
		Long workCenterId,
		Long workCenterIdFrom,
		Long workCenterIdTo,
		String workCenterName,
		String workCenterLoc,
		BigDecimal workCenterCapacity,
		BigDecimal workCenterCapacityMin,
		BigDecimal workCenterCapacityMax,
		Long localStorageId,
		Long localStorageFrom,
		Long localStorageTo,
		LocalDate date,
		LocalDate dateBefore,
		LocalDate dateAfter,
		LocalDate dateStart,
		LocalDate dateEnd,
		BigDecimal availableCapacity,
		BigDecimal availableCapacityMin,
		BigDecimal availableCapacityMax,
		BigDecimal plannedLoad,
		BigDecimal plannedLoadMin,
		BigDecimal plannedLoadMax,
		BigDecimal remainingCapacity,
		BigDecimal remainingCapacityMin,
		BigDecimal remainingCapacityMax,
		Boolean confirmed,
		CapacityPlanningStatus status
		) {
}
