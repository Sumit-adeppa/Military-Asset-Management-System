package com.example.demo.service.impl;

import com.example.demo.entity.EquipmentType;
import com.example.demo.entity.Inventory;
import com.example.demo.exception.InvalidOperationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EquipmentTypeRepository;
import com.example.demo.repository.BaseRepository;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final BaseRepository baseRepository;
    private final EquipmentTypeRepository equipmentTypeRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository,
                               BaseRepository baseRepository,
                               EquipmentTypeRepository equipmentTypeRepository) {
        this.inventoryRepository = inventoryRepository;
        this.baseRepository = baseRepository;
        this.equipmentTypeRepository = equipmentTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Inventory getInventory(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Inventory getInventoryByBaseAndEquipment(Long baseId, Long equipmentTypeId) {
        return inventoryRepository.findByBaseAndEquipmentWithRelations(baseId, equipmentTypeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for base " + baseId + " and equipment type " + equipmentTypeId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAllWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryByBase(Long baseId) {
        return inventoryRepository.findByBaseIdWithRelations(baseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryByEquipmentType(Long equipmentTypeId) {
        return inventoryRepository.findByEquipmentTypeIdWithRelations(equipmentTypeId);
    }

    @Override
    @Transactional
    public Inventory createInitialInventory(Long baseId, Long equipmentTypeId, Integer openingQuantity) {
        if (!baseRepository.existsById(baseId)) {
            throw new ResourceNotFoundException("Base not found with id: " + baseId);
        }
        EquipmentType equipmentType = equipmentTypeRepository.findById(equipmentTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment type not found with id: " + equipmentTypeId));
        if (openingQuantity < 0) {
            throw new InvalidOperationException("Opening quantity cannot be negative");
        }
        if (inventoryRepository.findByBaseIdAndEquipmentTypeId(baseId, equipmentTypeId).isPresent()) {
            throw new InvalidOperationException(
                    "Inventory already exists for base " + baseId + " and equipment type " + equipmentTypeId);
        }
        var base = baseRepository.findById(baseId).orElseThrow();
        return inventoryRepository.save(new Inventory(base, equipmentType, openingQuantity));
    }

    @Override
    @Transactional
    public Inventory updateOpeningBalance(Long inventoryId, Integer newOpeningBalance) {
        if (newOpeningBalance < 0) {
            throw new InvalidOperationException("Opening balance cannot be negative");
        }
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + inventoryId));

        int difference = newOpeningBalance - inventory.getOpeningBalance();
        inventory.setOpeningBalance(newOpeningBalance);
        inventory.setQuantity(inventory.getQuantity() + difference);
        inventory.setClosingBalance(inventory.getClosingBalance() + difference);

        return inventoryRepository.save(inventory);
    }
}
