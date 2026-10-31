package com.example.demo.controller;

import com.example.demo.dto.request.EquipmentTypeRequest;
import com.example.demo.dto.response.EquipmentTypeResponse;
import com.example.demo.entity.EquipmentType;
import com.example.demo.service.EquipmentTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipment-types")
public class EquipmentTypeController {

    private final EquipmentTypeService equipmentTypeService;

    public EquipmentTypeController(EquipmentTypeService equipmentTypeService) {
        this.equipmentTypeService = equipmentTypeService;
    }

    @PostMapping
    public ResponseEntity<EquipmentTypeResponse> createEquipmentType(
            @Valid @RequestBody EquipmentTypeRequest request) {
        EquipmentType et = equipmentTypeService.createEquipmentType(
                request.getName(),
                request.getCategory(),
                request.getDescription(),
                request.getSerializationType()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(et));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentTypeResponse> getEquipmentTypeById(@PathVariable Long id) {
        EquipmentType et = equipmentTypeService.getEquipmentTypeById(id);
        return ResponseEntity.ok(toResponse(et));
    }

    @GetMapping
    public ResponseEntity<List<EquipmentTypeResponse>> getAllEquipmentTypes() {
        return ResponseEntity.ok(equipmentTypeService.getAllEquipmentTypes().stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentTypeResponse> updateEquipmentType(
            @PathVariable Long id,
            @RequestBody EquipmentTypeRequest request) {
        EquipmentType et = equipmentTypeService.updateEquipmentType(
                id,
                request.getName(),
                request.getCategory(),
                request.getDescription(),
                request.getSerializationType()
        );
        return ResponseEntity.ok(toResponse(et));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipmentType(@PathVariable Long id) {
        equipmentTypeService.deleteEquipmentType(id);
        return ResponseEntity.noContent().build();
    }

    private EquipmentTypeResponse toResponse(EquipmentType et) {
        return new EquipmentTypeResponse(
                et.getId(),
                et.getName(),
                et.getCategory(),
                et.getDescription(),
                et.getSerializationType()
        );
    }
}
