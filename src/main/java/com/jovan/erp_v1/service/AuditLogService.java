package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.mapper.AuditLogMapper;
import com.jovan.erp_v1.model.AuditLog;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.AuditLogRepository;
import com.jovan.erp_v1.response.AuditLogResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogService implements IAuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    public void log(String action, User user, String details) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setUser(user);
        log.setTimestamp(LocalDateTime.now());
        log.setDetails(details);
        auditLogRepository.save(log);
    }

    @Override
    public List<AuditLogResponse> getAllLogs() {
        return auditLogRepository.findAll().stream()
                .map(AuditLogResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponse> getLogsByUserId(Long userId) {
        return auditLogRepository.findByUser_Id(userId).stream()
                .map(AuditLogResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponse> getLogsByAction(String action) {
        return auditLogRepository.findByAction(action).stream()
                .map(AuditLogResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponse> getLogsBetweenDates(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.findByTimestampBetween(start, end).stream()
                .map(AuditLogResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AuditLogResponse> getById(Long id) {
        return auditLogRepository.findById(id)
                .map(auditLogMapper::toResponse);
    }
}
