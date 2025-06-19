package com.jovan.erp_v1.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.jovan.erp_v1.enumeration.FiscalQuarterStatus;
import com.jovan.erp_v1.enumeration.FiscalYearStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiscalYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int year;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column
    @Enumerated(EnumType.STRING)
    private FiscalYearStatus yearStatus;

    @Column
    @Enumerated(EnumType.STRING)
    private FiscalQuarterStatus quarterStatus;

    @OneToMany(mappedBy = "fiscalYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FiscalQuarter> quarters = new ArrayList<>();
}
