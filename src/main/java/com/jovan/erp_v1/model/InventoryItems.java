package com.jovan.erp_v1.model;

import java.math.BigDecimal;

import com.jovan.erp_v1.exception.ValidationException;

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
	private BigDecimal quantity; //Kolicina proizvoda koja je inventurisana
	
	@Column(name = "item_condition", nullable = false)
	private BigDecimal itemCondition;// Stanje proizvoda u skladistu (pre inventure)
	
	@Column(nullable = true)
	private BigDecimal difference; //Razlika između stanja na skladistu i inventurisanog
	
	public BigDecimal calculateDifference() {
		if(this.quantity == null || this.itemCondition == null) {
			throw new ValidationException("Quantity and itemCondition must not be null");
		}
		if(this.itemCondition.compareTo(this.quantity) > 0) {
			throw new ValidationException("ItemCondition must not be greater than quantity");
		}
		return this.quantity.subtract(this.itemCondition).max(BigDecimal.ZERO);
	}
	
	//ako se dozvoljava manjak
	public BigDecimal calculateDifferenceWithShortage() {
		if(this.quantity == null || this.itemCondition == null) {
			throw new ValidationException("Quantity and itemCondition must not be null");
		}
		return this.quantity.subtract(this.itemCondition).max(BigDecimal.ZERO);
	}
}
