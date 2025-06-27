package com.jovan.erp_v1.util;

import java.time.LocalDate;
import java.util.IllegalFormatFlagsException;

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

}
