package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.model.Supply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplyForBarCodesResponse {

	private Long id;
	private BigDecimal quantity;
	private LocalDateTime updates;
	
	public SupplyForBarCodesResponse(Supply s) {
		this.id = s.getId();
		this.quantity =s.getQuantity();
		this.updates = s.getUpdates();
	}
}
