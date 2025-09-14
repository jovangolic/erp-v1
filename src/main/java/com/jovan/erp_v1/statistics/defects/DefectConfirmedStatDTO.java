package com.jovan.erp_v1.statistics.defects;


/**
 *Defect statistic for confirmed status and their count
 */
public record DefectConfirmedStatDTO(
		Boolean confirmed,
	    Long count
		) {
}
