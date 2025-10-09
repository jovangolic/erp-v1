package com.jovan.erp_v1.model;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.BillOfMaterialsStatus;

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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
/** BOM - BillOfMaterials */
@Builder
public class BillOfMaterials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_product_id")
    private Product parentProduct;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private Product component;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean confirmed = false;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private BillOfMaterialsStatus status = BillOfMaterialsStatus.NEW;
}
