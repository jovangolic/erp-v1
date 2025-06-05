package com.jovan.erp_v1.repository;

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

    List<DeliveryItem> findByQuantity(Double quantity);

    List<DeliveryItem> findByQuantityGreaterThan(Double quantity);

    List<DeliveryItem> findByQuantityLessThan(Double quantity);

    @Query("SELECT d FROM DeliveryItem d WHERE d.product.id = :productId")
    DeliveryItem findByProductId(@Param("productId") Long productId);

    // Pretraga po nazivu proizvoda
    List<DeliveryItem> findByProduct_Name(String name);

    // Po opsegu datuma ulazne isporuke
    List<DeliveryItem> findByInboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end);

    // Po opsegu datuma izlazne isporuke
    List<DeliveryItem> findByOutboundDelivery_DeliveryDateBetween(LocalDate start, LocalDate end);
}
