package com.jovan.erp_v1.dto;

import java.math.BigDecimal;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProcurementStatsResponse {

	private Long count;
	private BigDecimal sum;
	private BigDecimal average;
	private BigDecimal min;
	private BigDecimal max;
	
	public ProcurementStatsResponse(Long count, BigDecimal sum, BigDecimal average, BigDecimal min, BigDecimal max) {
		this.count = count;
		this.sum = sum;
		this.average = average;
		this.min = min;
		this.max = max;
	}
}
