package com.jovan.erp_v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsItemDTO {

	private Long id;
	private String name;
    private Double quantity;
    private String unit;
}
