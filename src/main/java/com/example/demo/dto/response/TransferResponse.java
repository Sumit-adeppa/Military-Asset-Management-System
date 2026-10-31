package com.example.demo.dto.response;

import com.example.demo.enums.TransferStatus;

import java.time.LocalDateTime;

public class TransferResponse {

    private Long id;
    private Long sourceBaseId;
    private String sourceBaseName;
    private Long destinationBaseId;
    private String destinationBaseName;
    private Long equipmentTypeId;
    private String equipmentTypeName;
    private Integer quantity;
    private LocalDateTime transferDate;
    private TransferStatus status;
    private Long userId;
    private String username;

    public TransferResponse() {}

    public TransferResponse(Long id, Long sourceBaseId, String sourceBaseName, Long destinationBaseId,
                            String destinationBaseName, Long equipmentTypeId, String equipmentTypeName,
                            Integer quantity, LocalDateTime transferDate, TransferStatus status,
                            Long userId, String username) {
        this.id = id;
        this.sourceBaseId = sourceBaseId;
        this.sourceBaseName = sourceBaseName;
        this.destinationBaseId = destinationBaseId;
        this.destinationBaseName = destinationBaseName;
        this.equipmentTypeId = equipmentTypeId;
        this.equipmentTypeName = equipmentTypeName;
        this.quantity = quantity;
        this.transferDate = transferDate;
        this.status = status;
        this.userId = userId;
        this.username = username;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSourceBaseId() { return sourceBaseId; }
    public void setSourceBaseId(Long sourceBaseId) { this.sourceBaseId = sourceBaseId; }
    public String getSourceBaseName() { return sourceBaseName; }
    public void setSourceBaseName(String sourceBaseName) { this.sourceBaseName = sourceBaseName; }
    public Long getDestinationBaseId() { return destinationBaseId; }
    public void setDestinationBaseId(Long destinationBaseId) { this.destinationBaseId = destinationBaseId; }
    public String getDestinationBaseName() { return destinationBaseName; }
    public void setDestinationBaseName(String destinationBaseName) { this.destinationBaseName = destinationBaseName; }
    public Long getEquipmentTypeId() { return equipmentTypeId; }
    public void setEquipmentTypeId(Long equipmentTypeId) { this.equipmentTypeId = equipmentTypeId; }
    public String getEquipmentTypeName() { return equipmentTypeName; }
    public void setEquipmentTypeName(String equipmentTypeName) { this.equipmentTypeName = equipmentTypeName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public LocalDateTime getTransferDate() { return transferDate; }
    public void setTransferDate(LocalDateTime transferDate) { this.transferDate = transferDate; }
    public TransferStatus getStatus() { return status; }
    public void setStatus(TransferStatus status) { this.status = status; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
