package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;
import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.ItemSales;


@Repository
public interface ItemSalesRepository extends JpaRepository<ItemSales, Long>, JpaSpecificationExecutor<ItemSales> {

	//goods
	List<ItemSales> findByGoods(Goods goods);
	List<ItemSales> findByGoods_Id(Long goodsId);
	List<ItemSales> findByGoods_NameContainingIgnoreCase(String goodsName);
	List<ItemSales> findByGoods_UnitMeasure(UnitMeasure unitMeasure);
	List<ItemSales> findByGoods_SupplierType(SupplierType supplierType);
	List<ItemSales> findByGoods_StorageType(StorageType storageType);
	List<ItemSales> findByGoods_GoodsType(GoodsType goodsType);
	@Query("SELECT is FROM ItemSales is WHERE is.goods.storage.id = :storageId ")
	List<ItemSales> findByGoods_Storage_Id(@Param("storageId") Long storageId);
	@Query("SELECT is FROM ItemSales is WHERE is.goods.supply.id = :supplyId")
	List<ItemSales> findByGoods_Supply_Id(@Param("supplyId") Long supplyId);
	@Query("SELECT is FROM ItemSales is WHERE is.goods.shelf.id = :shelfId")
	List<ItemSales> findByGoods_Shelf_Id(@Param("shelfId") Long shelfId);
	@Query("SELECT is FROM ItemSales is WHERE is.goods.shelf.rowCount = :rowCount")
	List<ItemSales> findByGoods_Shelf_RowCount(@Param("rowCount") Integer rowCount);
	@Query("SELECT is FROM ItemSales is WHERE is.goods.shelf.cols = :cols")
	List<ItemSales> findByGoods_Shelf_Cols(@Param("cols") Integer cols);
	//sales
	List<ItemSales> findBySales_Id(Long salesId);
	@Query("SELECT is FROM ItemSales is WHERE is.sales.buyer.id = :buyerId")
	List<ItemSales> findBySales_Buyer_Id(@Param("buyerId") Long buyerId);
	List<ItemSales> findBySales_CreatedAt(LocalDateTime createdAt);
	List<ItemSales> findBySales_CreatedAtBetween(LocalDateTime createdAtStart, LocalDateTime createdAtEnd);
	List<ItemSales> findBySales_TotalPrice(BigDecimal totalPrice);
	List<ItemSales> findBySales_TotalPriceGreaterThan(BigDecimal totalPrice);
	List<ItemSales> findBySales_TotalPriceLessThan(BigDecimal totalPrice);
	List<ItemSales> findBySales_SalesDescription(String salesDescription);
	//procurement
	List<ItemSales> findByProcurement_Id(Long procurementId);
	List<ItemSales> findByProcurement_Date(LocalDateTime date);
	List<ItemSales> findByProcurement_DateBetween(LocalDateTime dateStart, LocalDateTime dateEnd);
	List<ItemSales> findByProcurement_TotalCost(BigDecimal totalCost);
	List<ItemSales> findByProcurement_TotalCostGreaterThan(BigDecimal totalCost);
	List<ItemSales> findByProcurement_TotalCostLessThan(BigDecimal totalCost);
	//salesOrder
	List<ItemSales> findBySalesOrder_Id(Long salesOrderId);
	boolean existsBySalesOrder_OrderNumber(String orderNumber);
	List<ItemSales> findBySalesOrder_OrderNumber(String orderNumber);
	List<ItemSales> findBySalesOrder_OrderDate(LocalDateTime orderDate);
	List<ItemSales> findBySalesOrder_OrderDateBetween(LocalDateTime orderDateStart, LocalDateTime orderDateEnd);
	List<ItemSales> findBySalesOrder_TotalAmount(BigDecimal totalAmount);
	List<ItemSales> findBySalesOrder_TotalAmountGreaterThan(BigDecimal totalAmount);
	List<ItemSales> findBySalesOrder_TotalAmountLessThan(BigDecimal totalAmount);
	@Query("SELECT is FROM  ItemSales is WHERE is.salesOrder.buyer.id = :buyerId")
	List<ItemSales> findBySalesOrder_Buyer_Id(@Param("buyerId") Long buyerId);
	@Query("SELECT is FROM ItemSales is WHERE is.salesOrder.status = :status")
	List<ItemSales> findBySalesOrder_OrderStatus(@Param("status") OrderStatus status);
	@Query("SELECT is FROM  ItemSales is WHERE is.salesOrder.invoice.id = :invoiceId")
	List<ItemSales> findBySalesOrder_Invoice_Id(@Param("invoiceId") Long invoiceId);
	//itemSales
	List<ItemSales> findByQuantity(BigDecimal quantity);
	List<ItemSales> findByQuantityLessThan(BigDecimal quantity);
	List<ItemSales> findByQuantityGreaterThan(BigDecimal quantity);
	List<ItemSales> findByUnitPrice(BigDecimal unitPrice);
	List<ItemSales> findByUnitPriceGreaterThan(BigDecimal unitPrice);
	List<ItemSales> findByUnitPriceLessThan(BigDecimal unitPrice);
}
