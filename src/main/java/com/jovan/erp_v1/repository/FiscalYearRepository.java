package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.FiscalQuarter;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;

@Repository
public interface FiscalYearRepository extends JpaRepository<FiscalYear, Long> {

    // List<FiscalYear> findByStatus(FiscalYearStatus status);

    @Query("SELECT fy FROM FiscalYear fy WHERE fy.startDate >= :start AND fy.endDate <= :end")
    List<FiscalYear> findBetweenStartAndEndDates(@Param("start") LocalDate start, @Param("end") LocalDate end);

    Optional<FiscalYear> findByYear(int year);

    // Po statusu i godini
    Optional<FiscalYear> findByYearStatusAndYear(FiscalYearStatus status, Integer year);

    // Trenutno otvorena fiskalna godina
    Optional<FiscalYear> findFirstByYearStatusOrderByStartDateDesc(FiscalYearStatus status);

    // Sve koje počinju posle određenog datuma
    List<FiscalYear> findByStartDateAfter(LocalDate date);

    // Sve koje se završavaju pre određenog datuma
    List<FiscalYear> findByEndDateBefore(LocalDate date);

    List<FiscalYear> findByYearStatus(FiscalYearStatus yearStatus);

    List<FiscalYear> findByQuarterStatus(FiscalQuarterStatus quarterStatus);

    // Kvartali "manji od" prosleđenog
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

    // Kvartali "veći od" prosleđenog
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
}
