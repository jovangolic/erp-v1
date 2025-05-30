package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.mapper.LanguageMapper;
import com.jovan.erp_v1.model.Language;
import com.jovan.erp_v1.repository.LanguageRepository;
import com.jovan.erp_v1.request.LanguageRequest;
import com.jovan.erp_v1.response.LanguageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LanguageService implements ILanguageService {

    private final LanguageMapper languageMapper;
    private final LanguageRepository languageRepository;

    @Override
    public LanguageResponse create(LanguageRequest request) {
        Language language = languageMapper.toEntity(request);
        languageRepository.save(language);
        return languageMapper.toResponse(language);
    }

    @Override
    public List<LanguageResponse> getAll() {
        return languageRepository.findAll().stream()
                .map(languageMapper::toResponse)
                .collect(Collectors.toList());
    }
}
