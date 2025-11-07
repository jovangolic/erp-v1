package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
import com.jovan.erp_v1.statistics.item_sales.ItemSalesQuantityByGoodsStatDTO;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesQuantityByProcurementStatDTO;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesQuantityBySalesOrderStatDTO;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesQuantityBySalesStatDTO;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesStatsDTO;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesUnitPriceByGoodsStatDTO;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesUnitPriceByProcurementStatDTO;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesUnitPriceBySalesOrderStatDTO;
import com.jovan.erp_v1.statistics.item_sales.ItemSalesUnitPriceBySalesStatDTO;

@Repository
public interface ItemSalesRepository extends JpaRepository<ItemSales, Long>, JpaSpecificationExecutor<ItemSales> {

	// goods
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

	// sales
	List<ItemSales> findBySales_Id(Long salesId);

	@Query("SELECT is FROM ItemSales is WHERE is.sales.buyer.id = :buyerId")
	List<ItemSales> findBySales_Buyer_Id(@Param("buyerId") Long buyerId);

	List<ItemSales> findBySales_CreatedAt(LocalDateTime createdAt);

	List<ItemSales> findBySales_CreatedAtBetween(LocalDateTime createdAtStart, LocalDateTime createdAtEnd);

	List<ItemSales> findBySales_TotalPrice(BigDecimal totalPrice);

	List<ItemSales> findBySales_TotalPriceGreaterThan(BigDecimal totalPrice);

	List<ItemSales> findBySales_TotalPriceLessThan(BigDecimal totalPrice);

	List<ItemSales> findBySales_SalesDescription(String salesDescription);

	// procurement
	List<ItemSales> findByProcurement_Id(Long procurementId);

	List<ItemSales> findByProcurement_Date(LocalDateTime date);

	List<ItemSales> findByProcurement_DateBetween(LocalDateTime dateStart, LocalDateTime dateEnd);

	List<ItemSales> findByProcurement_TotalCost(BigDecimal totalCost);

	List<ItemSales> findByProcurement_TotalCostGreaterThan(BigDecimal totalCost);

	List<ItemSales> findByProcurement_TotalCostLessThan(BigDecimal totalCost);

	// salesOrder
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

	// itemSales
	List<ItemSales> findByQuantity(BigDecimal quantity);

	List<ItemSales> findByQuantityLessThan(BigDecimal quantity);

	List<ItemSales> findByQuantityGreaterThan(BigDecimal quantity);

	List<ItemSales> findByUnitPrice(BigDecimal unitPrice);

	List<ItemSales> findByUnitPriceGreaterThan(BigDecimal unitPrice);

	List<ItemSales> findByUnitPriceLessThan(BigDecimal unitPrice);

	// nove metode
	@Query("SELECT i FROM ItemSales i WHERE i.goods.id = :goodsId")
	List<ItemSales> trackItemSalesByGoods(@Param("goodsId") Long goodsId);

	@Query("SELECT i FROM ItemSales i WHERE i.sales.id = :salesId")
	List<ItemSales> trackItemSalesBySales(@Param("salesId") Long salesId);

	@Query("SELECT i FROM ItemSales i WHERE i.procurement.id = :procurementId")
	List<ItemSales> trackItemSalesByProcurement(@Param("procurementId") Long procurementId);

	@Query("SELECT i FROM ItemSales i WHERE i.salesOrder.id = :salesOrderId")
	List<ItemSales> trackItemSalesBySalesOrder(@Param("salesOrderId") Long salesOrderId);

	@Query("SELECT i FROM ItemSales i WHERE i.id = :id")
	Optional<ItemSales> trackItemSales(@Param("id") Long id);

	@Query("""
			SELECT new com.jovan.erp_v1.statistics.item_sales.ItemSalesQuantityByGoodsStatDTO(
			COUNT(i),
			MAX(i.goods.name),
			SUM(i.quantity),
			i.goods.id)
			FROM ItemSales i
			WHERE i.confired = true
			GROUP BY i.goods.id
			""")
	List<ItemSalesQuantityByGoodsStatDTO> countItemSalesQuantityByGoods();

	@Query("""
			SELECT new com.jovan.erp_v1.statistics.item_sales.ItemSalesQuantityBySalesStatDTO(
			COUNT(i),
			SUM(i.quantity),
			i.sales,id)
			FROM ItemSales i
			WHERE i.confirmed = true
			GROUP BY i.sales.id
			""")
	List<ItemSalesQuantityBySalesStatDTO> countItemSalesQuantityBySales();

	@Query("""
			SELECT new com.jovan.erp_v1.statistics.item_sales.ItemSalesQuantityByProcurementStatDTO(
			COUNT(i),
			SUM(i.quantity),
			i.procurement.id)
			FROM ItemSales i
			WHERE i.confirmed = true
				AND (:procurementId IS NULL OR i.procurement.id = :procurementId)
			    AND (:fromDate IS NULL OR i.procurement.locdate >= :fromDate)
			    AND (:toDate IS NULL OR i.procurement.locdate <= :toDate)
			GROUP BY i.procurement.id
			""")
	List<ItemSalesQuantityByProcurementStatDTO> countItemSalesQuantityByProcurement(
			@Param("procurementId") Long procurementId,
			@Param("fromDate") LocalDate fromDate,
			@Param("toDate") LocalDate toDate);

	@Query("""
			SELECT new com.jovan.erp_v1.statistics.item_sales.ItemSalesQuantityBySalesOrderStatDTO(
			COUNT(i),
			i.salesOrder.orderNumber,
			SUM(i.quantity),
			i.salesOrder.id)
			FROM ItemSales i
			WHERE i.confirmed = true
				AND (:salesOrderId IS NULL OR i.salesOrder.id = :salesOrderId)
			    AND (:fromDate IS NULL OR i.salesOrder.orderDate >= :fromDate)
			    AND (:toDate IS NULL OR i.salesOrder.orderDate <= :toDate)
			GROUP BY i.salesOrder.id
			""")
	List<ItemSalesQuantityBySalesOrderStatDTO> countItemSalesQuantityBySalesOrder(
			@Param("salesOrderId") Long salesOrderId,
			@Param("fromDate") LocalDate fromDate,
			@Param("toDate") LocalDate toDate);

	@Query("""
			SELECT new com.jovan.erp_v1.statistics.item_sales.ItemSalesUnitPriceByGoodsStatDTO(
			COUNT(i),
			MAX(i.goods.name),
			SUM(i.unitPrice),
			i.goods.id
			)
			FROM ItemSales i
			WHERE i.confirmed = true
			GROUP BY i.goods.id
			""")
	List<ItemSalesUnitPriceByGoodsStatDTO> countItemSalesUnitPriceByGoods();

	@Query("""
			SELECT new com.jovan.erp_v1.statistics.item_sales.ItemSalesUnitPriceBySalesStatDTO(
			COUNT(i),
			SUM(i.unitPrice),
			i.sales.id)
			FROM ItemSales i
			WHERE i.confirmed = true
			GROUP BY i.sales.id
			""")
	List<ItemSalesUnitPriceBySalesStatDTO> countItemSalesUnitPriceBySalesStatDTO();

	@Query("""
			SELECT new com.jovan.erp_v1.statistics.item_sales.ItemSalesUnitPriceBySalesOrderStatDTO(
			COUNT(i),
			i.salesOrder.orderNumber,
			SUM(i.unitPrice),
			i.salesOrder.id)
			FROM ItemSales i
			WHERE i.confirmed = true
				AND (:salesOrderId IS NULL OR i.salesOrder.id = :salesOrderId)
			    AND (:fromDate IS NULL OR i.salesOrder.orderDate >= :fromDate)
			    AND (:toDate IS NULL OR i.salesOrder.orderDate <= :toDate)
			GROUP BY i.salesOrder.id
			""")
	List<ItemSalesUnitPriceBySalesOrderStatDTO> countItemSalesUnitPriceBySalesOrderStatDTO(
			@Param("salesOrderId") Long salesOrderId,
			@Param("fromDate") LocalDate fromDate,
			@Param("toDate") LocalDate toDate);

	@Query("""
			SELECT new com.jovan.erp_v1.statistics.item_sales.ItemSalesUnitPriceByProcurementStatDTO(
			COUNT(i),
			SUM(i.unitPrice),
			i.procurement.id)
			FROM ItemSales i
			WHERE i.confirmed = true
				AND (:procurementId IS NULL OR i.procurement.id = :procurementId)
			    AND (:fromDate IS NULL OR i.procurement.locdate >= :fromDate)
			    AND (:toDate IS NULL OR i.procurement.locdate <= :toDate)
			GROUP BY i.procurement.id
			""")
	List<ItemSalesUnitPriceByProcurementStatDTO> countItemSalesUnitPriceByProcurementStatDTO(
			@Param("procurementId") Long procurementId,
			@Param("fromDate") LocalDate fromDate,
			@Param("toDate") LocalDate toDate);

	@Query("""
			SELECT new com.jovan.erp_v1.statistics.item_sales.ItemSalesStatsDTO(
			i.goods.id,
			i.sales.id,
			         i.procurement.id,
			         i.salesOrder.id,
			         SUM(i.quantity),
			         SUM(i.quantity * i.unitPrice))
			         FROM ItemSales i
			         WHERE i.confirmed = true
			    AND (:goodsId IS NULL OR i.goods.id = :goodsId)
			          AND (:salesId IS NULL OR i.sales.id = :salesId)
			          AND (:procurementId IS NULL OR i.procurement.id = :procurementId)
			          AND (:salesOrderId IS NULL OR i.salesOrder.id = :salesOrderId)
			          AND (:fromDate IS NULL OR i.salesOrder.orderDate >= :fromDate)
			          AND (:toDate IS NULL OR i.salesOrder.orderDate <= :toDate)
			      GROUP BY i.goods.id, i.sales.id, i.procurement.id, i.salesOrder.id
			""")
	List<ItemSalesStatsDTO> findItemSalesStatsSQL(
			@Param("goodsId") Long goodsId,
			@Param("salesId") Long salesId,
			@Param("procurementId") Long procurementId,
			@Param("salesOrderId") Long salesOrderId,
			@Param("fromDate") LocalDateTime fromDate,
			@Param("toDate") LocalDateTime toDate);
}
