package com.example.demo.repository;

import com.example.demo.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Query("SELECT a FROM AuditLog a LEFT JOIN FETCH a.user")
    List<AuditLog> findAllWithUser();

    @Query("SELECT a FROM AuditLog a LEFT JOIN FETCH a.user WHERE a.user.id = :userId")
    List<AuditLog> findByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM AuditLog a LEFT JOIN FETCH a.user WHERE a.entity = :entity")
    List<AuditLog> findByEntity(@Param("entity") String entity);

    @Query("SELECT a FROM AuditLog a LEFT JOIN FETCH a.user WHERE a.entityId = :entityId")
    List<AuditLog> findByEntityId(@Param("entityId") Long entityId);

    @Query("SELECT a FROM AuditLog a LEFT JOIN FETCH a.user WHERE a.entity = :entity AND a.entityId = :entityId")
    List<AuditLog> findByEntityAndEntityId(@Param("entity") String entity, @Param("entityId") Long entityId);

    @Query("SELECT a FROM AuditLog a LEFT JOIN FETCH a.user WHERE a.action = :action")
    List<AuditLog> findByAction(@Param("action") String action);

    @Query("SELECT a FROM AuditLog a LEFT JOIN FETCH a.user WHERE a.timestamp BETWEEN :startDate AND :endDate")
    List<AuditLog> findByTimestampBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
