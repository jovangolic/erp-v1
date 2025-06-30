package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.model.FiscalQuarter;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.request.FiscalQuarterRequest;
import com.jovan.erp_v1.response.FiscalQuarterResponse;

public interface IFiscalQuarterService {

    FiscalQuarterResponse create(FiscalQuarterRequest request);

    FiscalQuarterResponse update(Long id, FiscalQuarterRequest request);

    void delete(Long id);

    FiscalQuarterResponse findOne(Long id);

    List<FiscalQuarterResponse> findAll();

    List<FiscalQuarterResponse> findByFiscalYear_Year(Integer year);

	List<FiscalQuarterResponse> findByFiscalYear_YearStatus(FiscalYearStatus yearStatus);

	List<FiscalQuarterResponse> findByFiscalYear_StartDateBetween(LocalDate start, LocalDate end);

    List<FiscalQuarterResponse> findByQuarterStatus(FiscalQuarterStatus status);

    List<FiscalQuarterResponse> findByStartDateBetween(LocalDate start, LocalDate end);
    List<FiscalQuarterResponse> findByFiscalYearIdAndQuarterStatus(Long fiscalYearId, FiscalQuarterStatus status);

    // Pronađi kvartale koji počinju posle datuma (greater than)
    List<FiscalQuarterResponse> findByStartDateAfter(LocalDate date);

    List<FiscalQuarterResponse> findByStartDateBefore(LocalDate date);

    List<FiscalQuarterResponse> findByFiscalYear_Id(Long fiscalYearId);
    List<FiscalQuarterResponse> findByEndDate(LocalDate endDate);
}
