package com.example.demo.entity;

import com.example.demo.enums.AssignmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personnel_id", nullable = false)
    @NotNull
    private Personnel personnel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_type_id", nullable = false)
    @NotNull
    private EquipmentType equipmentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_id", nullable = false)
    @NotNull
    private Base base;

    @Min(1)
    @Column(nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "assignment_date", nullable = false)
    private LocalDate assignmentDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus status = AssignmentStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Assignment() {}

    public Assignment(Personnel personnel, EquipmentType equipmentType, Base base,
                      Integer quantity, LocalDate assignmentDate, User user) {
        this.personnel = personnel;
        this.equipmentType = equipmentType;
        this.base = base;
        this.quantity = quantity;
        this.assignmentDate = assignmentDate;
        this.user = user;
        this.status = AssignmentStatus.ACTIVE;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Personnel getPersonnel() { return personnel; }
    public void setPersonnel(Personnel personnel) { this.personnel = personnel; }
    public EquipmentType getEquipmentType() { return equipmentType; }
    public void setEquipmentType(EquipmentType equipmentType) { this.equipmentType = equipmentType; }
    public Base getBase() { return base; }
    public void setBase(Base base) { this.base = base; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public LocalDate getAssignmentDate() { return assignmentDate; }
    public void setAssignmentDate(LocalDate assignmentDate) { this.assignmentDate = assignmentDate; }
    public AssignmentStatus getStatus() { return status; }
    public void setStatus(AssignmentStatus status) { this.status = status; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
