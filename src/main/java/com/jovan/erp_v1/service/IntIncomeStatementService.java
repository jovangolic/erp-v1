package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.dto.MonthlyNetProfitDTO;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.request.IncomeStatementRequest;
import com.jovan.erp_v1.response.IncomeStatementResponse;

public interface IntIncomeStatementService {

    IncomeStatementResponse create(IncomeStatementRequest request);
    IncomeStatementResponse update(Long id, IncomeStatementRequest request);
    void delete(Long id);
    IncomeStatementResponse findOne(Long id);
    List<IncomeStatementResponse> findAll();
    List<IncomeStatementResponse> findByTotalRevenue(BigDecimal totalRevenue);
    List<IncomeStatementResponse> findByTotalExpenses(BigDecimal totalExpenses);
    List<IncomeStatementResponse> findByNetProfit(BigDecimal netProfit);
    List<IncomeStatementResponse> findByFiscalYear_Year(Integer year);
    List<IncomeStatementResponse> findByFiscalYear_QuarterStatus(FiscalQuarterStatus quarterStatus);
    List<IncomeStatementResponse> findByPeriodStartBetween(LocalDate start, LocalDate end);
    List<IncomeStatementResponse> findByPeriodEndBetween(LocalDate start, LocalDate end);
    List<IncomeStatementResponse> findWithinPeriod(LocalDate start, LocalDate end);
    List<IncomeStatementResponse> findByDateWithinPeriod(LocalDate date);
    
    //nove metode
    List<MonthlyNetProfitDTO> getMonthlyNetProfitForYear(Integer year);
}
