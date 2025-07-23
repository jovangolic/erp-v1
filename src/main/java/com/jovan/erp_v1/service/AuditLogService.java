package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jovan.erp_v1.enumeration.AuditActionType;
import com.jovan.erp_v1.exception.NoDataFoundException;
import com.jovan.erp_v1.exception.ValidationException;
import com.jovan.erp_v1.mapper.AuditLogMapper;
import com.jovan.erp_v1.model.AuditLog;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.AuditLogRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.response.AuditLogResponse;
import com.jovan.erp_v1.util.DateValidator;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogService implements IAuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;
    private final UserRepository userRepository;

    @Override
    public void log(AuditActionType action, User user, String details) {
    	fetchUserId(user.getId());
    	validateAuditActionType(action);
    	validateString(details);
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setUser(user);
        log.setTimestamp(LocalDateTime.now());
        log.setDetails(details);
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
    	List<AuditLog> items = auditLogRepository.findAll();
    	if(items.isEmpty()) {
    		throw new NoDataFoundException("No items found for audit-logs");
    	}
        return items.stream()
                .map(AuditLogResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponse> getLogsByUserId(Long userId) {
    	fetchUserId(userId);
    	List<AuditLog> items = auditLogRepository.findByUser_Id(userId);
    	if(items.isEmpty()) {
    		String msg = String.format("No audit logs with userId %d is found", userId);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(AuditLogResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponse> getLogsByAction(AuditActionType action) {
    	validateAuditActionType(action);
    	List<AuditLog> items = auditLogRepository.findByAction(action);
    	if(items.isEmpty()) {
    		String msg = String.format("No audti log with type action %s is found", action);
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
                .map(AuditLogResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponse> getLogsBetweenDates(LocalDateTime start, LocalDateTime end) {
    	DateValidator.validateRange(start, end);
    	List<AuditLog> items = auditLogRepository.findByTimestampBetween(start, end);
    	if(items.isEmpty()) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    		String msg = String.format("No audit-log with dates between %s and %s is found", 
    				start.format(formatter),end.format(formatter));
    		throw new NoDataFoundException(msg);
    	}
        return items.stream()
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
    
    private void validateAuditActionType(AuditActionType action) {
    	Optional.ofNullable(action)
    		.orElseThrow(() -> new ValidationException("AuditActionType action must not be null"));
    }
    
    private User fetchUserId(Long userId) {
    	if(userId == null) {
    		throw new ValidationException("User ID must not be null");
    	}
    	return userRepository.findById(userId).orElseThrow(() -> new ValidationException("User not found with id "+userId));
    }

    private void validateString(String str) {
    	if(str == null || str.trim().isEmpty()) {
    		throw new ValidationException("String must not be null nor empty");
    	}
    }
}
