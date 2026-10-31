package com.example.demo.dto.response;

import java.time.LocalDate;

public class PurchaseResponse {

    private Long id;
    private Long baseId;
    private String baseName;
    private Long equipmentTypeId;
    private String equipmentTypeName;
    private Integer quantity;
    private LocalDate purchaseDate;
    private String transactionInfo;
    private Long userId;
    private String username;

    public PurchaseResponse() {}

    public PurchaseResponse(Long id, Long baseId, String baseName, Long equipmentTypeId,
                           String equipmentTypeName, Integer quantity, LocalDate purchaseDate,
                           String transactionInfo, Long userId, String username) {
        this.id = id;
        this.baseId = baseId;
        this.baseName = baseName;
        this.equipmentTypeId = equipmentTypeId;
        this.equipmentTypeName = equipmentTypeName;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
        this.transactionInfo = transactionInfo;
        this.userId = userId;
        this.username = username;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBaseId() { return baseId; }
    public void setBaseId(Long baseId) { this.baseId = baseId; }
    public String getBaseName() { return baseName; }
    public void setBaseName(String baseName) { this.baseName = baseName; }
    public Long getEquipmentTypeId() { return equipmentTypeId; }
    public void setEquipmentTypeId(Long equipmentTypeId) { this.equipmentTypeId = equipmentTypeId; }
    public String getEquipmentTypeName() { return equipmentTypeName; }
    public void setEquipmentTypeName(String equipmentTypeName) { this.equipmentTypeName = equipmentTypeName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
    public String getTransactionInfo() { return transactionInfo; }
    public void setTransactionInfo(String transactionInfo) { this.transactionInfo = transactionInfo; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
