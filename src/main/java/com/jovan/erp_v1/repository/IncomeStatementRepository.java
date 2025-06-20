package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.model.IncomeStatement;

@Repository
public interface IncomeStatementRepository extends JpaRepository<IncomeStatement, Long> {

    List<IncomeStatement> findByTotalRevenue(BigDecimal totalRevenue);

    List<IncomeStatement> findByTotalExpenses(BigDecimal totalExpenses);

    List<IncomeStatement> findByNetProfit(BigDecimal netProfit);

    List<IncomeStatement> findByFiscalYear_Year(Integer year);

    List<IncomeStatement> findByFiscalYear_QuarterStatus(FiscalQuarterStatus quarterStatus);

    List<IncomeStatement> findByPeriodStartBetween(LocalDate start, LocalDate end);

    List<IncomeStatement> findByPeriodEndBetween(LocalDate start, LocalDate end);

    @Query("SELECT i FROM IncomeStatement i WHERE i.periodStart >= :start AND i.periodEnd <= :end")
    List<IncomeStatement> findWithinPeriod(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT i FROM IncomeStatement i WHERE :date BETWEEN i.periodStart AND i.periodEnd")
    List<IncomeStatement> findByDateWithinPeriod(@Param("date") LocalDate date);

    List<IncomeStatement> findByFiscalYearId(Long id);

}
