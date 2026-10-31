package com.example.demo.dto.response;

import java.time.LocalDateTime;

public class AuditLogResponse {

    private Long id;
    private Long userId;
    private String username;
    private String action;
    private String entity;
    private Long entityId;
    private LocalDateTime timestamp;
    private String details;
    private String resultStatus;

    public AuditLogResponse() {}

    public AuditLogResponse(Long id, Long userId, String username, String action, String entity,
                            Long entityId, LocalDateTime timestamp, String details, String resultStatus) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.action = action;
        this.entity = entity;
        this.entityId = entityId;
        this.timestamp = timestamp;
        this.details = details;
        this.resultStatus = resultStatus;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getEntity() { return entity; }
    public void setEntity(String entity) { this.entity = entity; }
    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getResultStatus() { return resultStatus; }
    public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }
}
