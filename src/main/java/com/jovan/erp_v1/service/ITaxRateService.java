package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.jovan.erp_v1.enumeration.TaxType;
import com.jovan.erp_v1.request.TaxRateRequest;
import com.jovan.erp_v1.response.TaxRateResponse;

public interface ITaxRateService {

    TaxRateResponse create(TaxRateRequest request);

    TaxRateResponse update(Long id, TaxRateRequest request);

    void delete(Long id);

    TaxRateResponse findOne(Long id);

    List<TaxRateResponse> findAll();

    List<TaxRateResponse> findByType(TaxType type);

    List<TaxRateResponse> findByTaxName(String taxName);

    List<TaxRateResponse> findByPercentage(BigDecimal percentage);

    List<TaxRateResponse> findByTaxNameAndPercentage(String taxName, BigDecimal percentage);

    List<TaxRateResponse> findByStartDateBeforeAndEndDateAfter(LocalDate date1, LocalDate date2);

    List<TaxRateResponse> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate date1, LocalDate date2);

    List<TaxRateResponse> findOverlapping(LocalDate start, LocalDate end);

    List<TaxRateResponse> findByStartDate(LocalDate startDate);

    List<TaxRateResponse> findByEndDate(LocalDate endDate);

    List<TaxRateResponse> findActiveByType(TaxType type, LocalDate date);

    List<TaxRateResponse> findByTypeAndPeriod(TaxType type, LocalDate startDate, LocalDate endDate);
}
