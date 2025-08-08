package com.jovan.erp_v1.enumeration;

public enum InventoryStatus {

	PENDING,        // Inventura ceka da pocne
    IN_PROGRESS,    // Inventura je u toku
    COMPLETED,      // Inventura je zavrsena
    CANCELLED,      // Inventura je otkazana
    RECONCILED,     // Razlike su usklađene
    PARTIALLY_COMPLETED, // Delimicno zavrsena inventura
    PENDING_APPROVAL, // Inventura je zavrsena i čeka zvanicno odobrenje
    APPROVED // Inventura je zvanicno odobrena
}
