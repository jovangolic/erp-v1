package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.model.BalanceSheet;
import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface BalanceSheetRepository extends JpaRepository<BalanceSheet, Long>, JpaSpecificationExecutor<BalanceSheet> {

    List<BalanceSheet> findByTotalAssets(BigDecimal totalAssets);
    List<BalanceSheet> findByTotalAssetsGreaterThan(BigDecimal totalAssets);
    List<BalanceSheet> findByTotalAssetsLessThan(BigDecimal totalAssets);
    Optional<BalanceSheet> findByDate(LocalDate date);
    List<BalanceSheet> findByDateBetween(LocalDate start, LocalDate end);
    List<BalanceSheet> findByTotalLiabilities(BigDecimal totalLiabilities);
    List<BalanceSheet> findByTotalEquity(BigDecimal totalEquity);
    List<BalanceSheet> findByTotalEquityGreaterThan(BigDecimal totalEquity);
    List<BalanceSheet> findByTotalEquityLessThan(BigDecimal totalEquity);
    List<BalanceSheet> findByFiscalYear_Id(Long id);
    List<BalanceSheet> findByFiscalYear_Year(Integer year);
    List<BalanceSheet> findByFiscalYear_YearStatus(FiscalYearStatus yearStatus);
    List<BalanceSheet> findByFiscalYear_QuarterStatus(FiscalQuarterStatus quarterStatus);
    @Query("SELECT b FROM BalanceSheet b WHERE b.fiscalYear.yearStatus = :status AND b.date BETWEEN :start AND :end")
    List<BalanceSheet> findByStatusAndDateRange(@Param("status") FiscalYearStatus status,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);
    List<BalanceSheet> findByTotalLiabilitiesLessThan(BigDecimal totalLiabilities);
    List<BalanceSheet> findByTotalLiabilitiesGreaterThan(BigDecimal totalLiabilities);
    @Query("SELECT b FROM BalanceSheet b WHERE b.totalAssets > b.totalLiabilities")
    List<BalanceSheet> findSolventBalanceSheets();
    BalanceSheet findFirstByOrderByDateDesc();
    
    //nove metode
    @Query("SELECT b FROM BalanceSheet b WHERE b.id = :id")
    Optional<BalanceSheet> trackBalanceSheet(@Param("id") Long id);
}
