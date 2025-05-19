package com.jovan.erp_v1.model;

import java.time.LocalDate;
import java.util.List;

import com.jovan.erp_v1.enumeration.InventoryStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "storage_employee_id", nullable = false)
	private User storageEmployee;
	
	@ManyToOne
    @JoinColumn(name = "storage_foreman_id", nullable = false)
	private User storageForeman;
	
	@Column(nullable = false)
	private LocalDate date;
	
	@Column(nullable = false)
	private Boolean aligned; //uskladjeno
	
	@OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InventoryItems> inventoryItems;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private InventoryStatus status;
}
