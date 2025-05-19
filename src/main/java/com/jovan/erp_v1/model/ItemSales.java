package com.jovan.erp_v1.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
	
	@Column
	private Integer quantity;
	
	@Column(nullable = false)
	private BigDecimal unitPrice;
}
