package com.jovan.erp_v1.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AvgCostByProcurementResponse {

	private Long procurementId;
    private BigDecimal avgCost;

    public AvgCostByProcurementResponse(Long procurementId, BigDecimal avgCost) {
        this.procurementId = procurementId;
        this.avgCost = avgCost;
    }
}
