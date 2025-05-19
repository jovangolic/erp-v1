package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.model.SupplyItem;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupplyItemResponse {

	private Long id;
	private Long procurementId;
	private Long vendorId;
	private String vendorName;
	private BigDecimal cost;
	
	
	public SupplyItemResponse(SupplyItem supplyItem) {
		this.id = supplyItem.getId();
		this.procurementId = supplyItem.getProcurement().getId();
		this.vendorId = supplyItem.getSupplier().getId();
		this.vendorName = supplyItem.getSupplier().getName();
		this.cost = supplyItem.getCost();
	}
}
