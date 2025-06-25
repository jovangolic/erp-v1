package com.jovan.erp_v1.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.model.AuditLog;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.request.AuditLogRequest;
import com.jovan.erp_v1.response.AuditLogResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuditLogMapper {

    private final UserRepository userRepository;

    public AuditLog toEntity(AuditLogRequest request) {
        AuditLog log = new AuditLog();
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id:" + request.userId()));
        log.setUser(user);
        log.setAction(request.action());
        log.setDetails(request.details());
        log.setIpAddress(request.ipAddress());
        log.setUserAgent(request.userAgent());
        return log;
    }

    public AuditLogResponse toResponse(AuditLog log) {
        AuditLogResponse response = new AuditLogResponse();
        if (log.getId() != null) {
            response.setId(log.getId());
        }
        if (log.getUser() != null) {
            response.setUserId(log.getUser().getId());
            response.setUsername(log.getUser().getUsername());
        }
        response.setAction(log.getAction());
        response.setTimestamp(log.getTimestamp());
        response.setDetail(log.getDetails());
        response.setIpAddress(log.getIpAddress());
        response.setUserAgent(log.getUserAgent());
        return response;
    }

    public List<AuditLogResponse> toResponseList(List<AuditLog> logs) {
        return logs.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
