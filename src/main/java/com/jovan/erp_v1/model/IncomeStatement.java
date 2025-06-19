package com.jovan.erp_v1.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IncomeStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate periodStart;

    @Column
    private LocalDate periodEnd;

    @Column
    private BigDecimal totalRevenue;

    @Column
    private BigDecimal totalExpenses;

    @Column
    private BigDecimal netProfit;

    @ManyToOne
    @JoinColumn(name = "fiscal_year_id")
    private FiscalYear fiscalYear;
}
