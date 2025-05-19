package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.Goods;
import com.jovan.erp_v1.model.ItemSales;
import com.jovan.erp_v1.model.Procurement;
import com.jovan.erp_v1.model.Sales;
import com.jovan.erp_v1.model.SalesOrder;

@Repository
public interface ItemSalesRepository extends JpaRepository<ItemSales, Long> {

	List<ItemSales> findByGoods(Goods goods);
	List<ItemSales> findBySales(Sales sales);
	List<ItemSales> findByProcurement(Procurement procurement);
	List<ItemSales> findByQuantity(Integer quantity);
	List<ItemSales> findByUnitPrice(BigDecimal unitPrice);
	List<ItemSales> findBySalesOrder(SalesOrder salesOrder);
}
