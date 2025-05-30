package com.jovan.erp_v1.mapper;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.LocalizedOption;
import com.jovan.erp_v1.repository.LanguageRepository;
import com.jovan.erp_v1.repository.OptionRepository;
import com.jovan.erp_v1.request.LocalizedOptionRequest;
import com.jovan.erp_v1.response.LocalizedOptionResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LocalizedOptionMapper {

    private final OptionRepository optionRepository;
    private final LanguageRepository languageRepository;

    public LocalizedOption toEntity(LocalizedOptionRequest request) {
        LocalizedOption entity = new LocalizedOption();
        entity.setOption(optionRepository.findById(request.getOptionId()).orElseThrow());
        entity.setLanguage(languageRepository.findById(request.getLanguageId()).orElseThrow());
        entity.setLocalizedLabel(request.getLocalizedLabel());
        return entity;
    }

    public LocalizedOptionResponse toResponse(LocalizedOption entity) {
        return new LocalizedOptionResponse(
                entity.getId(),
                entity.getOption().getId(),
                entity.getLanguage().getId(),
                entity.getLocalizedLabel());
    }
}
