package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.AuditLogService;
import com.example.demo.service.PurchaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final BaseRepository baseRepository;
    private final EquipmentTypeRepository equipmentTypeRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final AuditLogService auditLogService;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
                               BaseRepository baseRepository,
                               EquipmentTypeRepository equipmentTypeRepository,
                               UserRepository userRepository,
                               InventoryRepository inventoryRepository,
                               AuditLogService auditLogService) {
        this.purchaseRepository = purchaseRepository;
        this.baseRepository = baseRepository;
        this.equipmentTypeRepository = equipmentTypeRepository;
        this.userRepository = userRepository;
        this.inventoryRepository = inventoryRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional
    public Purchase recordPurchase(Long baseId, Long equipmentTypeId, Integer quantity,
                                   LocalDate purchaseDate, String transactionInfo, Long userId) {
        Base base = baseRepository.findById(baseId)
                .orElseThrow(() -> new ResourceNotFoundException("Base not found with id: " + baseId));
        EquipmentType equipmentType = equipmentTypeRepository.findById(equipmentTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment type not found with id: " + equipmentTypeId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Purchase purchase = new Purchase(base, equipmentType, quantity, purchaseDate, transactionInfo, user);
        Purchase saved = purchaseRepository.save(purchase);

        Inventory inventory = inventoryRepository.findByBaseIdAndEquipmentTypeId(baseId, equipmentTypeId)
                .orElseGet(() -> new Inventory(base, equipmentType, 0));
        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventory.setClosingBalance(inventory.getClosingBalance() + quantity);
        inventoryRepository.save(inventory);

        auditLogService.logEvent(userId, "CREATE", "Purchase", saved.getId(),
                "Recorded purchase of " + quantity + " " + equipmentType.getName() + " at base " + base.getName(),
                "SUCCESS");

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Purchase getPurchaseById(Long id) {
        return purchaseRepository.findByIdWithRelations(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAllWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getPurchasesByBase(Long baseId) {
        return purchaseRepository.findByBaseIdWithRelations(baseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getPurchasesByEquipmentType(Long equipmentTypeId) {
        return purchaseRepository.findByEquipmentTypeIdWithRelations(equipmentTypeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getPurchasesByDateRange(LocalDate startDate, LocalDate endDate) {
        return purchaseRepository.findByPurchaseDateBetweenWithRelations(startDate, endDate);
    }
}
