package com.jovan.erp_v1.model;

import java.util.ArrayList;
import java.util.List;

import com.jovan.erp_v1.enumeration.SeverityLevel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *Opis neusaglasenosti ili problema na proizvodu/batch-u.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Defect {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String code;
	
	@Column(nullable = false)
    private String name;
	
	@Column
    private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private SeverityLevel severity;
	
	@OneToMany(mappedBy = "defect", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InspectionDefect> inspections = new ArrayList<>();
}
