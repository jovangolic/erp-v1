package com.jovan.erp_v1.statistics.inspection;

import com.jovan.erp_v1.enumeration.InspectionResult;

public record InspectionResultStatDTO(
		InspectionResult result,
		Long count
		) {
}
