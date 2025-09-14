package com.jovan.erp_v1.statistics.defects;

import com.jovan.erp_v1.enumeration.DefectStatus;

/**
 *Defect statistic for defect-status and their count
 */
public record DefectStatusStatDTO(
		DefectStatus status,
	    Long count
		) {
}
