package com.jovan.erp_v1.enumeration;

public enum ReportType {

    // General
    USERS,
    ACTIVITY_LOG,

    // Skladiste
    INVENTORY,
    STOCK_MOVEMENT,
    STOCK_LEVELS,
    STOCK_DISCREPANCY,

    // Logistika
    DELIVERY_NOTE,
    TRANSPORT_PLAN,
    SHIPPING_STATUS,

    // Racunovodstvo
    INVOICE,
    PAYMENT_REPORT,
    BALANCE_SHEET,
    PROFIT_AND_LOSS,
    TAX_REPORT,

    // Materijali
    RAW_MATERIAL_USAGE,
    MATERIAL_ORDER_REPORT,
    SUPPLY_CHAIN_REPORT,

    // Proizvodnja
    PRODUCTION_PLAN,
    PRODUCTION_REPORT,
    WORK_ORDER_REPORT,

    // Porudzbine
    ORDERS
}
