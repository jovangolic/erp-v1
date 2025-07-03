package com.jovan.erp_v1.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.Language;
import com.jovan.erp_v1.request.LanguageRequest;
import com.jovan.erp_v1.response.LanguageResponse;

@Component
public class LanguageMapper {

    public Language toEntity(LanguageRequest request) {
        Language language = new Language();
        language.setLanguageCodeType(request.getLanguageCodeType());
        language.setLanguageNameType(request.getLanguageNameType());
        return language;
    }

    public LanguageResponse toResponse(Language language) {
    	Objects.requireNonNull(language,"Language must not be null");
        return new LanguageResponse(
                language.getId(),
                language.getLanguageCodeType(),
                language.getLanguageNameType());
    }
}
