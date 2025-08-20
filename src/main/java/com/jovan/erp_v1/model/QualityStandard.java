package com.jovan.erp_v1.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.jovan.erp_v1.enumeration.Unit;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *Odredjuje kriterijume po kojima se proizvod ocenjuje. 
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityStandard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="product_id",nullable = false)
	private Product product;
	
	@Column(nullable = false)
	private String description;
	
	@Column(precision = 10, scale = 2)
    private BigDecimal minValue;
	
	@Column(precision = 10, scale = 2)
    private BigDecimal maxValue;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
    private Unit unit;
	
	@OneToMany(mappedBy = "standard", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TestMeasurement> measurements = new ArrayList<>();
}
