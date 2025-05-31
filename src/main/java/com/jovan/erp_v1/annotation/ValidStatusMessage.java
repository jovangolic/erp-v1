package com.jovan.erp_v1.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import com.jovan.erp_v1.util.StatusMessageValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StatusMessageValidator.class)
public @interface ValidStatusMessage {
    String message() default "Poruka nije validna";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
