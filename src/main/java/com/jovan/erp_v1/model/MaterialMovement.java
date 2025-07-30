package com.jovan.erp_v1.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.MovementType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @Column
    private LocalDate movementDate;

    @Enumerated(EnumType.STRING)
    @Column
    private MovementType type;

    private BigDecimal quantity;

    @ManyToOne
    @JoinColumn(name="from_storage_id")
    private Storage fromStorage;

    @ManyToOne
    @JoinColumn(name="to_storage_id")
    private Storage toStorage;
    
    @PrePersist
    public void prePersist() {
        if (this.movementDate == null) {
            this.movementDate = LocalDate.now();
        }
    }
}
