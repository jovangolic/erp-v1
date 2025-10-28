package com.jovan.erp_v1.statistics.inventory_items;

import java.time.LocalDate;

public record InventoryStatRequest(
		Long inventoryId,                 // konkretan inventar
	    Long productId,                   // opcionalno – konkretan proizvod
	    Long storageId,                   // opcionalno – konkretno skladiste
	    LocalDate fromDate,               // opcionalno – period od
	    LocalDate toDate,                 // opcionalno – period do
	    Boolean onlyConfirmed 
		) {
}
