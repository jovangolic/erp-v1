package com.jovan.erp_v1.exception;

import org.springframework.core.convert.TypeDescriptor;

public record ConversionFailedException(TypeDescriptor sourceType, TypeDescriptor targetType, Object value,
        Throwable cause) {

}
