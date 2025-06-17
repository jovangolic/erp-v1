package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.request.LocalizedOptionRequest;
import com.jovan.erp_v1.response.LocalizedOptionResponse;

public interface ILocalizedOptionService {

    LocalizedOptionResponse create(LocalizedOptionRequest request);

    List<LocalizedOptionResponse> getAll();

    List<LocalizedOptionResponse> getTranslationsForOption(Long optionId);

    LocalizedOptionResponse addTranslationForOption(Long optionId, LocalizedOptionRequest request);

    void delete(Long id);

    LocalizedOptionResponse update(Long id, LocalizedOptionRequest request);

    void deleteAllByOptionId(Long optionId);

    LocalizedOptionResponse getTranslation(Long optionId, Long languageId);

    List<LocalizedOptionResponse> getAllByLanguage(Long languageId);

    LocalizedOptionResponse findOne(Long id);
}
