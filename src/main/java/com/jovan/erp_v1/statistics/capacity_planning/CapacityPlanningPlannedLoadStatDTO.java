package com.jovan.erp_v1.statistics.capacity_planning;

import java.math.BigDecimal;

public record CapacityPlanningPlannedLoadStatDTO(
		BigDecimal plannedLoad,
		Long count
		) {
}
