package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.enums.AssignmentStatus;
import com.example.demo.enums.TransferStatus;
import com.example.demo.repository.*;
import com.example.demo.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final InventoryRepository inventoryRepository;
    private final PurchaseRepository purchaseRepository;
    private final TransferRepository transferRepository;
    private final AssignmentRepository assignmentRepository;
    private final ExpenditureRepository expenditureRepository;

    public DashboardServiceImpl(InventoryRepository inventoryRepository,
                                PurchaseRepository purchaseRepository,
                                TransferRepository transferRepository,
                                AssignmentRepository assignmentRepository,
                                ExpenditureRepository expenditureRepository) {
        this.inventoryRepository = inventoryRepository;
        this.purchaseRepository = purchaseRepository;
        this.transferRepository = transferRepository;
        this.assignmentRepository = assignmentRepository;
        this.expenditureRepository = expenditureRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardData(Long baseId, Long equipmentTypeId,
                                                LocalDate startDate, LocalDate endDate) {
        Map<String, Object> result = new HashMap<>();

        List<Inventory> inventories;
        if (baseId != null && equipmentTypeId != null) {
            inventories = inventoryRepository
                    .findByBaseIdAndEquipmentTypeId(baseId, equipmentTypeId)
                    .map(List::of)
                    .orElse(List.of());
        } else if (baseId != null) {
            inventories = inventoryRepository.findByBaseId(baseId);
        } else if (equipmentTypeId != null) {
            inventories = inventoryRepository.findByEquipmentTypeId(equipmentTypeId);
        } else {
            inventories = inventoryRepository.findAll();
        }

        int openingBalance = inventories.stream().mapToInt(Inventory::getOpeningBalance).sum();

        int purchases = purchaseRepository.findAll().stream()
                .filter(p -> matchesFilter(p.getBase().getId(), p.getEquipmentType().getId(), baseId, equipmentTypeId))
                .filter(p -> isWithinRange(p.getPurchaseDate(), startDate, endDate))
                .mapToInt(Purchase::getQuantity)
                .sum();

        int transferIn = transferRepository.findAll().stream()
                .filter(t -> t.getStatus() == TransferStatus.COMPLETED)
                .filter(t -> matchesFilter(t.getDestinationBase().getId(), t.getEquipmentType().getId(), baseId, equipmentTypeId))
                .filter(t -> isWithinRange(t.getTransferDate().toLocalDate(), startDate, endDate))
                .mapToInt(Transfer::getQuantity)
                .sum();

        int transferOut = transferRepository.findAll().stream()
                .filter(t -> t.getStatus() == TransferStatus.COMPLETED)
                .filter(t -> matchesFilter(t.getSourceBase().getId(), t.getEquipmentType().getId(), baseId, equipmentTypeId))
                .filter(t -> isWithinRange(t.getTransferDate().toLocalDate(), startDate, endDate))
                .mapToInt(Transfer::getQuantity)
                .sum();

        int netMovement = purchases + transferIn - transferOut;

        int assigned = assignmentRepository.findAll().stream()
                .filter(a -> a.getStatus() == AssignmentStatus.ACTIVE)
                .filter(a -> matchesFilter(a.getBase().getId(), a.getEquipmentType().getId(), baseId, equipmentTypeId))
                .filter(a -> isWithinRange(a.getAssignmentDate(), startDate, endDate))
                .mapToInt(Assignment::getQuantity)
                .sum();

        int expended = expenditureRepository.findAll().stream()
                .filter(e -> matchesFilter(e.getBase().getId(), e.getEquipmentType().getId(), baseId, equipmentTypeId))
                .filter(e -> isWithinRange(e.getExpenditureDate(), startDate, endDate))
                .mapToInt(Expenditure::getQuantity)
                .sum();

        int closingBalance = openingBalance + netMovement - assigned - expended;

        result.put("openingBalance", openingBalance);
        result.put("purchases", purchases);
        result.put("transferIn", transferIn);
        result.put("transferOut", transferOut);
        result.put("netMovement", netMovement);
        result.put("assigned", assigned);
        result.put("expended", expended);
        result.put("closingBalance", closingBalance);

        return result;
    }

    private boolean matchesFilter(Long entityBaseId, Long entityEquipmentTypeId,
                                  Long filterBaseId, Long filterEquipmentTypeId) {
        if (filterBaseId != null && !entityBaseId.equals(filterBaseId)) return false;
        if (filterEquipmentTypeId != null && !entityEquipmentTypeId.equals(filterEquipmentTypeId)) return false;
        return true;
    }

    private boolean isWithinRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && date.isBefore(startDate)) return false;
        if (endDate != null && date.isAfter(endDate)) return false;
        return true;
    }
}
