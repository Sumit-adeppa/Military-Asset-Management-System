package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Size(max = 50)
    @Column(nullable = false)
    private String action;

    @NotNull
    @Size(max = 50)
    @Column(nullable = false)
    private String entity;

    @Column(name = "entity_id")
    private Long entityId;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "details", length = 2000)
    private String details;

    @Size(max = 20)
    @Column(name = "result_status")
    private String resultStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        createdAt = LocalDateTime.now();
    }

    public AuditLog() {}

    public AuditLog(User user, String action, String entity, Long entityId,
                    String details, String resultStatus) {
        this.user = user;
        this.action = action;
        this.entity = entity;
        this.entityId = entityId;
        this.details = details;
        this.resultStatus = resultStatus;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
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
    public LocalDateTime getCreatedAt() { return createdAt; }
}
