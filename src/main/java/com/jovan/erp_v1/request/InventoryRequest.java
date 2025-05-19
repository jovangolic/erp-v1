package com.jovan.erp_v1.request;

import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.enumeration.InventoryStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public record InventoryRequest(
		
		Long id,
		@NotNull(message = "Zaposleni u skladištu je obavezan")
		Long storageEmployeeId,

		@NotNull(message = "Skladištar je obavezan")
		Long storageForemanId,

		@NotNull(message = "Datum je obavezan")
		LocalDate date,

		@NotNull(message = "Usklađenost mora biti navedena")
		Boolean aligned,

		@NotEmpty(message = "Lista inventurne robe ne sme biti prazna")
		@Valid // da validira i svaki InventoryItemRequest
		List<InventoryItemsRequest> inventoryItems,

		@NotNull(message = "Status inventara je obavezan")
		InventoryStatus status
		) {
}

