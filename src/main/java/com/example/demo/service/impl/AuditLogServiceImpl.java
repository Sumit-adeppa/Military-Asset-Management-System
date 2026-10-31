package com.example.demo.service.impl;

import com.example.demo.entity.AuditLog;
import com.example.demo.entity.User;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuditLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public AuditLog logEvent(Long userId, String action, String entity, Long entityId,
                             String details, String resultStatus) {
        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId).orElse(null);
        }
        AuditLog log = new AuditLog(user, action, entity, entityId, details, resultStatus);
        return auditLogRepository.save(log);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAllWithUser();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByUser(Long userId) {
        return auditLogRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByEntity(String entity) {
        return auditLogRepository.findByEntity(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByEntityAndEntityId(String entity, Long entityId) {
        return auditLogRepository.findByEntityAndEntityId(entity, entityId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByTimestampBetween(startDate, endDate);
    }
}
