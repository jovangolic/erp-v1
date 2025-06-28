package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.ProductionOrder;
import com.jovan.erp_v1.enumeration.ProductionOrderStatus;

@Repository
public interface ProductionOrderRepository
        extends JpaRepository<ProductionOrder, Long>, JpaSpecificationExecutor<ProductionOrder> {

    Optional<ProductionOrder> findByOrderNumber(String orderNumber);

    List<ProductionOrder> findByProduct_Id(Long productId);

    List<ProductionOrder> findByProduct_NameContainingIgnoreCase(String name);

    List<ProductionOrder> findByProduct_CurrentQuantity(Double currentQuantity);

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
};