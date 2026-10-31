package com.example.demo.controller;

import com.example.demo.dto.request.TransferRequest;
import com.example.demo.security.AuthContext;
import com.example.demo.dto.response.TransferResponse;
import com.example.demo.entity.Transfer;
import com.example.demo.enums.TransferStatus;
import com.example.demo.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> initiateTransfer(@Valid @RequestBody TransferRequest request) {
        Transfer transfer = transferService.initiateTransfer(
                request.getSourceBaseId(),
                request.getDestinationBaseId(),
                request.getEquipmentTypeId(),
                request.getQuantity(),
                request.getTransferDate(),
                AuthContext.getUserId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(transfer));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<TransferResponse> approveTransfer(@PathVariable Long id) {
        Transfer transfer = transferService.approveTransfer(id, null);
        return ResponseEntity.ok(toResponse(transfer));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<TransferResponse> completeTransfer(@PathVariable Long id) {
        Transfer transfer = transferService.completeTransfer(id, null);
        return ResponseEntity.ok(toResponse(transfer));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<TransferResponse> rejectTransfer(@PathVariable Long id) {
        Transfer transfer = transferService.rejectTransfer(id, null);
        return ResponseEntity.ok(toResponse(transfer));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<TransferResponse> cancelTransfer(@PathVariable Long id) {
        Transfer transfer = transferService.cancelTransfer(id, null);
        return ResponseEntity.ok(toResponse(transfer));
    }

    @GetMapping
    public ResponseEntity<List<TransferResponse>> getTransfers(
            @RequestParam(required = false) Long sourceBaseId,
            @RequestParam(required = false) Long destinationBaseId,
            @RequestParam(required = false) TransferStatus status) {
        List<Transfer> transfers;
        if (sourceBaseId != null) {
            transfers = transferService.getTransfersBySourceBase(sourceBaseId);
        } else if (destinationBaseId != null) {
            transfers = transferService.getTransfersByDestinationBase(destinationBaseId);
        } else if (status != null) {
            transfers = transferService.getTransfersByStatus(status);
        } else {
            transfers = transferService.getAllTransfers();
        }
        return ResponseEntity.ok(transfers.stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponse> getTransferById(@PathVariable Long id) {
        Transfer transfer = transferService.getTransferById(id);
        return ResponseEntity.ok(toResponse(transfer));
    }

    private TransferResponse toResponse(Transfer transfer) {
        return new TransferResponse(
                transfer.getId(),
                transfer.getSourceBase().getId(),
                transfer.getSourceBase().getName(),
                transfer.getDestinationBase().getId(),
                transfer.getDestinationBase().getName(),
                transfer.getEquipmentType().getId(),
                transfer.getEquipmentType().getName(),
                transfer.getQuantity(),
                transfer.getTransferDate(),
                transfer.getStatus(),
                transfer.getUser().getId(),
                transfer.getUser().getUsername()
        );
    }
}
