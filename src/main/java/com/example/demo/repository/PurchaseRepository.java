package com.example.demo.repository;

import com.example.demo.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByBaseId(Long baseId);
    List<Purchase> findByEquipmentTypeId(Long equipmentTypeId);
    List<Purchase> findByBaseIdAndEquipmentTypeId(Long baseId, Long equipmentTypeId);
    List<Purchase> findByPurchaseDateBetween(LocalDate startDate, LocalDate endDate);
    List<Purchase> findByBaseIdAndPurchaseDateBetween(Long baseId, LocalDate startDate, LocalDate endDate);
    List<Purchase> findByUserId(Long userId);

    @Query("SELECT p FROM Purchase p LEFT JOIN FETCH p.base LEFT JOIN FETCH p.equipmentType LEFT JOIN FETCH p.user")
    List<Purchase> findAllWithRelations();

    @Query("SELECT p FROM Purchase p LEFT JOIN FETCH p.base LEFT JOIN FETCH p.equipmentType LEFT JOIN FETCH p.user WHERE p.base.id = :baseId")
    List<Purchase> findByBaseIdWithRelations(@Param("baseId") Long baseId);

    @Query("SELECT p FROM Purchase p LEFT JOIN FETCH p.base LEFT JOIN FETCH p.equipmentType LEFT JOIN FETCH p.user WHERE p.equipmentType.id = :equipmentTypeId")
    List<Purchase> findByEquipmentTypeIdWithRelations(@Param("equipmentTypeId") Long equipmentTypeId);

    @Query("SELECT p FROM Purchase p LEFT JOIN FETCH p.base LEFT JOIN FETCH p.equipmentType LEFT JOIN FETCH p.user WHERE p.purchaseDate BETWEEN :startDate AND :endDate")
    List<Purchase> findByPurchaseDateBetweenWithRelations(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT p FROM Purchase p LEFT JOIN FETCH p.base LEFT JOIN FETCH p.equipmentType LEFT JOIN FETCH p.user WHERE p.id = :id")
    Purchase findByIdWithRelations(@Param("id") Long id);
}
