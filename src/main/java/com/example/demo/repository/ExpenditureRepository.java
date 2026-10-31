package com.example.demo.repository;

import com.example.demo.entity.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {
    List<Expenditure> findByBaseId(Long baseId);
    List<Expenditure> findByEquipmentTypeId(Long equipmentTypeId);
    List<Expenditure> findByBaseIdAndEquipmentTypeId(Long baseId, Long equipmentTypeId);
    List<Expenditure> findByExpenditureDateBetween(LocalDate startDate, LocalDate endDate);
    List<Expenditure> findByUserId(Long userId);

    @Query("SELECT e FROM Expenditure e LEFT JOIN FETCH e.equipmentType LEFT JOIN FETCH e.base LEFT JOIN FETCH e.user")
    List<Expenditure> findAllWithRelations();

    @Query("SELECT e FROM Expenditure e LEFT JOIN FETCH e.equipmentType LEFT JOIN FETCH e.base LEFT JOIN FETCH e.user WHERE e.base.id = :baseId")
    List<Expenditure> findByBaseIdWithRelations(@Param("baseId") Long baseId);

    @Query("SELECT e FROM Expenditure e LEFT JOIN FETCH e.equipmentType LEFT JOIN FETCH e.base LEFT JOIN FETCH e.user WHERE e.equipmentType.id = :equipmentTypeId")
    List<Expenditure> findByEquipmentTypeIdWithRelations(@Param("equipmentTypeId") Long equipmentTypeId);

    @Query("SELECT e FROM Expenditure e LEFT JOIN FETCH e.equipmentType LEFT JOIN FETCH e.base LEFT JOIN FETCH e.user WHERE e.expenditureDate BETWEEN :startDate AND :endDate")
    List<Expenditure> findByExpenditureDateBetweenWithRelations(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT e FROM Expenditure e LEFT JOIN FETCH e.equipmentType LEFT JOIN FETCH e.base LEFT JOIN FETCH e.user WHERE e.id = :id")
    Expenditure findByIdWithRelations(@Param("id") Long id);
}
