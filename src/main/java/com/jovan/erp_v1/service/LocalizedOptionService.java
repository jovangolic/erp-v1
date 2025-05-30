package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.mapper.LocalizedOptionMapper;
import com.jovan.erp_v1.model.LocalizedOption;
import com.jovan.erp_v1.repository.LocalizedOptionRepository;
import com.jovan.erp_v1.request.LocalizedOptionRequest;
import com.jovan.erp_v1.response.LocalizedOptionResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocalizedOptionService implements ILocalizedOptionService {

    private final LocalizedOptionRepository localizedOptionRepository;
    private final LocalizedOptionMapper localizedOptionMapper;

    @Transactional
    @Override
    public LocalizedOptionResponse create(LocalizedOptionRequest request) {
        LocalizedOption entity = localizedOptionMapper.toEntity(request);
        localizedOptionRepository.save(entity);
        return localizedOptionMapper.toResponse(entity);
    }

    @Override
    public List<LocalizedOptionResponse> getAll() {
        return localizedOptionRepository.findAll().stream()
                .map(localizedOptionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<LocalizedOptionResponse> getTranslationsForOption(Long optionId) {
        return localizedOptionRepository.findByOptionId(optionId).stream()
                .map(localizedOptionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LocalizedOptionResponse addTranslationForOption(Long optionId, LocalizedOptionRequest request) {
        request.setOptionId(optionId);
        return create(request);
    }
}
