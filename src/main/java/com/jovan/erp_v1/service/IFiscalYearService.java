package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;

import com.jovan.erp_v1.request.FiscalYearRequest;
import com.jovan.erp_v1.response.FiscalYearResponse;

public interface IFiscalYearService {

    FiscalYearResponse create(FiscalYearRequest request);

    FiscalYearResponse update(Long id, FiscalYearRequest request);

    void delete(Long id);

    FiscalYearResponse findOne(Long id);

    List<FiscalYearResponse> findAll();

    // List<FiscalYearResponse> findByStatus(FiscalYearStatus status);

    List<FiscalYearResponse> findBetweenStartAndEndDates(LocalDate start, LocalDate end);

    FiscalYearResponse findByYear(Integer year);

    FiscalYearResponse findByYearStatusAndYear(FiscalYearStatus status, Integer year);

    FiscalYearResponse findFirstByYearStatusOrderByStartDateDesc(FiscalYearStatus status);

    List<FiscalYearResponse> findByStartDateAfter(LocalDate date);

    List<FiscalYearResponse> findByEndDateBefore(LocalDate date);

    List<FiscalYearResponse> findByYearStatus(FiscalYearStatus yearStatus);

    List<FiscalYearResponse> findByQuarterStatus(FiscalQuarterStatus quarterStatus);

    List<FiscalYearResponse> findByQuarterLessThan(FiscalQuarterStatus quarterStatus);

    List<FiscalYearResponse> findByQuarterGreaterThan(FiscalQuarterStatus quarterStatus);
}
