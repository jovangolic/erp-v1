package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.statistics.fiscal_year.FiscalYearMonthlyStatDTO;
import com.jovan.erp_v1.statistics.fiscal_year.FiscalYearQuarterStatDTO;
import com.jovan.erp_v1.statistics.fiscal_year.FiscalYearStatusStatDTO;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;

@Repository
public interface FiscalYearRepository extends JpaRepository<FiscalYear, Long>, JpaSpecificationExecutor<FiscalYear> {

    // List<FiscalYear> findByStatus(FiscalYearStatus status);

    @Query("SELECT fy FROM FiscalYear fy WHERE fy.startDate >= :start AND fy.endDate <= :end")
    List<FiscalYear> findBetweenStartAndEndDates(@Param("start") LocalDate start, @Param("end") LocalDate end);

    Optional<FiscalYear> findByYear(Integer year);

    Optional<FiscalYear> findByYearStatusAndYear(FiscalYearStatus status, Integer year);

    Optional<FiscalYear> findFirstByYearStatusOrderByStartDateDesc(FiscalYearStatus status);

    List<FiscalYear> findByStartDateAfter(LocalDate date);

    List<FiscalYear> findByEndDateBefore(LocalDate date);

    List<FiscalYear> findByYearStatus(FiscalYearStatus yearStatus);

    List<FiscalYear> findByQuarterStatus(FiscalQuarterStatus quarterStatus);

    // Kvartali "manji od" prosledjenog
    @Query("""
                SELECT f FROM FiscalYear f
                WHERE
                    CASE f.quarterStatus
                        WHEN 'Q1' THEN 1
                        WHEN 'Q2' THEN 2
                        WHEN 'Q3' THEN 3
                        WHEN 'Q4' THEN 4
                    END <
                    CASE :quarterStatus
                        WHEN 'Q1' THEN 1
                        WHEN 'Q2' THEN 2
                        WHEN 'Q3' THEN 3
                        WHEN 'Q4' THEN 4
                    END
            """)
    List<FiscalYear> findByQuarterLessThan(@Param("quarterStatus") FiscalQuarterStatus quarterStatus);

    // Kvartali "veci od" prosledjenog
    @Query("""
                SELECT f FROM FiscalYear f
                WHERE
                    CASE f.quarterStatus
                        WHEN 'Q1' THEN 1
                        WHEN 'Q2' THEN 2
                        WHEN 'Q3' THEN 3
                        WHEN 'Q4' THEN 4
                    END >
                    CASE :quarterStatus
                        WHEN 'Q1' THEN 1
                        WHEN 'Q2' THEN 2
                        WHEN 'Q3' THEN 3
                        WHEN 'Q4' THEN 4
                    END
            """)
    List<FiscalYear> findByQuarterGreaterThan(@Param("quarterStatus") FiscalQuarterStatus quarterStatus);

    @Query("SELECT f FROM FiscalYear f LEFT JOIN FETCH f.quarters WHERE f.id = :id")
    List<FiscalYear> trackFiscalYear(@Param("id") Long id);

    @Query("""
            SELECT new com.jovan.erp_v1.statistics.fiscal_year.FiscalYearMonthlyStatDTO(
            CAST(FUNCTION('YEAR', f.startDate) as Integer),
            CAST(FUNCTION('MONTH', f.startDate) as Integer),
            COUNT(f)) FROM FiscalYear f
            GROUP BY FUNCTION('YEAR' , f.startDate), FUNCTION('MONTH' , f.startDate)
            ORDER BY FUNCTION('YEAR' , f.startDate), FUNCTION('MONTH' , f.startDate)
            """)
    List<FiscalYearMonthlyStatDTO> countFiscalYearsByYearAndMonth();

    @Query("SELECT new com.jovan.erp_v1.statistics.fiscal_year.FiscalYearStatusStatDTO(f.yearStatus, COUNT(f))"
            + "FROM FiscalYear f GROUP BY f.yearStatus")
    List<FiscalYearStatusStatDTO> countByFiscalYearStatus();

    @Query("SELECT new com.jovan.erp_v1.statistics.fiscal_year.FiscalYearQuarterStatDTO(f.quarterStatus, COUNT(f))"
            + "FROM FiscalYear f GROUP BY f.quarterStatus")
    List<FiscalYearQuarterStatDTO> countByFiscalYearQuarterStatus();
}
