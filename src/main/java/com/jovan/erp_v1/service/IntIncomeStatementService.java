package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.jovan.erp_v1.dto.MonthlyNetProfitDTO;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.enumeration.IncomeStatementStatus;
import com.jovan.erp_v1.request.IncomeStatementRequest;
import com.jovan.erp_v1.response.IncomeStatementResponse;
import com.jovan.erp_v1.save_as.IncomeStatementSaveAsRequest;
import com.jovan.erp_v1.search_request.IncomeStatementSearchRequest;

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
    BigDecimal calculateTotalNetProfitByFiscalYear( Long fiscalYearId);
    BigDecimal findTotalNetProfitByFiscalYear( Long fiscalYearId);
    List<IncomeStatementResponse> findByTotalRevenueGreaterThan(BigDecimal totalRevenue);
    List<IncomeStatementResponse> findByTotalExpensesGreaterThan(BigDecimal totalExpenses);
    List<IncomeStatementResponse> findByNetProfitGreaterThan(BigDecimal netProfit);
    List<IncomeStatementResponse> findByTotalRevenueLessThan(BigDecimal totalRevenue);
    List<IncomeStatementResponse> findByTotalExpensesLessThan(BigDecimal totalExpenses);
    List<IncomeStatementResponse> findByNetProfitLessThan(BigDecimal netProfit);
    List<IncomeStatementResponse> findByFiscalYear_YearStatus(FiscalYearStatus yearStatus);
    List<IncomeStatementResponse> findByFiscalYear_QuarterStatusAndYearStatus(FiscalYearStatus yearStatus, FiscalQuarterStatus quarterStatus);
    BigDecimal sumTotalRevenueBetweenDates( LocalDate start,  LocalDate end);
    BigDecimal sumTotalExpensesBetweenDates( LocalDate start,  LocalDate end);
    BigDecimal sumNetProfitBetweenDates( LocalDate start,  LocalDate end);
    BigDecimal sumNetProfitByQuarterStatus( FiscalQuarterStatus quarterStatus);
    List<IncomeStatementResponse> findByQuarterStatusAndMinRevenue(FiscalQuarterStatus quarterStatus, BigDecimal minRevenue);
    BigDecimal sumRevenueByFiscalYearStatus( FiscalYearStatus yearStatus);
    List<IncomeStatementResponse> findByFiscalYear_StartDate(LocalDate startDate);
    List<IncomeStatementResponse> findByFiscalYear_EndDate(LocalDate endDate);
    BigDecimal sumTotalRevenue(LocalDate start, LocalDate end);
    BigDecimal sumTotalExpenses( LocalDate start,  LocalDate end);
    BigDecimal sumNetProfit( LocalDate start,  LocalDate end);
    BigDecimal sumNetProfitByYearStatus( FiscalYearStatus yearStatus);
    
    //nove metode
    IncomeStatementResponse trackIncomeStatement(Long id);
    IncomeStatementResponse confirmIncomeStatement(Long id);
    IncomeStatementResponse cancelIncomeStatement(Long id);
    IncomeStatementResponse closeIncomeStatement(Long id);
    IncomeStatementResponse changeStatus(Long id, IncomeStatementStatus status);
    IncomeStatementResponse saveIncomeStatement(IncomeStatementRequest request);
    IncomeStatementResponse saveAs(IncomeStatementSaveAsRequest request);
    List<IncomeStatementResponse> saveAll(List<IncomeStatementRequest> requests);
    List<IncomeStatementResponse> generalSearch(IncomeStatementSearchRequest request);
}
