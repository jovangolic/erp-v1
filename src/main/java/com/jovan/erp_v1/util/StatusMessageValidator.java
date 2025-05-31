package com.jovan.erp_v1.util;

import java.util.List;

import com.jovan.erp_v1.annotation.ValidStatusMessage;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StatusMessageValidator implements ConstraintValidator<ValidStatusMessage, String> {

    private static final List<String> allowed = List.of("Running", "Maintenance", "Error");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return allowed.contains(value);
    }
}
