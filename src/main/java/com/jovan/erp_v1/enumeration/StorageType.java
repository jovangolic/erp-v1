package com.jovan.erp_v1.enumeration;

public enum StorageType {

	PRODUCTION(false),
    DISTRIBUTION(false),
    YARD(false),
    SILO(false),
    COLD_STORAGE(false), 

    OPEN(false),
    CLOSED(false),
    INTERIM(false),
    AVAILABLE(false);

    private final boolean hasShelvesByDefault;

    StorageType(boolean hasShelvesByDefault) {
        this.hasShelvesByDefault = hasShelvesByDefault;
    }

    public boolean hasShelvesByDefault() {
        return hasShelvesByDefault;
    }
}
