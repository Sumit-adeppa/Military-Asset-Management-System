package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases")
public class Purchase {

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

    @Min(1)
    @Column(nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Size(max = 255)
    @Column(name = "transaction_info")
    private String transactionInfo;

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

    public Purchase() {}

    public Purchase(Base base, EquipmentType equipmentType, Integer quantity,
                    LocalDate purchaseDate, String transactionInfo, User user) {
        this.base = base;
        this.equipmentType = equipmentType;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
        this.transactionInfo = transactionInfo;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Base getBase() { return base; }
    public void setBase(Base base) { this.base = base; }
    public EquipmentType getEquipmentType() { return equipmentType; }
    public void setEquipmentType(EquipmentType equipmentType) { this.equipmentType = equipmentType; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
    public String getTransactionInfo() { return transactionInfo; }
    public void setTransactionInfo(String transactionInfo) { this.transactionInfo = transactionInfo; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
