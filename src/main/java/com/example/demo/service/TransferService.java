package com.example.demo.service;

import com.example.demo.entity.Transfer;
import com.example.demo.enums.TransferStatus;
import java.time.LocalDateTime;
import java.util.List;

public interface TransferService {
    Transfer initiateTransfer(Long sourceBaseId, Long destinationBaseId, Long equipmentTypeId,
                              Integer quantity, LocalDateTime transferDate, Long userId);
    Transfer approveTransfer(Long transferId, Long userId);
    Transfer completeTransfer(Long transferId, Long userId);
    Transfer rejectTransfer(Long transferId, Long userId);
    Transfer cancelTransfer(Long transferId, Long userId);
    Transfer getTransferById(Long id);
    List<Transfer> getAllTransfers();
    List<Transfer> getTransfersBySourceBase(Long sourceBaseId);
    List<Transfer> getTransfersByDestinationBase(Long destinationBaseId);
    List<Transfer> getTransfersByStatus(TransferStatus status);
}
