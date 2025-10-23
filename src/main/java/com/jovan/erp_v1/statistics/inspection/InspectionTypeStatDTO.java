package com.jovan.erp_v1.statistics.inspection;

import com.jovan.erp_v1.enumeration.InspectionType;

public record InspectionTypeStatDTO(
		InspectionType type,
		Long count
		) {
}
