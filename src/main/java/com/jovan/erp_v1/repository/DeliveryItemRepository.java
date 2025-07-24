package com.jovan.erp_v1.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.DeliveryItem;

@Repository
public interface DeliveryItemRepository extends JpaRepository<DeliveryItem, Long> {

    List<DeliveryItem> findByInboundDeliveryId(Long inboundId);
    List<DeliveryItem> findByOutboundDeliveryId(Long outboundId);
    List<DeliveryItem> findByQuantity(BigDecimal quantity);
    List<DeliveryItem> findByQuantityGreaterThan(BigDecimal quantity);
    List<DeliveryItem> findByQuantityLessThan(BigDecimal quantity);
    @Query("SELECT d FROM DeliveryItem d WHERE d.product.id = :productId")
    DeliveryItem findByProductId(@Param("productId") Long productId);
    List<DeliveryItem> findByProduct_Name(String name);
    List<DeliveryItem> findByInboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end);
    List<DeliveryItem> findByOutboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end);
}
