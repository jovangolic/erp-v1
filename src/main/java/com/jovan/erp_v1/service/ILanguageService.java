package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.enumeration.LanguageCodeType;
import com.jovan.erp_v1.enumeration.LanguageNameType;
import com.jovan.erp_v1.request.LanguageRequest;
import com.jovan.erp_v1.response.LanguageResponse;

public interface ILanguageService {

    LanguageResponse create(LanguageRequest request);
    LanguageResponse update(Long id, LanguageRequest request);
    void delete(Long id);
    LanguageResponse findOneById(Long id);
    List<LanguageResponse> getAll();
    LanguageResponse findByCodeType(LanguageCodeType codeType);
    LanguageResponse findByNameType(LanguageNameType nameType);

    LanguageResponse saveLanguage(LanguageRequest request);
}
