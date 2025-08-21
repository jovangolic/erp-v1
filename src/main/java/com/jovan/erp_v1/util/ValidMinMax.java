package com.jovan.erp_v1.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinMaxValidator.class)
@Documented
public @interface ValidMinMax {

	String message() default "Min-value must be less than or equal to Max-value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
