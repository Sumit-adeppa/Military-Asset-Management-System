package com.example.demo.controller;

import com.example.demo.dto.request.PurchaseRequest;
import com.example.demo.dto.response.PurchaseResponse;
import com.example.demo.entity.Purchase;
import com.example.demo.security.AuthContext;
import com.example.demo.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<PurchaseResponse> recordPurchase(@Valid @RequestBody PurchaseRequest request) {
        Purchase purchase = purchaseService.recordPurchase(
                request.getBaseId(),
                request.getEquipmentTypeId(),
                request.getQuantity(),
                request.getPurchaseDate(),
                request.getTransactionInfo(),
                AuthContext.getUserId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(purchase));
    }

    @GetMapping
    public ResponseEntity<List<PurchaseResponse>> getPurchases(
            @RequestParam(required = false) Long baseId,
            @RequestParam(required = false) Long equipmentTypeId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        List<Purchase> purchases;
        if (startDate != null && endDate != null) {
            purchases = purchaseService.getPurchasesByDateRange(startDate, endDate);
        } else if (baseId != null) {
            purchases = purchaseService.getPurchasesByBase(baseId);
        } else if (equipmentTypeId != null) {
            purchases = purchaseService.getPurchasesByEquipmentType(equipmentTypeId);
        } else {
            purchases = purchaseService.getAllPurchases();
        }
        return ResponseEntity.ok(purchases.stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponse> getPurchaseById(@PathVariable Long id) {
        Purchase purchase = purchaseService.getPurchaseById(id);
        return ResponseEntity.ok(toResponse(purchase));
    }

    private PurchaseResponse toResponse(Purchase purchase) {
        return new PurchaseResponse(
                purchase.getId(),
                purchase.getBase().getId(),
                purchase.getBase().getName(),
                purchase.getEquipmentType().getId(),
                purchase.getEquipmentType().getName(),
                purchase.getQuantity(),
                purchase.getPurchaseDate(),
                purchase.getTransactionInfo(),
                purchase.getUser().getId(),
                purchase.getUser().getUsername()
        );
    }
}
