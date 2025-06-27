package com.jovan.erp_v1.enumeration;

public enum MaterialTransactionStatus {

    PENDING, // Čeka obradu / potvrdu
    APPROVED, // Odobreno od strane odgovorne osobe
    SENT_TO_LAB, // Poslato laboratoriji
    LAB_CONFIRMED, // Laboratorija potvrdila prijem
    COMPLETED, // Završeno
    REJECTED
}
