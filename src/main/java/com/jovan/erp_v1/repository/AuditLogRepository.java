package com.jovan.erp_v1.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUser_Id(Long userId);

    List<AuditLog> findByAction(String action);

    List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
