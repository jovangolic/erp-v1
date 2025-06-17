package com.jovan.erp_v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jovan.erp_v1.exception.LocalizedOptionErrorException;
import com.jovan.erp_v1.exception.OptionErrorException;
import com.jovan.erp_v1.mapper.LocalizedOptionMapper;
import com.jovan.erp_v1.model.Language;
import com.jovan.erp_v1.model.LocalizedOption;
import com.jovan.erp_v1.model.Option;
import com.jovan.erp_v1.repository.LanguageRepository;
import com.jovan.erp_v1.repository.LocalizedOptionRepository;
import com.jovan.erp_v1.repository.OptionRepository;
import com.jovan.erp_v1.request.LocalizedOptionRequest;
import com.jovan.erp_v1.response.LocalizedOptionResponse;
import com.jovan.erp_v1.exception.LanguageErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocalizedOptionService implements ILocalizedOptionService {

    private final LocalizedOptionRepository localizedOptionRepository;
    private final LocalizedOptionMapper localizedOptionMapper;
    private final LanguageRepository languageRepository;
    private final OptionRepository optionRepository;

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

    @Override
    public void delete(Long id) {
        if (!localizedOptionRepository.existsById(id)) {
            throw new LocalizedOptionErrorException("LocalizedOption not found " + id);
        }
        localizedOptionRepository.deleteById(id);
    }

    @Override
    public LocalizedOptionResponse update(Long id, LocalizedOptionRequest request) {
        LocalizedOption local = localizedOptionRepository.findById(id)
                .orElseThrow(() -> new LocalizedOptionErrorException("LocalizedOption not found " + id));
        Option opt = optionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new OptionErrorException("Option not found"));
        Language lang = languageRepository.findById(request.getLanguageId())
                .orElseThrow(() -> new LanguageErrorException("Language not found"));
        local.setLocalizedLabel(request.getLocalizedLabel());
        local.setLanguage(lang);
        local.setOption(opt);
        LocalizedOption saved = localizedOptionRepository.save(local);
        return new LocalizedOptionResponse(saved);
    }

    @Override
    public void deleteAllByOptionId(Long optionId) {
        if (!optionRepository.existsById(optionId)) {
            throw new OptionErrorException("Option not found " + optionId);
        }
        localizedOptionRepository.deleteByOptionId(optionId);
    }

    @Override
    public LocalizedOptionResponse getTranslation(Long optionId, Long languageId) {
        return localizedOptionRepository.findByOptionIdAndLanguageId(optionId, languageId)
                .map(localizedOptionMapper::toResponse)
                .orElseThrow(() -> new OptionErrorException("Prevod nije pronađen"));
    }

    @Override
    public List<LocalizedOptionResponse> getAllByLanguage(Long languageId) {
        List<LocalizedOption> lista = localizedOptionRepository.findByLanguageId(languageId);
        return lista.stream().map(localizedOptionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LocalizedOptionResponse findOne(Long id) {
        LocalizedOption opt = localizedOptionRepository.findById(id)
                .orElseThrow(() -> new LocalizedOptionErrorException("LocalizedOption not found " + id));
        return new LocalizedOptionResponse(opt);
    }
}
