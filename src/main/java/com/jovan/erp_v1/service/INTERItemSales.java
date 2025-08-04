package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.ItemSalesFilterRequest;
import com.jovan.erp_v1.request.ItemSalesRequest;
import com.jovan.erp_v1.response.ItemSalesResponse;

public interface INTERItemSales {

	ItemSalesResponse create(ItemSalesRequest request);

	ItemSalesResponse update(Long id, ItemSalesRequest request);

	void delete(Long id);

	ItemSalesResponse getById(Long id);

	List<ItemSalesResponse> getAll();
	// dodao, goods

	List<ItemSalesResponse> findByGoods_Id(Long goodsId);

	List<ItemSalesResponse> findByGoods_NameContainingIgnoreCase(String goodsName);

	List<ItemSalesResponse> findByGoods_UnitMeasure(UnitMeasure unitMeasure);

	List<ItemSalesResponse> findByGoods_SupplierType(SupplierType supplierType);

	List<ItemSalesResponse> findByGoods_StorageType(StorageType storageType);

	List<ItemSalesResponse> findByGoods_GoodsType(GoodsType goodsType);

	List<ItemSalesResponse> findByGoods_Storage_Id(Long storageId);

	List<ItemSalesResponse> findByGoods_Supply_Id(Long supplyId);

	List<ItemSalesResponse> findByGoods_Shelf_Id(Long shelfId);

	List<ItemSalesResponse> findByGoods_Shelf_RowCount(Integer rowCount);

	List<ItemSalesResponse> findByGoods_Shelf_Cols(Integer cols);

	// sales
	List<ItemSalesResponse> findBySales_Id(Long salesId);

	List<ItemSalesResponse> findBySales_Buyer_Id(Long buyerId);

	List<ItemSalesResponse> findBySales_CreatedAt(LocalDateTime createdAt);

	List<ItemSalesResponse> findBySales_CreatedAtBetween(LocalDateTime createdAtStart, LocalDateTime createdAtEnd);

	List<ItemSalesResponse> findBySales_TotalPrice(BigDecimal totalPrice);

	List<ItemSalesResponse> findBySales_TotalPriceGreaterThan(BigDecimal totalPrice);

	List<ItemSalesResponse> findBySales_TotalPriceLessThan(BigDecimal totalPrice);

	List<ItemSalesResponse> findBySales_SalesDescription(String salesDescription);

	// procurement
	List<ItemSalesResponse> findByProcurement_Id(Long procurementId);

	List<ItemSalesResponse> findByProcurement_Date(LocalDateTime date);

	List<ItemSalesResponse> findByProcurement_DateBetween(LocalDateTime dateStart, LocalDateTime dateEnd);

	List<ItemSalesResponse> findByProcurement_TotalCost(BigDecimal totalCost);

	List<ItemSalesResponse> findByProcurement_TotalCostGreaterThan(BigDecimal totalCost);

	List<ItemSalesResponse> findByProcurement_TotalCostLessThan(BigDecimal totalCost);

	// salesOrder
	List<ItemSalesResponse> findBySalesOrder_Id(Long salesOrderId);

	List<ItemSalesResponse> findBySalesOrder_OrderNumber(String orderNumber);

	List<ItemSalesResponse> findBySalesOrder_OrderDate(LocalDateTime orderDate);

	List<ItemSalesResponse> findBySalesOrder_OrderDateBetween(LocalDateTime orderDateStart, LocalDateTime orderDateEnd);

	List<ItemSalesResponse> findBySalesOrder_TotalAmount(BigDecimal totalAmount);

	List<ItemSalesResponse> findBySalesOrder_TotalAmountGreaterThan(BigDecimal totalAmount);

	List<ItemSalesResponse> findBySalesOrder_TotalAmountLessThan(BigDecimal totalAmount);

	List<ItemSalesResponse> findBySalesOrder_Buyer_Id(Long buyerId);

	List<ItemSalesResponse> findBySalesOrder_OrderStatus(OrderStatus status);

	List<ItemSalesResponse> findBySalesOrder_Invoice_Id(Long invoiceId);

	// itemSales
	List<ItemSalesResponse> findByQuantity(BigDecimal quantity);

	List<ItemSalesResponse> findByQuantityLessThan(BigDecimal quantity);

	List<ItemSalesResponse> findByQuantityGreaterThan(BigDecimal quantity);

	List<ItemSalesResponse> findByUnitPrice(BigDecimal unitPrice);

	List<ItemSalesResponse> findByUnitPriceGreaterThan(BigDecimal unitPrice);

	List<ItemSalesResponse> findByUnitPriceLessThan(BigDecimal unitPrice);

	// filter
	List<ItemSalesResponse> filter(ItemSalesFilterRequest filterRequest);
}
