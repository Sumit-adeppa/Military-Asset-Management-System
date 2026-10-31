package com.example.demo.service;

import com.example.demo.entity.EquipmentType;
import com.example.demo.enums.SerializationType;
import java.util.List;

public interface EquipmentTypeService {
    EquipmentType createEquipmentType(String name, String category, String description, SerializationType serializationType);
    EquipmentType getEquipmentTypeById(Long id);
    List<EquipmentType> getAllEquipmentTypes();
    EquipmentType updateEquipmentType(Long id, String name, String category, String description, SerializationType serializationType);
    void deleteEquipmentType(Long id);
}
