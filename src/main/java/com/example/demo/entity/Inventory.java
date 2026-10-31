package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"base_id", "equipment_type_id"})
})
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_id", nullable = false)
    @NotNull
    private Base base;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_type_id", nullable = false)
    @NotNull
    private EquipmentType equipmentType;

    @Min(0)
    @Column(nullable = false)
    private Integer quantity = 0;

    @Min(0)
    @Column(name = "opening_balance", nullable = false)
    private Integer openingBalance = 0;

    @Min(0)
    @Column(name = "closing_balance", nullable = false)
    private Integer closingBalance = 0;

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

    public Inventory() {}

    public Inventory(Base base, EquipmentType equipmentType, Integer quantity) {
        this.base = base;
        this.equipmentType = equipmentType;
        this.quantity = quantity;
        this.openingBalance = quantity;
        this.closingBalance = quantity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Base getBase() { return base; }
    public void setBase(Base base) { this.base = base; }
    public EquipmentType getEquipmentType() { return equipmentType; }
    public void setEquipmentType(EquipmentType equipmentType) { this.equipmentType = equipmentType; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getOpeningBalance() { return openingBalance; }
    public void setOpeningBalance(Integer openingBalance) { this.openingBalance = openingBalance; }
    public Integer getClosingBalance() { return closingBalance; }
    public void setClosingBalance(Integer closingBalance) { this.closingBalance = closingBalance; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
