package com.jovan.erp_v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionQuantityAcceptedSummaryDTO {

	private Long totalCount;
    private Long totalQuantityAccepted;
}
