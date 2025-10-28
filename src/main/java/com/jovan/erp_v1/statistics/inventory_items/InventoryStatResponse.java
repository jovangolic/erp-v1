package com.jovan.erp_v1.statistics.inventory_items;

import java.math.BigDecimal;
import java.util.List;

import com.jovan.erp_v1.model.InventoryItems;

import lombok.Builder;

@Builder
public record InventoryStatResponse(
		Long inventoryId,
	    Long totalItemsCount,
	    BigDecimal totalQuantity,
	    BigDecimal totalItemCondition,
	    BigDecimal totalDifference,
	    List<InventoryItems> productStats
	    //List<QuantityByProductStatDTO> productStats
		) {
}
