package com.jovan.erp_v1.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.jovan.erp_v1.model.AuditLog;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.request.AuditLogRequest;
import com.jovan.erp_v1.response.AuditLogResponse;
import com.jovan.erp_v1.util.AbstractMapper;


@Component
public class AuditLogMapper extends AbstractMapper<AuditLogRequest> {

    public AuditLog toEntity(AuditLogRequest request, User user) {
    	Objects.requireNonNull(request, "AuditLogRequest must not be null");
    	Objects.requireNonNull(user, "User must not be null");
        AuditLog log = new AuditLog();
        log.setId(request.userId());
        log.setUser(user);
        log.setAction(request.action());
        log.setDetails(request.details());
        log.setIpAddress(request.ipAddress());
        log.setUserAgent(request.userAgent());
        return log;
    }

    public AuditLogResponse toResponse(AuditLog log) {
    	Objects.requireNonNull(log, "AuditLog must not be null");
    	return new AuditLogResponse(log);
    }

    public List<AuditLogResponse> toResponseList(List<AuditLog> logs) {
    	if(logs == null || logs.isEmpty()) {
    		return Collections.emptyList();
    	}
        return logs.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
