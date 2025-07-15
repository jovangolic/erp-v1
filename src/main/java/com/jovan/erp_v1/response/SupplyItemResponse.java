package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.model.SupplyItem;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupplyItemResponse {

	private Long id;
	private ProcurementSupplyItemResponse procurementSupplyItemResponse;
	private VendorResponse vendorResponse;
	private BigDecimal cost;
	
	
	public SupplyItemResponse(SupplyItem supplyItem) {
		this.id = supplyItem.getId();
		this.procurementSupplyItemResponse = supplyItem.getProcurement() != null ? new ProcurementSupplyItemResponse(supplyItem.getProcurement()) : null;
		this.vendorResponse = supplyItem.getSupplier() != null ? new VendorResponse(supplyItem.getSupplier()) : null;
		this.cost = supplyItem.getCost();
	}
}
