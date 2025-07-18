package com.jovan.erp_v1.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.IllegalFormatFlagsException;

import com.jovan.erp_v1.exception.LedgerEntryErrorException;

public class DateValidator {

    public static void validateNotNull(LocalDate date, String fieldName) {
        if (date == null) {
            throw new IllegalArgumentException(fieldName + " ne sme biti prazan.");
        }
    }

    public static void validateNotInFuture(LocalDate date, String fieldName) {
        if (date == null) {
            throw new IllegalArgumentException(fieldName + " ne sme biti prazan.");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(fieldName + " ne sme biti u budućnosti.");
        }
    }

    public static void validateRange(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalFormatFlagsException("Start i End datumi ne smeju niti null");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Početni datum ne može biti posle krajnjeg datuma.");
        }
    }

    public static void validatePastOrPresent(LocalDate date, String fieldName) {
        if (date == null) {
            throw new IllegalArgumentException(fieldName + " ne sme biti prazan.");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(fieldName + " ne sme biti u budućnosti.");
        }
    }

    public static void validateFutureOrPresent(LocalDate date, String fieldName) {
        if (date == null) {
            throw new IllegalArgumentException(fieldName + " ne sme biti prazan.");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(fieldName + " mora biti danas ili u budućnosti.");
        }
    }
    
    public static void validateNotInPast(LocalDate dateTime, String fieldName) {
    	if (dateTime == null) {
            throw new IllegalArgumentException(fieldName + " ne sme biti prazan.");
        }
        if (dateTime.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(fieldName + " ne sme biti u prošlosti.");
        }
    }
    
    public static void validateNotNull(LocalDateTime dateTime, String fieldName) {
        if (dateTime == null) {
            throw new IllegalArgumentException(fieldName + " ne sme biti prazan.");
        }
    }

    public static void validateNotInFuture(LocalDateTime dateTime, String fieldName) {
        if (dateTime == null) {
            throw new IllegalArgumentException(fieldName + " ne sme biti prazan.");
        }
        if (dateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(fieldName + " ne sme biti u budućnosti.");
        }
    }

    public static void validateRange(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start i End datumi ne smeju biti null");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Početni datum ne može biti posle krajnjeg datuma.");
        }
    }

    public static void validatePastOrPresent(LocalDateTime dateTime, String fieldName) {
        if (dateTime == null) {
            throw new IllegalArgumentException(fieldName + " ne sme biti prazan.");
        }
        if (dateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(fieldName + " ne sme biti u budućnosti.");
        }
    }
    
    public static void validateNotInPast(LocalDateTime dateTime, String fieldName) {
    	if (dateTime == null) {
            throw new IllegalArgumentException(fieldName + " ne sme biti prazan.");
        }
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(fieldName + " ne sme biti u prošlosti.");
        }
    }

    public static void validateFutureOrPresent(LocalDateTime dateTime, String fieldName) {
        if (dateTime == null) {
            throw new IllegalArgumentException(fieldName + " ne sme biti prazan.");
        }
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(fieldName + " mora biti danas ili u budućnosti.");
        }
    }
    
    public void validateDateRange(LocalDateTime date, LocalDateTime min, LocalDateTime max) {
        if (date.isBefore(min)) {
            throw new LedgerEntryErrorException("Entry date is too far in the past.");
        }
        if (date.isAfter(max)) {
            throw new LedgerEntryErrorException("Entry date is too far in the future.");
        }
    }

}
