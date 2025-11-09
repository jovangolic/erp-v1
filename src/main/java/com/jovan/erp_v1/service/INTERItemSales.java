package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.ItemSalesStatus;
import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.request.ItemSalesFilterRequest;
import com.jovan.erp_v1.request.ItemSalesRequest;
import com.jovan.erp_v1.response.ItemSalesResponse;
import com.jovan.erp_v1.save_as.ItemSalesSaveAsRequest;
import com.jovan.erp_v1.search_request.ItemSalesSearchRequest;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesByProcurementRequest;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesBySalesOrderRequest;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesQuantityByGoodsStatDTO;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesQuantityByProcurementStatDTO;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesQuantityBySalesOrderStatDTO;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesQuantityBySalesStatDTO;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesStatsDTO;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesStatsRequest;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesUnitPriceByGoodsStatDTO;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesUnitPriceByProcurementStatDTO;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesUnitPriceBySalesOrderStatDTO;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesUnitPriceBySalesStatDTO;

public interface INTERItemSales {

	ItemSalesResponse create(ItemSalesRequest request);
	ItemSalesResponse update(Long id, ItemSalesRequest request);
	void delete(Long id);
	ItemSalesResponse getById(Long id);
	List<ItemSalesResponse> getAll();
	//goods
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
	
	//nove metode
	
	List<ItemSalesStatsDTO> getStats(ItemSalesStatsRequest req);
	List<ItemSalesQuantityByGoodsStatDTO> countItemSalesQuantityByGoods();
	List<ItemSalesQuantityBySalesStatDTO> countItemSalesQuantityBySales();
	List<ItemSalesQuantityByProcurementStatDTO> countItemSalesQuantityByProcurement(ItemSalesByProcurementRequest request);
	List<ItemSalesQuantityBySalesOrderStatDTO> countItemSalesQuantityBySalesOrder(ItemSalesBySalesOrderRequest request);
	List<ItemSalesUnitPriceByGoodsStatDTO> countItemSalesUnitPriceByGoods();
	List<ItemSalesUnitPriceBySalesStatDTO> countItemSalesUnitPriceBySalesStatDTO();
	List<ItemSalesUnitPriceBySalesOrderStatDTO> countItemSalesUnitPriceBySalesOrderStatDTO(ItemSalesByProcurementRequest request);
	List<ItemSalesUnitPriceByProcurementStatDTO> countItemSalesUnitPriceByProcurementStatDTO(ItemSalesBySalesOrderRequest request);
	ItemSalesResponse trackItemSalesByGoods( Long goodsId);
	ItemSalesResponse trackItemSalesBySales(Long salesId);
	ItemSalesResponse trackItemSalesByProcurement( Long procurementId);
	ItemSalesResponse trackItemSalesBySalesOrder( Long salesOrderId);
	ItemSalesResponse trackItemSales( Long id);
	ItemSalesResponse confirmItemSales(Long id);
	ItemSalesResponse cancelItemSales(Long id);
	ItemSalesResponse closeItemSales(Long id);
	ItemSalesResponse changeStatus(Long id, ItemSalesStatus status);
	ItemSalesResponse saveItemSales(ItemSalesRequest request);
	ItemSalesResponse saveAs(ItemSalesSaveAsRequest request);
	List<ItemSalesResponse> saveAll(List<ItemSalesRequest> request);
	List<ItemSalesResponse> generalSearch(ItemSalesSearchRequest request);
}
