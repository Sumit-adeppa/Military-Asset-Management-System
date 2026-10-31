package com.example.demo.controller;

import com.example.demo.dto.response.InventoryResponse;
import com.example.demo.entity.Inventory;
import com.example.demo.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getInventory(
            @RequestParam(required = false) Long baseId,
            @RequestParam(required = false) Long equipmentTypeId) {
        List<Inventory> inventory;
        if (baseId != null && equipmentTypeId != null) {
            inventory = List.of(inventoryService.getInventoryByBaseAndEquipment(baseId, equipmentTypeId));
        } else if (baseId != null) {
            inventory = inventoryService.getInventoryByBase(baseId);
        } else if (equipmentTypeId != null) {
            inventory = inventoryService.getInventoryByEquipmentType(equipmentTypeId);
        } else {
            inventory = inventoryService.getAllInventory();
        }
        return ResponseEntity.ok(inventory.stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getInventoryById(@PathVariable Long id) {
        Inventory inventory = inventoryService.getInventory(id);
        return ResponseEntity.ok(toResponse(inventory));
    }

    @PutMapping("/{id}/opening-balance")
    public ResponseEntity<InventoryResponse> updateOpeningBalance(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        Integer newOpeningBalance = request.get("openingBalance");
        if (newOpeningBalance == null) {
            return ResponseEntity.badRequest().build();
        }
        Inventory inventory = inventoryService.updateOpeningBalance(id, newOpeningBalance);
        return ResponseEntity.ok(toResponse(inventory));
    }

    private InventoryResponse toResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getId(),
                inventory.getBase().getId(),
                inventory.getBase().getName(),
                inventory.getEquipmentType().getId(),
                inventory.getEquipmentType().getName(),
                inventory.getQuantity(),
                inventory.getOpeningBalance(),
                inventory.getClosingBalance()
        );
    }
}
