package com.jovan.erp_v1.statistics.defects;

import com.jovan.erp_v1.enumeration.DefectStatus;
import com.jovan.erp_v1.enumeration.SeverityLevel;

/**
 *Defect statistic for severity level and status and their count
 */
public record DefectStatusSeverityStatDTO(
		DefectStatus status,
	    SeverityLevel severity,
	    Long count
		) {
}
