package com.example.demo.dto.response;

import java.time.LocalDate;

public class ExpenditureResponse {

    private Long id;
    private Long equipmentTypeId;
    private String equipmentTypeName;
    private Long baseId;
    private String baseName;
    private Integer quantity;
    private LocalDate expenditureDate;
    private String reason;
    private Long userId;
    private String username;

    public ExpenditureResponse() {}

    public ExpenditureResponse(Long id, Long equipmentTypeId, String equipmentTypeName,
                              Long baseId, String baseName, Integer quantity, LocalDate expenditureDate,
                              String reason, Long userId, String username) {
        this.id = id;
        this.equipmentTypeId = equipmentTypeId;
        this.equipmentTypeName = equipmentTypeName;
        this.baseId = baseId;
        this.baseName = baseName;
        this.quantity = quantity;
        this.expenditureDate = expenditureDate;
        this.reason = reason;
        this.userId = userId;
        this.username = username;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEquipmentTypeId() { return equipmentTypeId; }
    public void setEquipmentTypeId(Long equipmentTypeId) { this.equipmentTypeId = equipmentTypeId; }
    public String getEquipmentTypeName() { return equipmentTypeName; }
    public void setEquipmentTypeName(String equipmentTypeName) { this.equipmentTypeName = equipmentTypeName; }
    public Long getBaseId() { return baseId; }
    public void setBaseId(Long baseId) { this.baseId = baseId; }
    public String getBaseName() { return baseName; }
    public void setBaseName(String baseName) { this.baseName = baseName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public LocalDate getExpenditureDate() { return expenditureDate; }
    public void setExpenditureDate(LocalDate expenditureDate) { this.expenditureDate = expenditureDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
