package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.FiscalQuarter;
import com.jovan.erp_v1.model.FiscalYear;
import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;

@Repository
public interface FiscalQuarterRepository extends JpaRepository<FiscalQuarter, Long> {

    // Pronađi sve kvartale koji pripadaju određenoj fiskalnoj godini
    List<FiscalQuarter> findByFiscalYear(FiscalYear fiscalYear);

    // Pronađi kvartale određenog statusa (Q1, Q2, itd.)
    List<FiscalQuarter> findByQuarterStatus(FiscalQuarterStatus status);

    // Pronađi sve kvartale između dva datuma (npr. za neki opseg)
    List<FiscalQuarter> findByStartDateBetween(LocalDate start, LocalDate end);

    // Pronađi kvartale određenog statusa unutar fiskalne godine
    List<FiscalQuarter> findByFiscalYearAndQuarterStatus(FiscalYear fiscalYear, FiscalQuarterStatus status);

    // Pronađi kvartale koji počinju posle datuma (greater than)
    List<FiscalQuarter> findByStartDateAfter(LocalDate date);

    // Pronađi kvartale koji počinju pre datuma (less than)
    List<FiscalQuarter> findByStartDateBefore(LocalDate date);

    // Opcionalno: po godini (ako želiš da filtriraš po int vrednosti iz
    // `FiscalYear`)
    List<FiscalQuarter> findByFiscalYear_Year(int year);
}
