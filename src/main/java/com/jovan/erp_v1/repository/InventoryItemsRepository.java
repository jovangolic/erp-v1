package com.jovan.erp_v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.InventoryItems;

@Repository
public interface InventoryItemsRepository extends JpaRepository<InventoryItems, Long> {

	List<InventoryItems> findByInventoryId(Long inventoryId);
	
	List<InventoryItems> findByProductId(Long productId);
	
	@Query("SELECT it FROM InventoryItems it WHERE it.product.name = :productName")
	List<InventoryItems> findByProductName(@Param("productName") String productName);
	
	List<InventoryItems> findByDifferenceGreaterThan(Double threshold);
	
	List<InventoryItems> findByQuantity(Double quantity);
	
	List<InventoryItems> findByItemCondition(Integer itemCondition);
	
}
