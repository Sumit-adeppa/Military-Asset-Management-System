package com.example.demo.repository;

import com.example.demo.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByBaseIdAndEquipmentTypeId(Long baseId, Long equipmentTypeId);
    List<Inventory> findByBaseId(Long baseId);
    List<Inventory> findByEquipmentTypeId(Long equipmentTypeId);

    @Query("SELECT i FROM Inventory i WHERE i.base.id = :baseId AND i.equipmentType.id = :equipmentTypeId")
    Optional<Inventory> findByBaseAndEquipment(@Param("baseId") Long baseId, @Param("equipmentTypeId") Long equipmentTypeId);

    @Query("SELECT i FROM Inventory i LEFT JOIN FETCH i.base LEFT JOIN FETCH i.equipmentType")
    List<Inventory> findAllWithRelations();

    @Query("SELECT i FROM Inventory i LEFT JOIN FETCH i.base LEFT JOIN FETCH i.equipmentType WHERE i.base.id = :baseId")
    List<Inventory> findByBaseIdWithRelations(@Param("baseId") Long baseId);

    @Query("SELECT i FROM Inventory i LEFT JOIN FETCH i.base LEFT JOIN FETCH i.equipmentType WHERE i.equipmentType.id = :equipmentTypeId")
    List<Inventory> findByEquipmentTypeIdWithRelations(@Param("equipmentTypeId") Long equipmentTypeId);

    @Query("SELECT i FROM Inventory i LEFT JOIN FETCH i.base LEFT JOIN FETCH i.equipmentType WHERE i.base.id = :baseId AND i.equipmentType.id = :equipmentTypeId")
    Optional<Inventory> findByBaseAndEquipmentWithRelations(@Param("baseId") Long baseId, @Param("equipmentTypeId") Long equipmentTypeId);
}
