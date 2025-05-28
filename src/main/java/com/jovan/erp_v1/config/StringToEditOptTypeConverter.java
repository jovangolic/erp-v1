package com.jovan.erp_v1.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.jovan.erp_v1.enumeration.EditOptType;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;

import org.springframework.stereotype.Component;

@Component
public class StringToEditOptTypeConverter implements Converter<String, EditOptType> {

    @Override
    public EditOptType convert(String source) {
        try {
            return EditOptType.valueOf(source.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new ConversionFailedException(
                    TypeDescriptor.valueOf(String.class),
                    TypeDescriptor.valueOf(EditOptType.class),
                    source,
                    ex);
        }
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(String.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(EditOptType.class);
    }
}
