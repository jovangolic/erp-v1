package com.jovan.erp_v1.response;

import java.math.BigDecimal;

import com.jovan.erp_v1.model.ItemSales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemSalesForSalesOrderResponse {

	private Long id;
	private BigDecimal quantity;
	private BigDecimal unitPrice;
	private GoodsResponse goods;
	private SalesResponse sales;
	private ProcurementResponse procurement;
		
	public ItemSalesForSalesOrderResponse(ItemSales is) {
		this.id = is.getId();
		this.quantity = is.getQuantity();
		this.unitPrice = is.getUnitPrice();
		this.goods = is.getGoods() != null ? new GoodsResponse(is.getGoods()) : null;
		this.sales = is.getSales() != null ? new SalesResponse(is.getSales()) : null;
		this.procurement = is.getProcurement() != null ? new ProcurementResponse(is.getProcurement()) : null;
	}
}
