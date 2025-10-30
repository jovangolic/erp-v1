package com.jovan.erp_v1.statistics.invoice;

/**
 *Enum objekat za tipove strategija, vezane za fakture i njene obrade u kodu
 */
public enum InvoiceStatStrategy {

	SQL,      // koristi SQL upite direktno u bazi
    MEMORY,   // koristi stream/agregaciju u memoriji
    AUTO      // sistem sam bira (npr. na osnovu broja faktura)
}
