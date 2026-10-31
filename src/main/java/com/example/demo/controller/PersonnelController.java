package com.example.demo.controller;

import com.example.demo.dto.request.PersonnelRequest;
import com.example.demo.dto.response.PersonnelResponse;
import com.example.demo.entity.Personnel;
import com.example.demo.service.PersonnelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personnel")
public class PersonnelController {

    private final PersonnelService personnelService;

    public PersonnelController(PersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    @PostMapping
    public ResponseEntity<PersonnelResponse> createPersonnel(@Valid @RequestBody PersonnelRequest request) {
        Personnel personnel = personnelService.createPersonnel(
                request.getName(),
                request.getRank(),
                request.getServiceNumber(),
                request.getBaseId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(personnel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonnelResponse> getPersonnelById(@PathVariable Long id) {
        Personnel personnel = personnelService.getPersonnelById(id);
        return ResponseEntity.ok(toResponse(personnel));
    }

    @GetMapping
    public ResponseEntity<List<PersonnelResponse>> getAllPersonnel(
            @RequestParam(required = false) Long baseId) {
        List<Personnel> personnel = baseId != null
                ? personnelService.getPersonnelByBase(baseId)
                : personnelService.getAllPersonnel();
        return ResponseEntity.ok(personnel.stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonnelResponse> updatePersonnel(@PathVariable Long id,
                                                             @RequestBody PersonnelRequest request) {
        Personnel personnel = personnelService.updatePersonnel(
                id, request.getName(), request.getRank(), request.getBaseId());
        return ResponseEntity.ok(toResponse(personnel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonnel(@PathVariable Long id) {
        personnelService.deletePersonnel(id);
        return ResponseEntity.noContent().build();
    }

    private PersonnelResponse toResponse(Personnel personnel) {
        return new PersonnelResponse(
                personnel.getId(),
                personnel.getName(),
                personnel.getRank(),
                personnel.getServiceNumber(),
                personnel.getBase() != null ? personnel.getBase().getId() : null,
                personnel.getBase() != null ? personnel.getBase().getName() : null
        );
    }
}
