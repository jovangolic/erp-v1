package com.jovan.erp_v1.model;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.ProductionOrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
public class ProductionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column
    private Integer quantityPlanned;

    @Column
    private Integer quantityProduced;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ProductionOrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_center_id")
    private WorkCenter workCenter;
}
