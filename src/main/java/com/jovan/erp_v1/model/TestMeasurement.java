package com.jovan.erp_v1.model;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *Za inspekcije koje ukljucuju fizicka/hemijska merenja.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TestMeasurement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="inspection_id",nullable = false)
	private Inspection inspection;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="qualitiy_standard_id",nullable = false)
    private QualityStandard standard;
	
	@Column(precision = 10,scale = 2)
	private BigDecimal measuredValue;
	
	@Column
    private Boolean withinSpec;
	
	@Column(nullable = false)
	@Builder.Default
	private Boolean confirmed = false;
}
