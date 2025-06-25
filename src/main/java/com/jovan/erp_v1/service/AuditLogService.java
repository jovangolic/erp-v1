package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jovan.erp_v1.enumeration.AuditActionType;
import com.jovan.erp_v1.mapper.AuditLogMapper;
import com.jovan.erp_v1.model.AuditLog;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.AuditLogRepository;
import com.jovan.erp_v1.response.AuditLogResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogService implements IAuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    public void log(AuditActionType action, User user, String details) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setUser(user);
        log.setTimestamp(LocalDateTime.now());
        log.setDetails(details);

        // Ako koristiš ServletRequest:
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            log.setIpAddress(request.getRemoteAddr());
            log.setUserAgent(request.getHeader("User-Agent"));
        } catch (Exception e) {
            // fallback ako nije web kontekst
            log.setIpAddress(null);
            log.setUserAgent(null);
        }

        auditLogRepository.save(log);
    }
    /*
     * public void log(AuditActionType action, User user, String details, String
     * ipAddress, String userAgent) {
     * AuditLog log = new AuditLog();
     * log.setAction(action);
     * log.setUser(user);
     * log.setTimestamp(LocalDateTime.now());
     * log.setDetails(details);
     * log.setIpAddress(ipAddress);
     * log.setUserAgent(userAgent);
     * auditLogRepository.save(log);
     * }
     */

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
    public List<AuditLogResponse> getLogsByAction(AuditActionType action) {
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

    @Override
    public List<AuditLogResponse> searchLogs(Long userId, AuditActionType action, LocalDateTime start,
            LocalDateTime end, String ipAddress, String userAgent) {
        Specification<AuditLog> spec = Specification.where(null);
        if (userId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), userId));
        }
        if (action != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("action"), action));
        }
        if (start != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("timestamp"), start));
        }
        if (end != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("timestamp"), end));
        }
        if (ipAddress != null && !ipAddress.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("ipAddress"), ipAddress));
        }
        if (userAgent != null && !userAgent.isBlank()) {
            spec = spec.and(
                    (root, query, cb) -> cb.like(cb.lower(root.get("userAgent")), "%" + userAgent.toLowerCase() + "%"));
        }
        List<AuditLog> logs = auditLogRepository.findAll(spec);
        return logs.stream()
                .map(AuditLogResponse::new)
                .collect(Collectors.toList());
    }

}
