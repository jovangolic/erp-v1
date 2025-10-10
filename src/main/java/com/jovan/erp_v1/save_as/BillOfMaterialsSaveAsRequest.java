package com.jovan.erp_v1.save_as;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.BillOfMaterialsStatus;

import jakarta.validation.constraints.NotNull;

public record BillOfMaterialsSaveAsRequest(
		@NotNull Long sourceId,
		/** Novi parent proizvod (ako se kopira u drugi proizvod) */
	    Long newParentProductId,
	    /** Novi naziv ili oznaka ako je potrebno */
	    String newParentProductName,
	    /** Novi component (ako se zeli promeniti komponenta) */
	    Long newComponentId,
	    /** Novi naziv komponente — opcionalno, za prikaz */
	    String newComponentName,
	    /** Nova kolicina (ako se menja u kopiji) */
	    @NotNull BigDecimal quantity,
	    /** Da li odmah potvrdjujemo novi BOM */
	    Boolean confirmed,
	    /** Novi status — po defaultu NEW */
	    BillOfMaterialsStatus status
		) {
}
