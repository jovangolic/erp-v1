package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.jovan.erp_v1.model.Procurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcurementSupplyItemResponse {

	private Long id;
	private LocalDateTime date;
	private BigDecimal totalCost;

	public ProcurementSupplyItemResponse(Procurement procurement) {
		this.id = procurement.getId();
		this.date = procurement.getLocdate();
		this.totalCost = procurement.getTotalCost();
	}
}
