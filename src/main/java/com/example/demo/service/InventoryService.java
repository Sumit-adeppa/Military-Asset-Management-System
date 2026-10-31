package com.example.demo.service;

import com.example.demo.entity.Inventory;
import java.util.List;

public interface InventoryService {
    Inventory getInventory(Long id);
    Inventory getInventoryByBaseAndEquipment(Long baseId, Long equipmentTypeId);
    List<Inventory> getAllInventory();
    List<Inventory> getInventoryByBase(Long baseId);
    List<Inventory> getInventoryByEquipmentType(Long equipmentTypeId);
    Inventory createInitialInventory(Long baseId, Long equipmentTypeId, Integer openingQuantity);
    Inventory updateOpeningBalance(Long inventoryId, Integer newOpeningBalance);
}
