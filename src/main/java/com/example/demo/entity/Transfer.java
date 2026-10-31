package com.example.demo.entity;

import com.example.demo.enums.TransferStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_base_id", nullable = false)
    @NotNull
    private Base sourceBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_base_id", nullable = false)
    @NotNull
    private Base destinationBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_type_id", nullable = false)
    @NotNull
    private EquipmentType equipmentType;

    @Min(1)
    @Column(nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "transfer_date", nullable = false)
    private LocalDateTime transferDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status = TransferStatus.PENDING;

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

    public Transfer() {}

    public Transfer(Base sourceBase, Base destinationBase, EquipmentType equipmentType,
                    Integer quantity, LocalDateTime transferDate, User user) {
        this.sourceBase = sourceBase;
        this.destinationBase = destinationBase;
        this.equipmentType = equipmentType;
        this.quantity = quantity;
        this.transferDate = transferDate;
        this.user = user;
        this.status = TransferStatus.PENDING;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Base getSourceBase() { return sourceBase; }
    public void setSourceBase(Base sourceBase) { this.sourceBase = sourceBase; }
    public Base getDestinationBase() { return destinationBase; }
    public void setDestinationBase(Base destinationBase) { this.destinationBase = destinationBase; }
    public EquipmentType getEquipmentType() { return equipmentType; }
    public void setEquipmentType(EquipmentType equipmentType) { this.equipmentType = equipmentType; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public LocalDateTime getTransferDate() { return transferDate; }
    public void setTransferDate(LocalDateTime transferDate) { this.transferDate = transferDate; }
    public TransferStatus getStatus() { return status; }
    public void setStatus(TransferStatus status) { this.status = status; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
