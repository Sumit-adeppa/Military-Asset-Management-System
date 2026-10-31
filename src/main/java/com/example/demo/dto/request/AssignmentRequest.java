package com.example.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class AssignmentRequest {

    @NotNull(message = "Personnel ID is required")
    private Long personnelId;

    @NotNull(message = "Equipment type ID is required")
    private Long equipmentTypeId;

    @NotNull(message = "Base ID is required")
    private Long baseId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Assignment date is required")
    private LocalDate assignmentDate;

    public AssignmentRequest() {}

    public Long getPersonnelId() { return personnelId; }
    public void setPersonnelId(Long personnelId) { this.personnelId = personnelId; }
    public Long getEquipmentTypeId() { return equipmentTypeId; }
    public void setEquipmentTypeId(Long equipmentTypeId) { this.equipmentTypeId = equipmentTypeId; }
    public Long getBaseId() { return baseId; }
    public void setBaseId(Long baseId) { this.baseId = baseId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public LocalDate getAssignmentDate() { return assignmentDate; }
    public void setAssignmentDate(LocalDate assignmentDate) { this.assignmentDate = assignmentDate; }
}
