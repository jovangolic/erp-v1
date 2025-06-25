package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.jovan.erp_v1.enumeration.AuditActionType;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.response.AuditLogResponse;

public interface IAuditLogService {

    void log(AuditActionType action, User user, String details); // koristi se iz backend logike

    List<AuditLogResponse> getAllLogs();

    List<AuditLogResponse> getLogsByUserId(Long userId);

    List<AuditLogResponse> getLogsByAction(AuditActionType action);

    List<AuditLogResponse> getLogsBetweenDates(LocalDateTime start, LocalDateTime end);

    Optional<AuditLogResponse> getById(Long id);

    List<AuditLogResponse> searchLogs(Long userId, AuditActionType action, LocalDateTime start, LocalDateTime end,
            String ipAddress, String userAgent);
}
