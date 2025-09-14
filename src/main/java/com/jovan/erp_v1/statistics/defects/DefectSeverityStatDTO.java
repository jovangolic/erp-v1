package com.jovan.erp_v1.statistics.defects;

import com.jovan.erp_v1.enumeration.SeverityLevel;

/**
 *Defect statistic for severity level and their count
 */
public record DefectSeverityStatDTO(
		SeverityLevel severity,
	    Long count
		) {
}
