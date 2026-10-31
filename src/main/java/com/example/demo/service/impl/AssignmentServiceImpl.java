package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.enums.AssignmentStatus;
import com.example.demo.exception.InsufficientInventoryException;
import com.example.demo.exception.InvalidOperationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.AssignmentService;
import com.example.demo.service.AuditLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final PersonnelRepository personnelRepository;
    private final EquipmentTypeRepository equipmentTypeRepository;
    private final BaseRepository baseRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final AuditLogService auditLogService;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository,
                                 PersonnelRepository personnelRepository,
                                 EquipmentTypeRepository equipmentTypeRepository,
                                 BaseRepository baseRepository,
                                 UserRepository userRepository,
                                 InventoryRepository inventoryRepository,
                                 AuditLogService auditLogService) {
        this.assignmentRepository = assignmentRepository;
        this.personnelRepository = personnelRepository;
        this.equipmentTypeRepository = equipmentTypeRepository;
        this.baseRepository = baseRepository;
        this.userRepository = userRepository;
        this.inventoryRepository = inventoryRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional
    public Assignment createAssignment(Long personnelId, Long equipmentTypeId, Long baseId,
                                       Integer quantity, LocalDate assignmentDate, Long userId) {
        Personnel personnel = personnelRepository.findById(personnelId)
                .orElseThrow(() -> new ResourceNotFoundException("Personnel not found with id: " + personnelId));
        EquipmentType equipmentType = equipmentTypeRepository.findById(equipmentTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment type not found with id: " + equipmentTypeId));
        Base base = baseRepository.findById(baseId)
                .orElseThrow(() -> new ResourceNotFoundException("Base not found with id: " + baseId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Inventory inventory = inventoryRepository.findByBaseIdAndEquipmentTypeId(baseId, equipmentTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for base " + baseId
                        + " and equipment type " + equipmentTypeId));
        if (inventory.getQuantity() < quantity) {
            throw new InsufficientInventoryException(
                    "Insufficient inventory. Available: " + inventory.getQuantity() + ", Requested: " + quantity);
        }
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventory.setClosingBalance(inventory.getClosingBalance() - quantity);
        inventoryRepository.save(inventory);

        Assignment assignment = new Assignment(personnel, equipmentType, base, quantity, assignmentDate, user);
        Assignment saved = assignmentRepository.save(assignment);

        auditLogService.logEvent(userId, "CREATE", "Assignment", saved.getId(),
                "Assigned " + quantity + " " + equipmentType.getName() + " to " + personnel.getName()
                        + " (SN: " + personnel.getServiceNumber() + ") at " + base.getName(), "SUCCESS");

        return saved;
    }

    @Override
    @Transactional
    public Assignment updateAssignmentStatus(Long assignmentId, AssignmentStatus status) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + assignmentId));
        if (assignment.getStatus() == AssignmentStatus.RETURNED || assignment.getStatus() == AssignmentStatus.LOST
                || assignment.getStatus() == AssignmentStatus.DAMAGED) {
            throw new InvalidOperationException("Cannot change status of a closed assignment");
        }

        AssignmentStatus oldStatus = assignment.getStatus();
        assignment.setStatus(status);
        Assignment saved = assignmentRepository.save(assignment);

        if (status == AssignmentStatus.RETURNED) {
            Inventory inventory = inventoryRepository
                    .findByBaseIdAndEquipmentTypeId(
                            assignment.getBase().getId(),
                            assignment.getEquipmentType().getId())
                    .orElseGet(() -> new Inventory(
                            assignment.getBase(),
                            assignment.getEquipmentType(),
                            0));
            inventory.setQuantity(inventory.getQuantity() + assignment.getQuantity());
            inventory.setClosingBalance(inventory.getClosingBalance() + assignment.getQuantity());
            inventoryRepository.save(inventory);
        }

        auditLogService.logEvent(null, "UPDATE_STATUS", "Assignment", assignmentId,
                "Assignment status changed from " + oldStatus + " to " + status, "SUCCESS");

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findByIdWithRelations(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAllWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Assignment> getAssignmentsByBase(Long baseId) {
        return assignmentRepository.findByBaseIdWithRelations(baseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Assignment> getAssignmentsByPersonnel(Long personnelId) {
        return assignmentRepository.findByPersonnelIdWithRelations(personnelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Assignment> getAssignmentsByStatus(AssignmentStatus status) {
        return assignmentRepository.findByStatusWithRelations(status);
    }
}
