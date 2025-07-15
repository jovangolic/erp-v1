package com.jovan.erp_v1.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Za statistiku po jednoj nabavci. Ovaj DTO nema ID jer nije potreban
 */
@Data
@NoArgsConstructor
public class ProcurementStatsPerEntityResponse {

	private Long procurementId;
	private Long count;
	private BigDecimal sum;
	private BigDecimal average;
	private BigDecimal min;
	private BigDecimal max;

	public ProcurementStatsPerEntityResponse(Long procurementId, Long count, BigDecimal sum, BigDecimal average, BigDecimal min, BigDecimal max) {
		this.procurementId = procurementId;
		this.count = count;
		this.sum = sum;
		this.average = average;
		this.min = min;
		this.max = max;
	}
}
