package com.jovan.erp_v1.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.enumeration.AuditActionType;
import com.jovan.erp_v1.exception.UserNotFoundException;
import com.jovan.erp_v1.model.User;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.response.AuditLogResponse;
import com.jovan.erp_v1.service.IAuditLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/audit-logs")
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN','SECURITY_AUDITOR')")
public class AuditLogController {

    private final IAuditLogService auditLogService;
    private final UserRepository userRepository;

    @PostMapping("/log")
    public ResponseEntity<Void> log(@RequestParam("action") AuditActionType action,
            @RequestParam("userId") Long userId,
            @RequestParam("details") String details) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        auditLogService.log(action, user, details);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','SECURITY_AUDITOR','QUALITY_MANAGER','ACCOUNTANT')")
    @GetMapping("/{id}")
    public ResponseEntity<AuditLogResponse> getById(@PathVariable Long id) {
        return auditLogService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','SECURITY_AUDITOR','QUALITY_MANAGER','ACCOUNTANT')")
    @GetMapping("/between-dates")
    public ResponseEntity<List<AuditLogResponse>> getLogsBetweenDates(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<AuditLogResponse> responses = auditLogService.getLogsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','SECURITY_AUDITOR','QUALITY_MANAGER','ACCOUNTANT')")
    @GetMapping("/get-by-action")
    public ResponseEntity<List<AuditLogResponse>> getLogsByAction(@RequestParam("action") AuditActionType action) {
        List<AuditLogResponse> responses = auditLogService.getLogsByAction(action);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','SECURITY_AUDITOR','QUALITY_MANAGER','ACCOUNTANT')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLogResponse>> getLogsByUserId(@PathVariable Long userId) {
        List<AuditLogResponse> responses = auditLogService.getLogsByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','SECURITY_AUDITOR','QUALITY_MANAGER','ACCOUNTANT')")
    @GetMapping("/get-all-logs")
    public ResponseEntity<List<AuditLogResponse>> getAllLogs() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','SECURITY_AUDITOR','QUALITY_MANAGER','ACCOUNTANT')")
    @GetMapping("/search")
    public ResponseEntity<List<AuditLogResponse>> searchLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) AuditActionType action,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false) String userAgent) {
        List<AuditLogResponse> responses = auditLogService.searchLogs(userId, action, start, end, ipAddress, userAgent);
        return ResponseEntity.ok(responses);
    }

}
