package com.jovan.erp_v1.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
//@DiscriminatorValue("RAW_MATERIAL")
public class RawMaterial extends Goods {
	
	@Column(nullable = false)
    private Integer currentQuantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
}
