package com.example.demo.repository;

import com.example.demo.entity.Assignment;
import com.example.demo.enums.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByPersonnelId(Long personnelId);
    List<Assignment> findByBaseId(Long baseId);
    List<Assignment> findByEquipmentTypeId(Long equipmentTypeId);
    List<Assignment> findByStatus(AssignmentStatus status);
    List<Assignment> findByUserId(Long userId);
    List<Assignment> findByAssignmentDateBetween(LocalDate startDate, LocalDate endDate);
    List<Assignment> findByBaseIdAndStatus(Long baseId, AssignmentStatus status);

    @Query("SELECT a FROM Assignment a LEFT JOIN FETCH a.personnel LEFT JOIN FETCH a.equipmentType LEFT JOIN FETCH a.base LEFT JOIN FETCH a.user")
    List<Assignment> findAllWithRelations();

    @Query("SELECT a FROM Assignment a LEFT JOIN FETCH a.personnel LEFT JOIN FETCH a.equipmentType LEFT JOIN FETCH a.base LEFT JOIN FETCH a.user WHERE a.personnel.id = :personnelId")
    List<Assignment> findByPersonnelIdWithRelations(@Param("personnelId") Long personnelId);

    @Query("SELECT a FROM Assignment a LEFT JOIN FETCH a.personnel LEFT JOIN FETCH a.equipmentType LEFT JOIN FETCH a.base LEFT JOIN FETCH a.user WHERE a.base.id = :baseId")
    List<Assignment> findByBaseIdWithRelations(@Param("baseId") Long baseId);

    @Query("SELECT a FROM Assignment a LEFT JOIN FETCH a.personnel LEFT JOIN FETCH a.equipmentType LEFT JOIN FETCH a.base LEFT JOIN FETCH a.user WHERE a.status = :status")
    List<Assignment> findByStatusWithRelations(@Param("status") AssignmentStatus status);

    @Query("SELECT a FROM Assignment a LEFT JOIN FETCH a.personnel LEFT JOIN FETCH a.equipmentType LEFT JOIN FETCH a.base LEFT JOIN FETCH a.user WHERE a.id = :id")
    Assignment findByIdWithRelations(@Param("id") Long id);
}
