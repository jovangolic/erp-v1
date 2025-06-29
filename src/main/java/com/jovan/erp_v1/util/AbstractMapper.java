package com.jovan.erp_v1.util;

import java.util.function.Function;

/**
 * Apstraktna mapper klasa koja proverava validnost ID-ija
 * */
public abstract class AbstractMapper<R> {

	protected void validateIdForCreate(R request, Function<R, Long> idExtractor) {
        Long id = idExtractor.apply(request);
        if (id != null) {
            throw new IllegalArgumentException("ID mora biti null prilikom kreiranja entiteta.");
        }
    }

    protected void validateIdForUpdate(R request, Function<R, Long> idExtractor) {
        Long id = idExtractor.apply(request);
        if (id == null) {
            throw new IllegalArgumentException("ID ne sme biti null prilikom ažuriranja entiteta.");
        }
    }
}
