package com.jovan.erp_v1.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.DeliveryStatus;
import com.jovan.erp_v1.model.InboundDelivery;

@Repository
public interface InboundDeliveryRepository extends JpaRepository<InboundDelivery, Long> {

    @EntityGraph(attributePaths = { "supply", "items", "items.product" })
    Optional<InboundDelivery> findById(Long id);

    @EntityGraph(attributePaths = { "supply", "items", "items.product" })
    List<InboundDelivery> findAll();

    @EntityGraph(attributePaths = { "supply", "items", "items.product" })
    List<InboundDelivery> findByStatus(DeliveryStatus status);

    @EntityGraph(attributePaths = { "supply", "items", "items.product" })
    List<InboundDelivery> findBySupplyId(Long supplyId);

    @EntityGraph(attributePaths = { "supply", "items", "items.product" })
    List<InboundDelivery> findByDeliveryDateBetween(LocalDate from, LocalDate to);

}
