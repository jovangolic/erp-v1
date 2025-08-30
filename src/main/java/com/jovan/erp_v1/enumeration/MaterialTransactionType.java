package com.jovan.erp_v1.enumeration;

public enum MaterialTransactionType {

    RECEIPT, // Prijem materijala od dobavljača
    RETURN, // Povrat materijala dobavljaču
    TRANSFER_TO_LAB, // Slanje materijala u laboratoriju
    SCRAP, // Otpisano / neupotrebljivo
    INTERNAL_USE, // Upotrebljeno interno bez kretanja u skladištima
    CORRECTION
}
