package com.example.demo.service;

import com.example.demo.entity.AuditLog;
import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogService {
    AuditLog logEvent(Long userId, String action, String entity, Long entityId,
                      String details, String resultStatus);
    List<AuditLog> getAllAuditLogs();
    List<AuditLog> getAuditLogsByUser(Long userId);
    List<AuditLog> getAuditLogsByEntity(String entity);
    List<AuditLog> getAuditLogsByEntityAndEntityId(String entity, Long entityId);
    List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
