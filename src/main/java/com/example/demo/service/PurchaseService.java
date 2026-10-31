package com.example.demo.service;

import com.example.demo.entity.Purchase;
import java.time.LocalDate;
import java.util.List;

public interface PurchaseService {
    Purchase recordPurchase(Long baseId, Long equipmentTypeId, Integer quantity,
                           LocalDate purchaseDate, String transactionInfo, Long userId);
    Purchase getPurchaseById(Long id);
    List<Purchase> getAllPurchases();
    List<Purchase> getPurchasesByBase(Long baseId);
    List<Purchase> getPurchasesByEquipmentType(Long equipmentTypeId);
    List<Purchase> getPurchasesByDateRange(LocalDate startDate, LocalDate endDate);
}
