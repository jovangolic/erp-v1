package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.request.LogisticsProviderRequest;
import com.jovan.erp_v1.response.LogisticsProviderResponse;

public interface ILogisticsProviderService {

    LogisticsProviderResponse create(LogisticsProviderRequest request);

    LogisticsProviderResponse update(Long id, LogisticsProviderRequest request);

    void delete(Long id);

    LogisticsProviderResponse findOneById(Long id);

    List<LogisticsProviderResponse> findAll();

    List<LogisticsProviderResponse> findByName(String name);

    List<LogisticsProviderResponse> findByNameContainingIgnoreCase(String fragment);

    List<LogisticsProviderResponse> searchByNameOrWebsite(String query);

    LogisticsProviderResponse findByContactPhone(String contactPhone);

    LogisticsProviderResponse findByEmail(String email);

    LogisticsProviderResponse findByWebsite(String website);
}
