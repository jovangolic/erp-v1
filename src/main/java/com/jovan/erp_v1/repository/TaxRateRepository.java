package com.jovan.erp_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.TaxRate;
import com.jovan.erp_v1.enumeration.TaxType;
import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {

    List<TaxRate> findByType(TaxType type);
    List<TaxRate> findByTaxName(String taxName);
    List<TaxRate> findByPercentage(BigDecimal percentage);
    List<TaxRate> findByTaxNameAndPercentage(String taxName, BigDecimal percentage);
    List<TaxRate> findByStartDateBeforeAndEndDateAfter(LocalDate date1, LocalDate date2);
    List<TaxRate> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate date1, LocalDate date2);
    @Query("SELECT t FROM TaxRate t WHERE t.startDate <= :end AND t.endDate >= :start")
    List<TaxRate> findOverlapping(@Param("start") LocalDate start, @Param("end") LocalDate end);
    List<TaxRate> findByStartDate(LocalDate startDate);
    List<TaxRate> findByEndDate(LocalDate endDate);
    @Query("SELECT t FROM TaxRate t WHERE t.type = :type AND :date BETWEEN t.startDate AND t.endDate")
    List<TaxRate> findActiveByType(@Param("type") TaxType type, @Param("date") LocalDate date);

    @Query("""
                SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END
                FROM TaxRate t
                WHERE t.taxName = :taxName AND t.type = :type
                AND (
                    (:startDate BETWEEN t.startDate AND t.endDate)
                    OR (:endDate BETWEEN t.startDate AND t.endDate)
                    OR (t.startDate BETWEEN :startDate AND :endDate)
                )
            """)
    boolean existsByTaxNameAndTypeAndDateRangeOverlap(
            @Param("taxName") String taxName,
            @Param("type") TaxType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
            SELECT t FROM TaxRate t
            WHERE t.type = :type
            AND t.startDate <= :endDate
            AND t.endDate >= :startDate
            """)
    List<TaxRate> findByTypeAndPeriod(@Param("type") TaxType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
                SELECT t FROM TaxRate t
                WHERE t.type = :type
                AND t.startDate <= :endDate
                AND t.endDate >= :startDate
            """)
    List<TaxRate> findByTypeAndDateRange(@Param("type") TaxType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<TaxRate> findByTypeAndStartDateLessThanEqualAndEndDateGreaterThanEqual(TaxType type, LocalDate date1,
            LocalDate date2);
    
    //nove metode
    @Query("SELECT t FROM TaxRate t WHERE t.type =  'VAT'")
    List<TaxRate> findByVat();
    @Query("SELECT t FROM TaxRate t WHERE t.type =  'INCOME_TAX'")
    List<TaxRate> findByIncome_Tax();
    @Query("SELECT t FROM TaxRate t WHERE t.type =  'SALES_TAX'")
    List<TaxRate> findBySales_Tax();
    @Query("SELECT t FROM TaxRate t WHERE t.type =  'CUSTOM'")
    List<TaxRate> findByCustom();
}
