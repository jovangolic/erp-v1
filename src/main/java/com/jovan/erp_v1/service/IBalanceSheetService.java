package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.enumeration.BalanceSheetStatus;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.request.BalanceSheetRequest;
import com.jovan.erp_v1.request.BalanceSheetSearchRequest;
import com.jovan.erp_v1.response.BalanceSheetResponse;
import com.jovan.erp_v1.save_as.BalanceSheetSaveAsRequest;
import com.jovan.erp_v1.search_request.BalanceSheetGeneralSearchRequest;

public interface IBalanceSheetService {

    BalanceSheetResponse create(BalanceSheetRequest request);
    BalanceSheetResponse update(Long id, BalanceSheetRequest request);
    void delete(Long id);
    BalanceSheetResponse findOne(Long id);
    List<BalanceSheetResponse> findAll();
    List<BalanceSheetResponse> findByTotalAssets(BigDecimal totalAssets);
    BalanceSheetResponse findByDate(LocalDate date);
    List<BalanceSheetResponse> findByDateBetween(LocalDate start, LocalDate end);
    List<BalanceSheetResponse> findByTotalLiabilities(BigDecimal totalLiabilities);
    List<BalanceSheetResponse> findByTotalEquity(BigDecimal totalEquity);
    List<BalanceSheetResponse> findByFiscalYear_Id(Long id);
    List<BalanceSheetResponse> findByFiscalYear_Year(Integer year);
    List<BalanceSheetResponse> findByFiscalYear_YearStatus(FiscalYearStatus yearStatus);
    List<BalanceSheetResponse> findByFiscalYear_QuarterStatus(FiscalQuarterStatus quarterStatus);
    List<BalanceSheetResponse> findByStatusAndDateRange(FiscalYearStatus status, LocalDate start, LocalDate end);
    List<BalanceSheetResponse> findByTotalAssetsGreaterThan(BigDecimal totalAssets);
    List<BalanceSheetResponse> findByTotalAssetsLessThan(BigDecimal totalAssets);
    List<BalanceSheetResponse> findByTotalEquityGreaterThan(BigDecimal totalEquity);
    List<BalanceSheetResponse> findByTotalEquityLessThan(BigDecimal totalEquity);
    //nove metode
    List<BalanceSheetResponse> searchBalanceSheets(LocalDate startDate, LocalDate endDate, Long fiscalYearId, BigDecimal minAssets);
    List<BalanceSheetResponse> findByTotalLiabilitiesLessThan(BigDecimal totalLiabilities);
    List<BalanceSheetResponse> findByTotalLiabilitiesGreaterThan(BigDecimal totalLiabilities);
    List<BalanceSheetResponse> findSolventBalanceSheets();
    BalanceSheetResponse findFirstByOrderByDateDesc();
    List<BalanceSheetResponse> searchBalanceSheets(BalanceSheetSearchRequest request);
    
    //nove metode
    BalanceSheetResponse trackBalanceSheet( Long id);
    BalanceSheetResponse confirmBalanceSheet( Long id);
    BalanceSheetResponse closeBalanceSheet( Long id);
    BalanceSheetResponse cancelBalanceSheet( Long id);
    BalanceSheetResponse changeStatus( Long id, BalanceSheetStatus status);
    BalanceSheetResponse saveBalanceSheet(BalanceSheetRequest request);
    BalanceSheetResponse saveAs(BalanceSheetSaveAsRequest request);
    List<BalanceSheetResponse> saveAll(List<BalanceSheetRequest> requests);
    List<BalanceSheetResponse> generalSearch(BalanceSheetGeneralSearchRequest req);
    
}
