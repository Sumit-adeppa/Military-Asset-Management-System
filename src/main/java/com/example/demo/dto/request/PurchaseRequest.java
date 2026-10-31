package com.example.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class PurchaseRequest {

    @NotNull(message = "Base ID is required")
    private Long baseId;

    @NotNull(message = "Equipment type ID is required")
    private Long equipmentTypeId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Purchase date is required")
    private LocalDate purchaseDate;

    @Size(max = 255, message = "Transaction info must be at most 255 characters")
    private String transactionInfo;

    public PurchaseRequest() {
    }

    public Long getBaseId() {
        return baseId;
    }

    public void setBaseId(Long baseId) {
        this.baseId = baseId;
    }

    public Long getEquipmentTypeId() {
        return equipmentTypeId;
    }

    public void setEquipmentTypeId(Long equipmentTypeId) {
        this.equipmentTypeId = equipmentTypeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(String transactionInfo) {
        this.transactionInfo = transactionInfo;
    }
}
