package com.jovan.erp_v1.statistics.capacity_planning;

import java.math.BigDecimal;

public record CapacityPlanningAvailableCapacityStatDTO(
		BigDecimal available,
		Long count
		) {
}
