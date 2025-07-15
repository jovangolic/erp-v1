package com.jovan.erp_v1.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SumCostGroupedByProcurementResponse {

	private long procurementId;
	private BigDecimal sumCost;
	
	public SumCostGroupedByProcurementResponse(Long procurementId, BigDecimal sumCost) {
		this.procurementId = procurementId;
		this.sumCost = sumCost;
	}
}
