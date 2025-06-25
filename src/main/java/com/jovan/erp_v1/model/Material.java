package com.jovan.erp_v1.model;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.UnitOfMeasure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String code;
    @Column
    private String name;
    @Column
    @Enumerated(EnumType.STRING)
    private UnitOfMeasure unit;
    @Column
    private BigDecimal currentStock;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;

    @Column
    private BigDecimal reorderLevel;
}
