package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.request.BalanceSheetRequest;
import com.jovan.erp_v1.response.BalanceSheetResponse;

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
}
