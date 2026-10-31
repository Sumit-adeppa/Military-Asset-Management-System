package com.example.demo.dto.response;

import com.example.demo.enums.AssignmentStatus;

import java.time.LocalDate;

public class AssignmentResponse {

    private Long id;
    private Long personnelId;
    private String personnelName;
    private String personnelRank;
    private String personnelServiceNumber;
    private Long equipmentTypeId;
    private String equipmentTypeName;
    private Long baseId;
    private String baseName;
    private Integer quantity;
    private LocalDate assignmentDate;
    private AssignmentStatus status;
    private Long userId;
    private String username;

    public AssignmentResponse() {}

    public AssignmentResponse(Long id, Long personnelId, String personnelName, String personnelRank,
                              String personnelServiceNumber, Long equipmentTypeId, String equipmentTypeName,
                              Long baseId, String baseName, Integer quantity, LocalDate assignmentDate,
                              AssignmentStatus status, Long userId, String username) {
        this.id = id;
        this.personnelId = personnelId;
        this.personnelName = personnelName;
        this.personnelRank = personnelRank;
        this.personnelServiceNumber = personnelServiceNumber;
        this.equipmentTypeId = equipmentTypeId;
        this.equipmentTypeName = equipmentTypeName;
        this.baseId = baseId;
        this.baseName = baseName;
        this.quantity = quantity;
        this.assignmentDate = assignmentDate;
        this.status = status;
        this.userId = userId;
        this.username = username;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPersonnelId() { return personnelId; }
    public void setPersonnelId(Long personnelId) { this.personnelId = personnelId; }
    public String getPersonnelName() { return personnelName; }
    public void setPersonnelName(String personnelName) { this.personnelName = personnelName; }
    public String getPersonnelRank() { return personnelRank; }
    public void setPersonnelRank(String personnelRank) { this.personnelRank = personnelRank; }
    public String getPersonnelServiceNumber() { return personnelServiceNumber; }
    public void setPersonnelServiceNumber(String personnelServiceNumber) { this.personnelServiceNumber = personnelServiceNumber; }
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
    public LocalDate getAssignmentDate() { return assignmentDate; }
    public void setAssignmentDate(LocalDate assignmentDate) { this.assignmentDate = assignmentDate; }
    public AssignmentStatus getStatus() { return status; }
    public void setStatus(AssignmentStatus status) { this.status = status; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
