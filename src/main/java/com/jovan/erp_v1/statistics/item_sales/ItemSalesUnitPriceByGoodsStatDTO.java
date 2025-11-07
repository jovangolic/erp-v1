package com.jovan.erp_v1.statistics.item_sales;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemSalesUnitPriceByGoodsStatDTO {

	private Long count;
	private String goodsName;
	private BigDecimal unitPrice;
	private Long goodsId;
	
	public ItemSalesUnitPriceByGoodsStatDTO(Long count,String goodsName,BigDecimal unitPrice,Long goodsId) {
		this.count = count;
		this.goodsName = goodsName;
		this.unitPrice = unitPrice;
		this.goodsId = goodsId;
	}
}
