package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;
import com.jovan.erp_v1.model.IncomeStatement;
import com.jovan.erp_v1.statistics.income_statement.IncomeStatementExpensesStatDTO;
import com.jovan.erp_v1.statistics.income_statement.IncomeStatementNetProfitStatDTO;
import com.jovan.erp_v1.statistics.income_statement.IncomeStatementRevenuStatDTO;

@Repository
public interface IncomeStatementRepository extends JpaRepository<IncomeStatement, Long>, JpaSpecificationExecutor<IncomeStatement> {

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
    
    //nove metode
    
    @Query("SELECT SUM(i.netProfit) FROM IncomeStatement i WHERE i.fiscalYear.id = :fiscalYearId")
    BigDecimal calculateTotalNetProfitByFiscalYear(@Param("fiscalYearId") Long fiscalYearId);
    @Query("SELECT SUM(i.totalRevenue - i.totalExpenses) FROM IncomeStatement i WHERE i.fiscalYear.id = :fiscalYearId")
    BigDecimal findTotalNetProfitByFiscalYear(@Param("fiscalYearId") Long fiscalYearId);
    List<IncomeStatement> findByTotalRevenueGreaterThan(BigDecimal totalRevenue);
    List<IncomeStatement> findByTotalExpensesGreaterThan(BigDecimal totalExpenses);
    List<IncomeStatement> findByNetProfitGreaterThan(BigDecimal netProfit);
    List<IncomeStatement> findByTotalRevenueLessThan(BigDecimal totalRevenue);
    List<IncomeStatement> findByTotalExpensesLessThan(BigDecimal totalExpenses);
    List<IncomeStatement> findByNetProfitLessThan(BigDecimal netProfit);
    List<IncomeStatement> findByFiscalYear_YearStatus(FiscalYearStatus yearStatus);
    @Query("SELECT is FROM IncomeStatement is WHERE is.fiscalYear.yearStatus = :yearStatus AND is.fiscalYear.quarterStatus = :quarterStatus")
    List<IncomeStatement> findByFiscalYear_QuarterStatusAndYearStatus(@Param("yearStatus") FiscalYearStatus yearStatus,@Param("quarterStatus") FiscalQuarterStatus quarterStatus);
    @Query("SELECT SUM(i.totalRevenue) FROM IncomeStatement i WHERE i.periodStart >= :start AND i.periodEnd <= :end")
    BigDecimal sumTotalRevenueBetweenDates(@Param("start") LocalDate start, @Param("end") LocalDate end);
    @Query("SELECT SUM(i.totalExpenses) FROM IncomeStatement i WHERE i.periodStart >= :start AND i.periodEnd <= :end")
    BigDecimal sumTotalExpensesBetweenDates(@Param("start") LocalDate start, @Param("end") LocalDate end);
    @Query("SELECT SUM(i.netProfit) FROM IncomeStatement i WHERE i.periodStart >= :start AND i.periodEnd <= :end")
    BigDecimal sumNetProfitBetweenDates(@Param("start") LocalDate start, @Param("end") LocalDate end);
    @Query("SELECT SUM(i.netProfit) FROM IncomeStatement i WHERE i.fiscalYear.quarterStatus = :quarterStatus")
    BigDecimal sumNetProfitByQuarterStatus(@Param("quarterStatus") FiscalQuarterStatus quarterStatus);
    @Query("SELECT i FROM IncomeStatement i WHERE i.fiscalYear.quarterStatus = :quarterStatus AND i.totalRevenue > :minRevenue")
    List<IncomeStatement> findByQuarterStatusAndMinRevenue(@Param("quarterStatus") FiscalQuarterStatus quarterStatus,
                                                           @Param("minRevenue") BigDecimal minRevenue);
    @Query("SELECT SUM(i.totalRevenue) FROM IncomeStatement i WHERE i.fiscalYear.yearStatus = :yearStatus")
    BigDecimal sumRevenueByFiscalYearStatus(@Param("yearStatus") FiscalYearStatus yearStatus);
    List<IncomeStatement> findByFiscalYear_StartDate(LocalDate startDate);
    List<IncomeStatement> findByFiscalYear_EndDate(LocalDate endDate);
    @Query("SELECT MONTH(i.periodStart) AS mesec, YEAR(i.periodStart) AS godina, SUM(i.netProfit) AS ukupnaDobit " +
    	       "FROM IncomeStatement i " +
    	       "WHERE YEAR(i.periodStart) = :year " +
    	       "GROUP BY YEAR(i.periodStart), MONTH(i.periodStart) " +
    	       "ORDER BY mesec")
    List<Object[]> findMonthlyNetProfitByYear(@Param("year") Integer year);
    @Query("SELECT SUM(i.totalRevenue) FROM IncomeStatement i WHERE i.periodStart >= :start AND i.periodEnd <= :end")
    BigDecimal sumTotalRevenue(@Param("start") LocalDate start, @Param("end") LocalDate end);
    @Query("SELECT SUM(i.totalExpenses) FROM IncomeStatement i WHERE i.periodStart >= :start AND i.periodEnd <= :end")
    BigDecimal sumTotalExpenses(@Param("start") LocalDate start, @Param("end") LocalDate end);
    @Query("SELECT SUM(i.netProfit) FROM IncomeStatement i WHERE i.periodStart >= :start AND i.periodEnd <= :end")
    BigDecimal sumNetProfit(@Param("start") LocalDate start, @Param("end") LocalDate end);
    @Query("SELECT SUM(i.netProfit) FROM IncomeStatement i WHERE i.fiscalYear.yearStatus = :yearStatus")
    BigDecimal sumNetProfitByYearStatus(@Param("yearStatus") FiscalYearStatus yearStatus);
    
    //nove metode
    
    @Query("SELECT is FROM IncomeStatement is WHERE is.id = :id")
    Optional<IncomeStatement> trackIncomeStatement(@Param("id") Long id);
    
    @Query("""
    	    SELECT new com.jovan.erp_v1.statistics.income_statement.IncomeStatementRevenuStatDTO(
    	        COUNT(in),
    	        SUM(in.totalRevenue),
    	        in.fiscalYear.id,
    	        in.fiscalYear.year
    	    )
    	    FROM IncomeStatement in
    	    WHERE in.confirmed = true
    	    GROUP BY in.fiscalYear.id, in.fiscalYear.year
    """)
    List<IncomeStatementRevenuStatDTO> countIncomeStatementRevenuByFiscalYear();
    
    @Query("""
    	    SELECT new com.jovan.erp_v1.statistics.income_statement.IncomeStatementExpensesStatDTO(
    	        COUNT(in),
    	        SUM(in.totalExpenses),
    	        in.fiscalYear.id,
    	        in.fiscalYear.year
    	    )
    	    FROM IncomeStatement in
    	    WHERE in.confirmed = true
    	    GROUP BY in.fiscalYear.id, in.fiscalYear.year
    """)
    List<IncomeStatementExpensesStatDTO> countIncomeStatementExpensesByFiscalYear();
    
    @Query("""
    		SELECT new com.jovan.erp_v1.statistics.income_statement.IncomeStatementNetProfitStatDTO(
    		COUNT(in),
    		SUM(in.netProfit),
    		in.fiscalYear.id,
    	    in.fiscalYear.year
    	    )
    	    FROM IncomeStatement in
    	    WHERE in.confirmed = true
    	    GROUP BY in.fiscalYear.id, in.fiscalYear.year
    """)
    List<IncomeStatementNetProfitStatDTO> countIncomeStatementNetProfitByFiscalYear();
}
