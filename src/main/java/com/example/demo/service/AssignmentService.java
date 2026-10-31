package com.example.demo.service;

import com.example.demo.entity.Assignment;
import com.example.demo.enums.AssignmentStatus;
import java.time.LocalDate;
import java.util.List;

public interface AssignmentService {
    Assignment createAssignment(Long personnelId, Long equipmentTypeId, Long baseId,
                                Integer quantity, LocalDate assignmentDate, Long userId);
    Assignment updateAssignmentStatus(Long assignmentId, AssignmentStatus status);
    Assignment getAssignmentById(Long id);
    List<Assignment> getAllAssignments();
    List<Assignment> getAssignmentsByBase(Long baseId);
    List<Assignment> getAssignmentsByPersonnel(Long personnelId);
    List<Assignment> getAssignmentsByStatus(AssignmentStatus status);
}
