package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.enumeration.FiscalYearTypeStatus;
import com.jovan.erp_v1.request.FiscalYearRequest;
import com.jovan.erp_v1.response.FiscalYearResponse;
import com.jovan.erp_v1.save_as.FiscalYearSaveAsRequest;
import com.jovan.erp_v1.search_request.FiscalYearSearchRequest;
import com.jovan.erp_v1.statistics.fiscal_year.FiscalYearMonthlyStatDTO;
import com.jovan.erp_v1.statistics.fiscal_year.FiscalYearQuarterStatDTO;
import com.jovan.erp_v1.statistics.fiscal_year.FiscalYearStatusStatDTO;

public interface IFiscalYearService {

    FiscalYearResponse create(FiscalYearRequest request);
    FiscalYearResponse update(Long id, FiscalYearRequest request);
    void delete(Long id);
    FiscalYearResponse findOne(Long id);
    List<FiscalYearResponse> findAll();
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
    
    //nove metode
    
    FiscalYearResponse trackFiscalYear(Long id);
    FiscalYearResponse confirmFiscalYear(Long id);
    FiscalYearResponse cancelFiscalYear(Long id);
    FiscalYearResponse closeFiscalYear(Long id);
    FiscalYearResponse changeStatus(Long id, FiscalYearTypeStatus status);
    List<FiscalYearMonthlyStatDTO> countFiscalYearsByYearAndMonth();
    List<FiscalYearStatusStatDTO> countByFiscalYearStatus();
    List<FiscalYearQuarterStatDTO> countByFiscalYearQuarterStatus();
    FiscalYearResponse saveFiscalYear(FiscalYearRequest request);
    FiscalYearResponse saveAs(FiscalYearSaveAsRequest req);
    List<FiscalYearResponse> saveAll(List<FiscalYearRequest> request);
    List<FiscalYearResponse> generalSearch(FiscalYearSearchRequest req);
}
