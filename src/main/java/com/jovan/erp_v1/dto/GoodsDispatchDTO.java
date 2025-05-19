package com.jovan.erp_v1.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDispatchDTO {

	/**
	 * DTO koji nosi podatke potrebne za potvrdu:
	 */
	private Long employeeId;
	private String employeeName;
	private Long shiftId;
    private List<GoodsItemDTO> items;
}
