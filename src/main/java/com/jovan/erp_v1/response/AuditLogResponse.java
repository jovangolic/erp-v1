package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

import com.jovan.erp_v1.enumeration.AuditActionType;
import com.jovan.erp_v1.model.AuditLog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {

    private Long id;
    private Long userId;
    private String username;
    private AuditActionType action;
    private LocalDateTime timestamp;
    private String detail;
    private String ipAddress;
    private String userAgent;

    public AuditLogResponse(AuditLog log) {
        this.id = log.getId();
        this.userId = log.getUser().getId();
        this.username = log.getUser().getUsername();
        this.action = log.getAction();
        this.timestamp = log.getTimestamp();
        this.detail = log.getDetails();
        this.ipAddress = log.getIpAddress();
        this.userAgent = log.getUserAgent();
    }
}
