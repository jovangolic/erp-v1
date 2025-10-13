package com.jovan.erp_v1.service;

import java.time.LocalDate;
import java.util.List;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalQuarterTypeStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.request.FiscalQuarterRequest;
import com.jovan.erp_v1.response.FiscalQuarterResponse;
import com.jovan.erp_v1.save_as.FiscalQuarterSaveAsRequest;
import com.jovan.erp_v1.search_request.FiscalQuarterSearchRequest;

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
    List<FiscalQuarterResponse> findByStartDateAfter(LocalDate date);
    List<FiscalQuarterResponse> findByStartDateBefore(LocalDate date);
    List<FiscalQuarterResponse> findByFiscalYear_Id(Long fiscalYearId);
    List<FiscalQuarterResponse> findByEndDate(LocalDate endDate);
    
    //nove metode
    List<FiscalQuarterResponse> findByQuarterStatusQ1();
    List<FiscalQuarterResponse> findByQuarterStatusQ2();
    List<FiscalQuarterResponse> findByQuarterStatusQ3();
    List<FiscalQuarterResponse> findByQuarterStatusQ4();
    List<FiscalQuarterResponse> findByFiscalYearStatusOpen();
    List<FiscalQuarterResponse> findByFiscalYearStatusClosed();
    List<FiscalQuarterResponse> findByFiscalYearStatusArchived();
    List<FiscalQuarterResponse> findByFiscalYearStartDate(LocalDate startDate);
    List<FiscalQuarterResponse> findByFiscalYearStartDateAfter(LocalDate startDate);
    List<FiscalQuarterResponse> findByFiscalYearStartDateBefore(LocalDate startDate);
    List<FiscalQuarterResponse> findByFiscalYearEndDate(LocalDate endDate);
    List<FiscalQuarterResponse> findByFiscalYearStartDateBetween(LocalDate start, LocalDate end);
    List<FiscalQuarterResponse> findByFiscalYearEndDateBetween(LocalDate start, LocalDate end);
    List<FiscalQuarterResponse> findByFiscalYear_QuarterStatus(FiscalQuarterStatus quarterStatus);
    List<FiscalQuarterResponse> findActiveQuarters();
    List<FiscalQuarterResponse> findQuartersEndingSoon( LocalDate date);
    List<FiscalQuarterResponse> findByFiscalYear_YearAndQuarterStatus(Integer year, FiscalQuarterStatus status);
    List<FiscalQuarterResponse> findByFiscalYearBetweenYears(Integer start,  Integer end);
    
    //nove metode
    FiscalQuarterResponse trackFiscalQuarter(Long id);
    FiscalQuarterResponse confirmFiscalQuarter(Long id);
    FiscalQuarterResponse cancelFiscalQuarter(Long id);
    FiscalQuarterResponse closeFiscalQuarter(Long id);
    FiscalQuarterResponse changeStatus(Long id, FiscalQuarterTypeStatus status);
    FiscalQuarterResponse saveFiscalQuarter(FiscalQuarterRequest request);
    FiscalQuarterResponse saveAs(FiscalQuarterSaveAsRequest request);
    List<FiscalQuarterResponse> saveAll(List<FiscalQuarterRequest> requests);
    List<FiscalQuarterResponse> generalSearch(FiscalQuarterSearchRequest request);
}
