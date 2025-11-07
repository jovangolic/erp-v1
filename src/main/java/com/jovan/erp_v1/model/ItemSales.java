package com.jovan.erp_v1.model;

import java.math.BigDecimal;

import com.jovan.erp_v1.enumeration.ItemSalesStatus;
import com.jovan.erp_v1.exception.ValidationException;

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
@Builder
public class ItemSales {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="goods_id")
	private Goods goods;
	
	@ManyToOne
	@JoinColumn(name="sales_id")
	private Sales sales;
	
	@ManyToOne
	@JoinColumn(name="procurement_id")
	private Procurement procurement;
	
	@ManyToOne
	@JoinColumn(name = "sales_order_id") 
	private SalesOrder salesOrder;
	
	@Column(nullable = false, precision = 15, scale = 3)
	private BigDecimal quantity;
	
	@Column(nullable = false, precision = 15, scale = 3)
	private BigDecimal unitPrice;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private ItemSalesStatus status = ItemSalesStatus.NEW;
	
	@Column(nullable = false)
	@Builder.Default
	private Boolean confirmed = false;
	
	public BigDecimal totalPrice() {
		if(this.quantity == null || this.unitPrice == null) {
			throw new ValidationException("Quantity and unitPrice must not be null");
		}
		if(this.quantity.compareTo(BigDecimal.ZERO) < 0 || this.unitPrice.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Quantity and unitPrice must be positive numbers");
		}
		return this.quantity.multiply(this.unitPrice).max(BigDecimal.ZERO);
	}
}
