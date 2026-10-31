package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.InsufficientInventoryException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.AuditLogService;
import com.example.demo.service.ExpenditureService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenditureServiceImpl implements ExpenditureService {

    private final ExpenditureRepository expenditureRepository;
    private final EquipmentTypeRepository equipmentTypeRepository;
    private final BaseRepository baseRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final AuditLogService auditLogService;

    public ExpenditureServiceImpl(ExpenditureRepository expenditureRepository,
                                  EquipmentTypeRepository equipmentTypeRepository,
                                  BaseRepository baseRepository,
                                  UserRepository userRepository,
                                  InventoryRepository inventoryRepository,
                                  AuditLogService auditLogService) {
        this.expenditureRepository = expenditureRepository;
        this.equipmentTypeRepository = equipmentTypeRepository;
        this.baseRepository = baseRepository;
        this.userRepository = userRepository;
        this.inventoryRepository = inventoryRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional
    public Expenditure recordExpenditure(Long equipmentTypeId, Long baseId, Integer quantity,
                                        LocalDate expenditureDate, String reason, Long userId) {
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

        Expenditure expenditure = new Expenditure(equipmentType, base, quantity, expenditureDate, reason, user);
        Expenditure saved = expenditureRepository.save(expenditure);

        auditLogService.logEvent(userId, "CREATE", "Expenditure", saved.getId(),
                "Recorded expenditure of " + quantity + " " + equipmentType.getName()
                        + " at " + base.getName() + " for reason: " + reason, "SUCCESS");

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Expenditure getExpenditureById(Long id) {
        return expenditureRepository.findByIdWithRelations(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Expenditure> getAllExpenditures() {
        return expenditureRepository.findAllWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Expenditure> getExpendituresByBase(Long baseId) {
        return expenditureRepository.findByBaseIdWithRelations(baseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Expenditure> getExpendituresByEquipmentType(Long equipmentTypeId) {
        return expenditureRepository.findByEquipmentTypeIdWithRelations(equipmentTypeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Expenditure> getExpendituresByDateRange(LocalDate startDate, LocalDate endDate) {
        return expenditureRepository.findByExpenditureDateBetweenWithRelations(startDate, endDate);
    }
}
