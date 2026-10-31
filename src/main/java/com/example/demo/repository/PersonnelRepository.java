package com.example.demo.repository;

import com.example.demo.entity.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface PersonnelRepository extends JpaRepository<Personnel, Long> {
    Optional<Personnel> findByServiceNumber(String serviceNumber);
    boolean existsByServiceNumber(String serviceNumber);
    List<Personnel> findByBaseId(Long baseId);

    @Query("SELECT p FROM Personnel p LEFT JOIN FETCH p.base")
    List<Personnel> findAllWithBase();
}
