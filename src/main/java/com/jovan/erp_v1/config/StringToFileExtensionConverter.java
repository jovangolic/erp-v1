package com.jovan.erp_v1.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.jovan.erp_v1.enumeration.FileExtension;

@Component
public class StringToFileExtensionConverter implements Converter<String, FileExtension> {

    @Override
    public FileExtension convert(String source) {
        return FileExtension.valueOf(source.toUpperCase());
    }
}
