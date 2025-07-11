package com.jovan.erp_v1.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.AuditActionType;
import com.jovan.erp_v1.model.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {

  List<AuditLog> findByUser_Id(Long userId);

  List<AuditLog> findByAction(AuditActionType action);

  List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

  @Query("""
          SELECT a FROM AuditLog a
          WHERE (:userId IS NULL OR a.user.id = :userId)
            AND (:action IS NULL OR a.action = :action)
            AND (:start IS NULL OR a.timestamp >= :start)
            AND (:end IS NULL OR a.timestamp <= :end)
            AND (:ipAddress IS NULL OR a.ipAddress = :ipAddress)
            AND (:userAgent IS NULL OR a.userAgent LIKE %:userAgent%)
      """)
  List<AuditLog> searchLogs(
      @Param("userId") Long userId,
      @Param("action") AuditActionType action,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end,
      @Param("ipAddress") String ipAddress,
      @Param("userAgent") String userAgent);

}
