package com.jovan.erp_v1.enumeration;

public enum InventoryStatus {

	PENDING,        // Inventura čeka da počne
    IN_PROGRESS,    // Inventura je u toku
    COMPLETED,      // Inventura je završena
    CANCELLED,      // Inventura je otkazana
    RECONCILED,     // Razlike su usklađene
    PARTIALLY_COMPLETED // Delimično završena inventura
}
