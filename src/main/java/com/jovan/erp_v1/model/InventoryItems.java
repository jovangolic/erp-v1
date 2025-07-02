package com.jovan.erp_v1.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventory_items")
public class InventoryItems {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="inventory_id")
	private Inventory inventory;
	
	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;
	
	@Column(nullable = false)
	private BigDecimal quantity; //Količina proizvoda koja je inventurisana
	
	@Column(name = "item_condition", nullable = false)
	private Integer itemCondition;// Stanje proizvoda u skladištu (pre inventure)
	
	@Column(nullable = true)
	private BigDecimal difference; //Razlika između stanja na skladištu i inventurisanog
}
