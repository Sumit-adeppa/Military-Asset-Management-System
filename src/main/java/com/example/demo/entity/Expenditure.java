package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenditures")
public class Expenditure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @Column(name = "expenditure_date", nullable = false)
    private LocalDate expenditureDate;

    @Size(max = 255)
    private String reason;

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

    public Expenditure() {}

    public Expenditure(EquipmentType equipmentType, Base base, Integer quantity,
                       LocalDate expenditureDate, String reason, User user) {
        this.equipmentType = equipmentType;
        this.base = base;
        this.quantity = quantity;
        this.expenditureDate = expenditureDate;
        this.reason = reason;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public EquipmentType getEquipmentType() { return equipmentType; }
    public void setEquipmentType(EquipmentType equipmentType) { this.equipmentType = equipmentType; }
    public Base getBase() { return base; }
    public void setBase(Base base) { this.base = base; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public LocalDate getExpenditureDate() { return expenditureDate; }
    public void setExpenditureDate(LocalDate expenditureDate) { this.expenditureDate = expenditureDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
