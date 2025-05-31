package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.response.AuditLogResponse;

public interface IAuditLogService {

    void log(String action, User user, String details); // koristi se iz backend logike

    List<AuditLogResponse> getAllLogs();

    List<AuditLogResponse> getLogsByUserId(Long userId);

    List<AuditLogResponse> getLogsByAction(String action);

    List<AuditLogResponse> getLogsBetweenDates(LocalDateTime start, LocalDateTime end);

    Optional<AuditLogResponse> getById(Long id);
}
