package com.example.demo.repository;

import com.example.demo.entity.Transfer;
import com.example.demo.enums.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findBySourceBaseId(Long sourceBaseId);
    List<Transfer> findByDestinationBaseId(Long destinationBaseId);
    List<Transfer> findByStatus(TransferStatus status);
    List<Transfer> findByUserId(Long userId);
    List<Transfer> findByTransferDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Transfer> findBySourceBaseIdOrDestinationBaseId(Long sourceBaseId, Long destinationBaseId);
    List<Transfer> findByEquipmentTypeId(Long equipmentTypeId);

    @Query("SELECT t FROM Transfer t LEFT JOIN FETCH t.sourceBase LEFT JOIN FETCH t.destinationBase LEFT JOIN FETCH t.equipmentType LEFT JOIN FETCH t.user")
    List<Transfer> findAllWithRelations();

    @Query("SELECT t FROM Transfer t LEFT JOIN FETCH t.sourceBase LEFT JOIN FETCH t.destinationBase LEFT JOIN FETCH t.equipmentType LEFT JOIN FETCH t.user WHERE t.sourceBase.id = :sourceBaseId")
    List<Transfer> findBySourceBaseIdWithRelations(@Param("sourceBaseId") Long sourceBaseId);

    @Query("SELECT t FROM Transfer t LEFT JOIN FETCH t.sourceBase LEFT JOIN FETCH t.destinationBase LEFT JOIN FETCH t.equipmentType LEFT JOIN FETCH t.user WHERE t.destinationBase.id = :destinationBaseId")
    List<Transfer> findByDestinationBaseIdWithRelations(@Param("destinationBaseId") Long destinationBaseId);

    @Query("SELECT t FROM Transfer t LEFT JOIN FETCH t.sourceBase LEFT JOIN FETCH t.destinationBase LEFT JOIN FETCH t.equipmentType LEFT JOIN FETCH t.user WHERE t.status = :status")
    List<Transfer> findByStatusWithRelations(@Param("status") TransferStatus status);

    @Query("SELECT t FROM Transfer t LEFT JOIN FETCH t.sourceBase LEFT JOIN FETCH t.destinationBase LEFT JOIN FETCH t.equipmentType LEFT JOIN FETCH t.user WHERE t.id = :id")
    Transfer findByIdWithRelations(@Param("id") Long id);
}
