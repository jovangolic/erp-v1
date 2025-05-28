package com.jovan.erp_v1.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.jovan.erp_v1.enumeration.OptionCategory;

@Component
public class StringToOptionCategoryConverter implements Converter<String, OptionCategory> {

    @Override
    public OptionCategory convert(String source) {
        try {
            return OptionCategory.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nepoznata vrednost za OptionCategory: " + source);
        }
    }
}
