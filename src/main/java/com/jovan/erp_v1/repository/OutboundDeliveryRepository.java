package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.model.OutboundDelivery;

@Repository
public interface OutboundDeliveryRepository extends JpaRepository<OutboundDelivery, Long> {

    @EntityGraph(attributePaths = { "supply", "items", "items.product" })
    Optional<OutboundDelivery> findById(Long id);

    @EntityGraph(attributePaths = { "supply", "items", "items.product" })
    List<OutboundDelivery> findAll();

    @EntityGraph(attributePaths = { "supply", "items", "items.product" })
    List<OutboundDelivery> findByStatus(DeliveryStatus status);

    @EntityGraph(attributePaths = { "buyer", "items", "items.product" })
    List<OutboundDelivery> findByBuyerId(Long buyerId);

    @EntityGraph(attributePaths = { "supply", "items", "items.product" })
    List<OutboundDelivery> findByDeliveryDateBetween(LocalDate from, LocalDate to);
}
