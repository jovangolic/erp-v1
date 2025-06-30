package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.FiscalQuarter;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;

@Repository
public interface FiscalQuarterRepository extends JpaRepository<FiscalQuarter, Long> {

	List<FiscalQuarter> findByFiscalYear_Year(Integer year);

	List<FiscalQuarter> findByFiscalYear_YearStatus(FiscalYearStatus yearStatus);

	List<FiscalQuarter> findByFiscalYear_StartDateBetween(LocalDate start, LocalDate end);

    List<FiscalQuarter> findByQuarterStatus(FiscalQuarterStatus status);

    List<FiscalQuarter> findByStartDateBetween(LocalDate start, LocalDate end);
    List<FiscalQuarter> findByFiscalYearIdAndQuarterStatus(Long fiscalYearId, FiscalQuarterStatus status);

    // Pronađi kvartale koji počinju posle datuma (greater than)
    List<FiscalQuarter> findByStartDateAfter(LocalDate date);

    List<FiscalQuarter> findByStartDateBefore(LocalDate date);

    List<FiscalQuarter> findByFiscalYear_Id(Long fiscalYearId);
    List<FiscalQuarter> findByEndDate(LocalDate endDate);
}
