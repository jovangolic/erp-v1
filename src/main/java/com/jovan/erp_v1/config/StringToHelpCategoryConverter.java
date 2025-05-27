package com.jovan.erp_v1.config;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.enumeration.HelpCategory;

import org.springframework.core.convert.converter.Converter;

@Component
public class StringToHelpCategoryConverter implements Converter<String, HelpCategory> {

    @Override
    public HelpCategory convert(String source) {
        return HelpCategory.valueOf(source.toUpperCase());
    }
}
