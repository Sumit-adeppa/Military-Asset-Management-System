package com.example.demo.repository;

import com.example.demo.entity.EquipmentType;
import com.example.demo.enums.SerializationType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EquipmentTypeRepository extends JpaRepository<EquipmentType, Long> {
    Optional<EquipmentType> findByName(String name);
    boolean existsByName(String name);
    List<EquipmentType> findByCategory(String category);
    List<EquipmentType> findBySerializationType(SerializationType serializationType);
}
