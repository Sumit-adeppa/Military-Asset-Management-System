package com.example.demo.controller;

import com.example.demo.dto.response.AuditLogResponse;
import com.example.demo.entity.AuditLog;
import com.example.demo.service.AuditLogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<List<AuditLogResponse>> getAuditLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String entity,
            @RequestParam(required = false) Long entityId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<AuditLog> logs;
        if (userId != null) {
            logs = auditLogService.getAuditLogsByUser(userId);
        } else if (entity != null && entityId != null) {
            logs = auditLogService.getAuditLogsByEntityAndEntityId(entity, entityId);
        } else if (entity != null) {
            logs = auditLogService.getAuditLogsByEntity(entity);
        } else if (startDate != null && endDate != null) {
            logs = auditLogService.getAuditLogsByDateRange(startDate, endDate);
        } else {
            logs = auditLogService.getAllAuditLogs();
        }
        return ResponseEntity.ok(logs.stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    private AuditLogResponse toResponse(AuditLog log) {
        return new AuditLogResponse(
                log.getId(),
                log.getUser() != null ? log.getUser().getId() : null,
                log.getUser() != null ? log.getUser().getUsername() : null,
                log.getAction(),
                log.getEntity(),
                log.getEntityId(),
                log.getTimestamp(),
                log.getDetails(),
                log.getResultStatus()
        );
    }
}
