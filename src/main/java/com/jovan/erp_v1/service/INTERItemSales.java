package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;

import com.jovan.erp_v1.request.ItemSalesRequest;
import com.jovan.erp_v1.response.ItemSalesResponse;

public interface INTERItemSales {

	ItemSalesResponse create(ItemSalesRequest request);
	ItemSalesResponse update(Long id, ItemSalesRequest request);
	void delete(Long id);
	ItemSalesResponse getById(Long id);
    List<ItemSalesResponse> getAll();
	List<ItemSalesResponse> getByQuantity(Integer quantity);
	List<ItemSalesResponse> getBySalesId(Long salesId);
	List<ItemSalesResponse> getByGoodsId(Long goodsId);
	List<ItemSalesResponse> getByProcurementId(Long procurementId);
	List<ItemSalesResponse> getByUnitPrice(BigDecimal unitPrice);
	List<ItemSalesResponse> getBySalesOrderId(Long salesOrderId);
	List<ItemSalesResponse> getBySalesOrderNumber(String orderNumber);
}
