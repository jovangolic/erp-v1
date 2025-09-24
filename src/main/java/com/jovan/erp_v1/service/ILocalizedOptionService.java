package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.enumeration.LanguageCodeType;
import com.jovan.erp_v1.enumeration.LanguageNameType;
import com.jovan.erp_v1.enumeration.OptionCategory;
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
    //nove metode
    List<LocalizedOptionResponse> findByOption_Label(String label);
    List<LocalizedOptionResponse> findByOption_Value(String value);
    List<LocalizedOptionResponse> findByOption_Category(OptionCategory category);
    List<LocalizedOptionResponse> findByLanguage_Id(Long languageId);
    List<LocalizedOptionResponse> findByLanguage_LanguageCodeType(LanguageCodeType languageCodeType);
    List<LocalizedOptionResponse> findByLanguage_LanguageNameType(LanguageNameType languageNameType);
    
    //save
    LocalizedOptionResponse saveLozalizedOptions(LocalizedOptionRequest request);
    //save_as
    LocalizedOptionResponse saveAs(Long sourceId, String newLabel);
    //save_all
    List<LocalizedOptionResponse> saveAll(List<LocalizedOptionRequest> requests);
}
