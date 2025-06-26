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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapacityPlanning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "work_center_id")
    private WorkCenter workCenter;

    @Column
    private LocalDate date;

    @Column(precision = 10, scale = 2)
    private BigDecimal availableCapacity;

    @Column(precision = 10, scale = 2)
    private BigDecimal plannedLoad;

    @Column(precision = 10, scale = 2)
    private BigDecimal remainingCapacity;
}
