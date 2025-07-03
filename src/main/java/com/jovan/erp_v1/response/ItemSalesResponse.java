package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.model.ItemSales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemSalesResponse {

	private Long id;
	private BigDecimal quantity;
	private BigDecimal unitPrice;

	private GoodsResponse goods;
	private SalesResponse sales;
	private ProcurementResponse procurement;
	private SalesOrderResponse salesOrder;

	public ItemSalesResponse(ItemSales itemSales) {
		this.id = itemSales.getId();
		this.quantity = itemSales.getQuantity();
		this.unitPrice = itemSales.getUnitPrice();

		if (itemSales.getGoods() != null) {
			this.goods = new GoodsResponse(itemSales.getGoods());
		}
		if (itemSales.getSales() != null) {
			this.sales = new SalesResponse(itemSales.getSales());
		}
		if (itemSales.getProcurement() != null) {
			this.procurement = new ProcurementResponse(itemSales.getProcurement());
		}
		if (itemSales.getSalesOrder() != null) {
			this.salesOrder = new SalesOrderResponse(itemSales.getSalesOrder());
		}
	}
}
