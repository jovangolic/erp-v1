package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.request.LanguageRequest;
import com.jovan.erp_v1.response.LanguageResponse;

public interface ILanguageService {

    LanguageResponse create(LanguageRequest request);

    List<LanguageResponse> getAll();
}
