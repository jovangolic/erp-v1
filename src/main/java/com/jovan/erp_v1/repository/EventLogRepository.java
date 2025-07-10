package com.jovan.erp_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.jovan.erp_v1.model.EventLog;

@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Long> {
    List<EventLog> findByShipmentId(Long shipmentId);
    List<EventLog> findByTimestampAfter(LocalDateTime date);
    List<EventLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<EventLog> findByDescriptionContaining(String text);
    List<EventLog> findByShipmentIdAndTimestampBetween(Long shipmentId, LocalDateTime from, LocalDateTime to);
    Optional<EventLog> findTopByShipmentIdOrderByTimestampDesc(Long shipmentId);
}