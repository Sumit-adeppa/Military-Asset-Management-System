package com.example.demo.controller;

import com.example.demo.dto.request.AssignmentRequest;
import com.example.demo.security.AuthContext;
import com.example.demo.dto.response.AssignmentResponse;
import com.example.demo.entity.Assignment;
import com.example.demo.enums.AssignmentStatus;
import com.example.demo.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<AssignmentResponse> createAssignment(@Valid @RequestBody AssignmentRequest request) {
        Assignment assignment = assignmentService.createAssignment(
                request.getPersonnelId(),
                request.getEquipmentTypeId(),
                request.getBaseId(),
                request.getQuantity(),
                request.getAssignmentDate(),
                AuthContext.getUserId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(assignment));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AssignmentResponse> updateAssignmentStatus(
            @PathVariable Long id,
            @RequestParam AssignmentStatus status) {
        Assignment assignment = assignmentService.updateAssignmentStatus(id, status);
        return ResponseEntity.ok(toResponse(assignment));
    }

    @GetMapping
    public ResponseEntity<List<AssignmentResponse>> getAssignments(
            @RequestParam(required = false) Long baseId,
            @RequestParam(required = false) Long personnelId,
            @RequestParam(required = false) AssignmentStatus status) {
        List<Assignment> assignments;
        if (baseId != null) {
            assignments = assignmentService.getAssignmentsByBase(baseId);
        } else if (personnelId != null) {
            assignments = assignmentService.getAssignmentsByPersonnel(personnelId);
        } else if (status != null) {
            assignments = assignmentService.getAssignmentsByStatus(status);
        } else {
            assignments = assignmentService.getAllAssignments();
        }
        return ResponseEntity.ok(assignments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentResponse> getAssignmentById(@PathVariable Long id) {
        Assignment assignment = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(toResponse(assignment));
    }

    private AssignmentResponse toResponse(Assignment assignment) {
        return new AssignmentResponse(
                assignment.getId(),
                assignment.getPersonnel().getId(),
                assignment.getPersonnel().getName(),
                assignment.getPersonnel().getRank(),
                assignment.getPersonnel().getServiceNumber(),
                assignment.getEquipmentType().getId(),
                assignment.getEquipmentType().getName(),
                assignment.getBase().getId(),
                assignment.getBase().getName(),
                assignment.getQuantity(),
                assignment.getAssignmentDate(),
                assignment.getStatus(),
                assignment.getUser().getId(),
                assignment.getUser().getUsername()
        );
    }
}
