package com.jovan.erp_v1.statistics.item_sales;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemSalesQuantityByGoodsStatDTO {

	private Long count;
	private String goodsName;
	private BigDecimal quantity;
	private Long goodsId;
	
	public ItemSalesQuantityByGoodsStatDTO(Long count,String goodsName,BigDecimal quantity, Long goodsId) {
		this.count = count;
		this.goodsName = goodsName;
		this.quantity = quantity;
		this.goodsId = goodsId;
	}
}
