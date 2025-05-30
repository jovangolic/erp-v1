package com.jovan.erp_v1.mapper;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.enumeration.LanguageCodeType;
import com.jovan.erp_v1.enumeration.LanguageNameType;
import com.jovan.erp_v1.model.Language;
import com.jovan.erp_v1.request.LanguageRequest;
import com.jovan.erp_v1.response.LanguageResponse;

@Component
public class LanguageMapper {

    public Language toEntity(LanguageRequest request) {
        Language language = new Language();
        language.setLanguageCodeType(LanguageCodeType.valueOf(request.getLanguageCodeType()));
        language.setLanguageNameType(LanguageNameType.valueOf(request.getLanguageNameType()));
        return language;
    }

    public LanguageResponse toResponse(Language language) {
        return new LanguageResponse(
                language.getId(),
                language.getLanguageCodeType().name(),
                language.getLanguageNameType().name());
    }
}
