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

import com.jovan.erp_v1.model.ProductionOrder;
import com.jovan.erp_v1.enumeration.ProductionOrderStatus;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;

@Repository
public interface ProductionOrderRepository
        extends JpaRepository<ProductionOrder, Long>, JpaSpecificationExecutor<ProductionOrder> {

    Optional<ProductionOrder> findByOrderNumber(String orderNumber);
    List<ProductionOrder> findByProduct_Id(Long productId);
    List<ProductionOrder> findByProduct_NameContainingIgnoreCase(String name);
    List<ProductionOrder> findByProduct_CurrentQuantity(BigDecimal currentQuantity);
    List<ProductionOrder> findByStatus(ProductionOrderStatus status);
    List<ProductionOrder> findByWorkCenter_Id(Long workCenterId);
    List<ProductionOrder> findByWorkCenter_NameContainingIgnoreCase(String name);
    List<ProductionOrder> findByWorkCenter_LocationContainingIgnoreCase(String location);
    List<ProductionOrder> findByWorkCenter_Capacity(Integer capacity);
    List<ProductionOrder> findByWorkCenter_CapacityGreaterThan(Integer capacity);
    List<ProductionOrder> findByWorkCenter_CapacityLessThan(Integer capacity);
    List<ProductionOrder> findByQuantityPlanned(Integer quantityPlanned);
    List<ProductionOrder> findByQuantityProduced(Integer quantityProduced);
    List<ProductionOrder> findByStartDateBetween(LocalDate start, LocalDate end);
    List<ProductionOrder> findByStartDate(LocalDate startDate);
    List<ProductionOrder> findByEndDate(LocalDate endDate);
    List<ProductionOrder> findByStartDateGreaterThanEqual(LocalDate startDate);
    @Query("SELECT p FROM ProductionOrder p WHERE p.startDate >= :startDate")
    List<ProductionOrder> findOrdersWithStartDateAfterOrEqual(@Param("startDate") LocalDate startDate);
    boolean existsByOrderNumber(String orderNumber);
    
    //nove metode
    List<ProductionOrder> findByProduct_CurrentQuantityGreaterThan(BigDecimal currentQuantity);
    List<ProductionOrder> findByProduct_CurrentQuantityLessThan(BigDecimal currentQuantity);
    List<ProductionOrder> findByProduct_UnitMeasure(UnitMeasure unitMeasure);
    List<ProductionOrder> findByProduct_SupplierType(SupplierType supplierType);
    List<ProductionOrder> findByProduct_StorageType(StorageType storageType);
    @Query("SELECT po FROM ProductionOrder po WHERE po.product.storage.id = :storageId")
    List<ProductionOrder> findByProduct_StorageId(@Param("storageId") Long storageId);
    @Query("SELECT po FROM ProductionOrder po WHERE LOWER(po.product.storage.name) LIKE LOWER(CONCAT('%', :storageName, '%'))")
    List<ProductionOrder> findByProduct_StorageNameContainingIgnoreCase(@Param("storageName") String storageName);
    @Query("SELECT po FROM ProductionOrder po WHERE LOWER(po.product.storage.location) LIKE LOWER(CONCAT('%', :storageLocation, '%'))")
    List<ProductionOrder> findByProduct_StorageLocationContainingIgnoreCase(@Param("storageLocation") String storageLocation);
    @Query("SELECT po FROM ProductionOrder po WHERE po.product.storage.capacity = :capacity")
    List<ProductionOrder> findByProduct_StorageCapacity(@Param("capacity") BigDecimal capacity);
    @Query("SELECT po FROM ProductionOrder po WHERE po.product.storage.capacity >= :capacity")
    List<ProductionOrder> findByProduct_StorageCapacityGreaterThan(@Param("capacity") BigDecimal capacity);
    @Query("SELECT po FROM ProductionOrder po WHERE po.product.storage.capacity <= :capacity")
    List<ProductionOrder> findByProduct_StorageCapacityLessThan(@Param("capacity") BigDecimal capacity);
    @Query("SELECT po FROM ProductionOrder po WHERE po.product.supply.id = :supplyId")
    List<ProductionOrder> findByProduct_SupplyId(@Param("supplyId") Long supplyId);
    @Query("SELECT po FROM ProductionOrder po WHERE po.product.supply.quantity = :quantity")
    List<ProductionOrder> findByProduct_SupplyQuantity(@Param("quantity") BigDecimal quantity);
    @Query("SELECT po FROM ProductionOrder po WHERE po.product.supply.quantity = :quantity")
    List<ProductionOrder> findByProduct_SupplyQuantityGreaterThan(@Param("quantity") BigDecimal quantity);
    @Query("SELECT po FROM ProductionOrder po WHERE po.product.supply.quantity = :quantity")
    List<ProductionOrder> findByProduct_SupplyQuantityLessThan(@Param("quantity") BigDecimal quantity);
    @Query("SELECT po FROM ProductionOrder po WHERE po.product.supply.updates = :updates")
    List<ProductionOrder> findByProduct_SupplyUpdates(@Param("updates") LocalDateTime updates);
    @Query("SELECT po FROM ProductionOrder po WHERE po.product.supply.updates BETWEEN :updatesStart AND :updatesEnd")
    List<ProductionOrder> findByProduct_SupplyUpdatesBetween(@Param("updatesStart") LocalDateTime updatesStart,@Param("updatesEnd") LocalDateTime updatesEnd);
    @Query("SELECT po FROM ProductionOrder po WHERE po.product.shelf.id = :shelfId")
    List<ProductionOrder> findByProduct_ShelfId(@Param("shelfId") Long shelfId);
    @Query("SELECT po FROM ProductionOrder po WHERE po.product.shelf.rowCount = :rowCount")
    List<ProductionOrder> findByProduct_ShelfRowCount(@Param("rowCount") Integer rowCount);
    @Query("SELECT po FROM ProductionOrder po WHERE po.product.shelf.cols = :supplyId")
    List<ProductionOrder> findByProduct_ShelfCols(@Param("cols") Integer cols);
    @Query("SELECT po FROM ProductionOrder po WHERE po.product.shelf IS NULL")
    List<ProductionOrder> findByProduct_ShelfIsNull();
};
