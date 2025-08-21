package com.jovan.erp_v1.util;

import java.math.BigDecimal;

import com.jovan.erp_v1.request.QualityStandardRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MinMaxValidator implements ConstraintValidator<ValidMinMax, QualityStandardRequest> {

    @Override
    public boolean isValid(QualityStandardRequest request, ConstraintValidatorContext context) {
        BigDecimal min = request.minValue();
        BigDecimal max = request.maxValue();

        if (min == null || max == null) {
            return true; // polja imaju svoje @NotNull validacije
        }

        return min.compareTo(max) <= 0;
    }
}
