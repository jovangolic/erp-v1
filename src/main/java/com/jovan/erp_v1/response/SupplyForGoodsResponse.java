package com.jovan.erp_v1.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jovan.erp_v1.model.Supply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplyForGoodsResponse {

	private Long id;
	private StorageResponse storageResponse;
	private BigDecimal quantity;
	private LocalDateTime updates;
	
	public SupplyForGoodsResponse(Supply supply) {
		this.id = supply.getId();
		this.storageResponse = supply.getStorage() != null ? new StorageResponse(supply.getStorage()) : null;
		this.quantity = supply.getQuantity();
		this.updates = supply.getUpdates();
	}
}
