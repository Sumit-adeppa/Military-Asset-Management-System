package com.example.demo.service.impl;

import com.example.demo.entity.EquipmentType;
import com.example.demo.enums.SerializationType;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EquipmentTypeRepository;
import com.example.demo.service.EquipmentTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipmentTypeServiceImpl implements EquipmentTypeService {

    private final EquipmentTypeRepository equipmentTypeRepository;

    public EquipmentTypeServiceImpl(EquipmentTypeRepository equipmentTypeRepository) {
        this.equipmentTypeRepository = equipmentTypeRepository;
    }

    @Override
    @Transactional
    public EquipmentType createEquipmentType(String name, String category, String description,
                                             SerializationType serializationType) {
        if (equipmentTypeRepository.existsByName(name)) {
            throw new DuplicateResourceException("Equipment type already exists with name: " + name);
        }
        EquipmentType et = new EquipmentType(name, category, serializationType);
        et.setDescription(description);
        return equipmentTypeRepository.save(et);
    }

    @Override
    @Transactional(readOnly = true)
    public EquipmentType getEquipmentTypeById(Long id) {
        return equipmentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment type not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentType> getAllEquipmentTypes() {
        return equipmentTypeRepository.findAll();
    }

    @Override
    @Transactional
    public EquipmentType updateEquipmentType(Long id, String name, String category,
                                             String description, SerializationType serializationType) {
        EquipmentType et = equipmentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment type not found with id: " + id));
        if (!et.getName().equals(name) && equipmentTypeRepository.existsByName(name)) {
            throw new DuplicateResourceException("Equipment type already exists with name: " + name);
        }
        et.setName(name);
        et.setCategory(category);
        et.setDescription(description);
        et.setSerializationType(serializationType);
        return equipmentTypeRepository.save(et);
    }

    @Override
    @Transactional
    public void deleteEquipmentType(Long id) {
        if (!equipmentTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipment type not found with id: " + id);
        }
        equipmentTypeRepository.deleteById(id);
    }
}
