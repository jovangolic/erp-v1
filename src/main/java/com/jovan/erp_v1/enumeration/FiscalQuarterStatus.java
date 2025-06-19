package com.jovan.erp_v1.enumeration;

public enum FiscalQuarterStatus {

    Q1, Q2, Q3, Q4;

    public int getOrder() {
        return switch (this) {
            case Q1 -> 1;
            case Q2 -> 2;
            case Q3 -> 3;
            case Q4 -> 4;
        };
    }
}
