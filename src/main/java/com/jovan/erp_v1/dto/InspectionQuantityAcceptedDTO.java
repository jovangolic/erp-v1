package com.jovan.erp_v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InspectionQuantityAcceptedDTO {

	private Long count;
	private Integer quantityAccepted;
}
