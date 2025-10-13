package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.jovan.erp_v1.model.FiscalQuarter;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;

@Repository
public interface FiscalQuarterRepository extends JpaRepository<FiscalQuarter, Long>, JpaSpecificationExecutor<FiscalQuarter> {

	List<FiscalQuarter> findByFiscalYear_Year(Integer year);
	List<FiscalQuarter> findByFiscalYear_YearStatus(FiscalYearStatus yearStatus);
	List<FiscalQuarter> findByFiscalYear_StartDateBetween(LocalDate start, LocalDate end);
    List<FiscalQuarter> findByQuarterStatus(FiscalQuarterStatus status);
    List<FiscalQuarter> findByStartDateBetween(LocalDate start, LocalDate end);
    List<FiscalQuarter> findByFiscalYearIdAndQuarterStatus(Long fiscalYearId, FiscalQuarterStatus status);
    List<FiscalQuarter> findByStartDateAfter(LocalDate date);
    List<FiscalQuarter> findByStartDateBefore(LocalDate date);
    List<FiscalQuarter> findByFiscalYear_Id(Long fiscalYearId);
    List<FiscalQuarter> findByEndDate(LocalDate endDate);
    
    //nove metode
    @Query("SELECT fq FROM FiscalQuarter fq WHERE fq.quarterStatus  = 'Q1'")
    List<FiscalQuarter> findByQuarterStatusQ1();
    @Query("SELECT fq FROM FiscalQuarter fq WHERE fq.quarterStatus  = 'Q2'")
    List<FiscalQuarter> findByQuarterStatusQ2();
    @Query("SELECT fq FROM FiscalQuarter fq WHERE fq.quarterStatus  = 'Q3'")
    List<FiscalQuarter> findByQuarterStatusQ3();
    @Query("SELECT fq FROM FiscalQuarter fq WHERE fq.quarterStatus  = 'Q4'")
    List<FiscalQuarter> findByQuarterStatusQ4();
    @Query("SELECT fq FROM FiscalQuarter fq WHERE fq.fiscalYear.yearStatus = 'OPEN'")
    List<FiscalQuarter> findByFiscalYearStatusOpen();
    @Query("SELECT fq FROM FiscalQuarter fq WHERE fq.fiscalYear.yearStatus = 'CLOSED'")
    List<FiscalQuarter> findByFiscalYearStatusClosed();
    @Query("SELECT fq FROM FiscalQuarter fq WHERE fq.fiscalYear.yearStatus = 'ARCHIVED'")
    List<FiscalQuarter> findByFiscalYearStatusArchived();
    List<FiscalQuarter> findByFiscalYearStartDate(LocalDate startDate);
    List<FiscalQuarter> findByFiscalYearStartDateAfter(LocalDate startDate);
    List<FiscalQuarter> findByFiscalYearStartDateBefore(LocalDate startDate);
    List<FiscalQuarter> findByFiscalYearEndDate(LocalDate endDate);
    List<FiscalQuarter> findByFiscalYearStartDateBetween(LocalDate start, LocalDate end);
    List<FiscalQuarter> findByFiscalYearEndDateBetween(LocalDate start, LocalDate end);
    List<FiscalQuarter> findByFiscalYear_QuarterStatus(FiscalQuarterStatus quarterStatus);
    @Query("SELECT fq FROM FiscalQuarter fq WHERE fq.startDate <= CURRENT_DATE AND fq.endDate >= CURRENT_DATE")
    List<FiscalQuarter> findActiveQuarters();
    @Query("SELECT fq FROM FiscalQuarter fq WHERE fq.endDate BETWEEN CURRENT_DATE AND :date")
    List<FiscalQuarter> findQuartersEndingSoon(@Param("date") LocalDate date);
    List<FiscalQuarter> findByFiscalYear_YearAndQuarterStatus(Integer year, FiscalQuarterStatus status);
    @Query("SELECT fq FROM FiscalQuarter fq WHERE fq.fiscalYear.year BETWEEN :start AND :end")
    List<FiscalQuarter> findByFiscalYearBetweenYears(@Param("start") Integer start, @Param("end") Integer end);
    
    //nove metode
    @Query("SELECT fq FROM FiscalQuarter fq WHERE fq.id = :id")
    Optional<FiscalQuarter> trackFiscalQuarter(@Param("id") Long id);
}
