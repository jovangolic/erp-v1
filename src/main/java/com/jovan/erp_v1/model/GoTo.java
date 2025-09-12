package com.jovan.erp_v1.model;

import com.jovan.erp_v1.enumeration.GoToCategory;
import com.jovan.erp_v1.enumeration.GoToType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "goto_options")
public class GoTo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false,unique = true)
	private String label;
	
	@Column(length = 1000)
	private String description;
	
	@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoToCategory category; // Kategorija opcije

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoToType type; // NAVIGATION ili ACTION

    @Column(nullable = false, unique = true)
    private String path; // Frontend route ili API endpoint

    @Column
    private String icon; // Naziv ikonice, npr. "file-invoice"

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true; // Da li je opcija vidljiva

    @Column
    private String roles;
}
