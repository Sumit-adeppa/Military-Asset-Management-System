package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.enums.TransferStatus;
import com.example.demo.exception.InsufficientInventoryException;
import com.example.demo.exception.InvalidOperationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.AuditLogService;
import com.example.demo.service.TransferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final BaseRepository baseRepository;
    private final EquipmentTypeRepository equipmentTypeRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final AuditLogService auditLogService;

    public TransferServiceImpl(TransferRepository transferRepository,
                               BaseRepository baseRepository,
                               EquipmentTypeRepository equipmentTypeRepository,
                               UserRepository userRepository,
                               InventoryRepository inventoryRepository,
                               AuditLogService auditLogService) {
        this.transferRepository = transferRepository;
        this.baseRepository = baseRepository;
        this.equipmentTypeRepository = equipmentTypeRepository;
        this.userRepository = userRepository;
        this.inventoryRepository = inventoryRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional
    public Transfer initiateTransfer(Long sourceBaseId, Long destinationBaseId, Long equipmentTypeId,
                                     Integer quantity, LocalDateTime transferDate, Long userId) {
        if (sourceBaseId.equals(destinationBaseId)) {
            throw new InvalidOperationException("Source and destination bases must differ");
        }
        Base sourceBase = baseRepository.findById(sourceBaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Source base not found with id: " + sourceBaseId));
        Base destBase = baseRepository.findById(destinationBaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination base not found with id: " + destinationBaseId));
        EquipmentType equipmentType = equipmentTypeRepository.findById(equipmentTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment type not found with id: " + equipmentTypeId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Transfer transfer = new Transfer(sourceBase, destBase, equipmentType, quantity, transferDate, user);
        Transfer saved = transferRepository.save(transfer);

        auditLogService.logEvent(userId, "INITIATE", "Transfer", saved.getId(),
                "Transfer of " + quantity + " " + equipmentType.getName() +
                        " from " + sourceBase.getName() + " to " + destBase.getName(),
                "PENDING");

        return saved;
    }

    @Override
    @Transactional
    public Transfer approveTransfer(Long transferId, Long userId) {
        Transfer transfer = transferRepository.findByIdWithRelations(transferId);
        if (transfer.getStatus() != TransferStatus.PENDING) {
            throw new InvalidOperationException("Only PENDING transfers can be approved");
        }
        transfer.setStatus(TransferStatus.APPROVED);
        Transfer saved = transferRepository.save(transfer);
        auditLogService.logEvent(userId, "APPROVE", "Transfer", transferId,
                "Transfer " + transferId + " approved", "SUCCESS");
        return saved;
    }

    @Override
    @Transactional
    public Transfer completeTransfer(Long transferId, Long userId) {
        Transfer transfer = transferRepository.findByIdWithRelations(transferId);
        if (transfer.getStatus() != TransferStatus.APPROVED) {
            throw new InvalidOperationException("Only APPROVED transfers can be completed");
        }

        Inventory sourceInv = inventoryRepository
                .findByBaseIdAndEquipmentTypeId(
                        transfer.getSourceBase().getId(),
                        transfer.getEquipmentType().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Source inventory not found"));
        if (sourceInv.getQuantity() < transfer.getQuantity()) {
            throw new InsufficientInventoryException(
                    "Insufficient inventory at source base. Available: " + sourceInv.getQuantity()
                            + ", Requested: " + transfer.getQuantity());
        }
        sourceInv.setQuantity(sourceInv.getQuantity() - transfer.getQuantity());
        sourceInv.setClosingBalance(sourceInv.getClosingBalance() - transfer.getQuantity());
        inventoryRepository.save(sourceInv);

        Inventory destInv = inventoryRepository
                .findByBaseIdAndEquipmentTypeId(
                        transfer.getDestinationBase().getId(),
                        transfer.getEquipmentType().getId())
                .orElseGet(() -> new Inventory(
                        transfer.getDestinationBase(),
                        transfer.getEquipmentType(),
                        0));
        destInv.setQuantity(destInv.getQuantity() + transfer.getQuantity());
        destInv.setClosingBalance(destInv.getClosingBalance() + transfer.getQuantity());
        inventoryRepository.save(destInv);

        transfer.setStatus(TransferStatus.COMPLETED);
        Transfer saved = transferRepository.save(transfer);

        auditLogService.logEvent(userId, "COMPLETE", "Transfer", transferId,
                "Transfer of " + transfer.getQuantity() + " "
                        + transfer.getEquipmentType().getName() + " completed from "
                        + transfer.getSourceBase().getName() + " to "
                        + transfer.getDestinationBase().getName(), "SUCCESS");

        return saved;
    }

    @Override
    @Transactional
    public Transfer rejectTransfer(Long transferId, Long userId) {
        Transfer transfer = transferRepository.findByIdWithRelations(transferId);
        if (transfer.getStatus() != TransferStatus.PENDING) {
            throw new InvalidOperationException("Only PENDING transfers can be rejected");
        }
        transfer.setStatus(TransferStatus.REJECTED);
        Transfer saved = transferRepository.save(transfer);
        auditLogService.logEvent(userId, "REJECT", "Transfer", transferId,
                "Transfer " + transferId + " rejected", "SUCCESS");
        return saved;
    }

    @Override
    @Transactional
    public Transfer cancelTransfer(Long transferId, Long userId) {
        Transfer transfer = transferRepository.findByIdWithRelations(transferId);
        if (transfer.getStatus() == TransferStatus.COMPLETED) {
            throw new InvalidOperationException("Completed transfers cannot be cancelled");
        }
        transfer.setStatus(TransferStatus.CANCELLED);
        Transfer saved = transferRepository.save(transfer);
        auditLogService.logEvent(userId, "CANCEL", "Transfer", transferId,
                "Transfer " + transferId + " cancelled", "SUCCESS");
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Transfer getTransferById(Long id) {
        return transferRepository.findByIdWithRelations(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAllWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transfer> getTransfersBySourceBase(Long sourceBaseId) {
        return transferRepository.findBySourceBaseIdWithRelations(sourceBaseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transfer> getTransfersByDestinationBase(Long destinationBaseId) {
        return transferRepository.findByDestinationBaseIdWithRelations(destinationBaseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transfer> getTransfersByStatus(TransferStatus status) {
        return transferRepository.findByStatusWithRelations(status);
    }
}
