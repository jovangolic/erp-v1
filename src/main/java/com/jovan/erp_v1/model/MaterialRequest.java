package com.jovan.erp_v1.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.MaterialRequestStatus;

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
public class MaterialRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "work_center_id")
    private WorkCenter requester;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @Column
    private BigDecimal quantity;

    @Column
    private LocalDate requestDate;

    @Column
    private LocalDate neededBy;

    @Enumerated(EnumType.STRING)
    @Column
    private MaterialRequestStatus status;
}
