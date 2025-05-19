package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.model.ItemSales;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemSalesResponse {

	private Long id;
	private String goodsName;
	private Integer quantity;
	private BigDecimal unitPrice;
	private BigDecimal totalSalePrice;         // iz Sales
	private BigDecimal totalProcurementCost;   // iz Procurement
	private Long salesOrderId;
	private String orderNumber;

	public ItemSalesResponse(ItemSales itemSales) {
		this.id = itemSales.getId();
		this.goodsName = itemSales.getGoods().getName();
		this.quantity = itemSales.getQuantity();
		this.unitPrice = itemSales.getUnitPrice();

		if (itemSales.getSales() != null) {
			this.totalSalePrice = itemSales.getSales().getTotalPrice();
		}
		if (itemSales.getProcurement() != null) {
			this.totalProcurementCost = itemSales.getProcurement().getTotalCost();
		}
		if (itemSales.getSalesOrder() != null) {
			this.salesOrderId = itemSales.getSalesOrder().getId();
			this.orderNumber = itemSales.getSalesOrder().getOrderNumber();
		}
	}
}
