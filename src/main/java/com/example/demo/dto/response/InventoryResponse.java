package com.example.demo.dto.response;

public class InventoryResponse {

    private Long id;
    private Long baseId;
    private String baseName;
    private Long equipmentTypeId;
    private String equipmentTypeName;
    private Integer quantity;
    private Integer openingBalance;
    private Integer closingBalance;

    public InventoryResponse() {}

    public InventoryResponse(Long id, Long baseId, String baseName, Long equipmentTypeId,
                            String equipmentTypeName, Integer quantity, Integer openingBalance, Integer closingBalance) {
        this.id = id;
        this.baseId = baseId;
        this.baseName = baseName;
        this.equipmentTypeId = equipmentTypeId;
        this.equipmentTypeName = equipmentTypeName;
        this.quantity = quantity;
        this.openingBalance = openingBalance;
        this.closingBalance = closingBalance;
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
    public Integer getOpeningBalance() { return openingBalance; }
    public void setOpeningBalance(Integer openingBalance) { this.openingBalance = openingBalance; }
    public Integer getClosingBalance() { return closingBalance; }
    public void setClosingBalance(Integer closingBalance) { this.closingBalance = closingBalance; }
}
