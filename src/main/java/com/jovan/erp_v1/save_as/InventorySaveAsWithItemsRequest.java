package com.jovan.erp_v1.save_as;

import java.util.List;

import com.jovan.erp_v1.enumeration.InventoryStatus;
import com.jovan.erp_v1.enumeration.InventoryTypeStatus;

import jakarta.validation.constraints.NotNull;

public record InventorySaveAsWithItemsRequest(
		@NotNull Long sourceId,
		@NotNull Long storageEmployeeId,
		@NotNull Long storageForemanId,
		@NotNull Boolean aligned,
		@NotNull
		List<InventoryItemSaveAsRequest> inventoryItems,
		@NotNull InventoryStatus status,
		@NotNull InventoryTypeStatus typeStatus,
		@NotNull Boolean confirmed
		) {
}
