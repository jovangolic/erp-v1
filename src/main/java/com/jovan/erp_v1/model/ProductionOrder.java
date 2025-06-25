package com.jovan.erp_v1.model;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.ProductionOrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String orderNumber;

    @ManyToOne
    private Product product;

    @Column
    private int quantityPlanned;

    @Column
    private int quantityProduced;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ProductionOrderStatus status; // PLANNED, IN_PROGRESS, COMPLETED, CANCELED

    @ManyToOne
    private WorkCenter workCenter;
}
