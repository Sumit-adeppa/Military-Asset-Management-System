package com.example.demo.controller;

import com.example.demo.dto.request.ExpenditureRequest;
import com.example.demo.security.AuthContext;
import com.example.demo.dto.response.ExpenditureResponse;
import com.example.demo.entity.Expenditure;
import com.example.demo.service.ExpenditureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expenditures")
public class ExpenditureController {

    private final ExpenditureService expenditureService;

    public ExpenditureController(ExpenditureService expenditureService) {
        this.expenditureService = expenditureService;
    }

    @PostMapping
    public ResponseEntity<ExpenditureResponse> recordExpenditure(@Valid @RequestBody ExpenditureRequest request) {
        Expenditure expenditure = expenditureService.recordExpenditure(
                request.getEquipmentTypeId(),
                request.getBaseId(),
                request.getQuantity(),
                request.getExpenditureDate(),
                request.getReason(),
                AuthContext.getUserId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(expenditure));
    }

    @GetMapping
    public ResponseEntity<List<ExpenditureResponse>> getExpenditures(
            @RequestParam(required = false) Long baseId,
            @RequestParam(required = false) Long equipmentTypeId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        List<Expenditure> expenditures;
        if (startDate != null && endDate != null) {
            expenditures = expenditureService.getExpendituresByDateRange(startDate, endDate);
        } else if (baseId != null) {
            expenditures = expenditureService.getExpendituresByBase(baseId);
        } else if (equipmentTypeId != null) {
            expenditures = expenditureService.getExpendituresByEquipmentType(equipmentTypeId);
        } else {
            expenditures = expenditureService.getAllExpenditures();
        }
        return ResponseEntity.ok(expenditures.stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenditureResponse> getExpenditureById(@PathVariable Long id) {
        Expenditure expenditure = expenditureService.getExpenditureById(id);
        return ResponseEntity.ok(toResponse(expenditure));
    }

    private ExpenditureResponse toResponse(Expenditure expenditure) {
        return new ExpenditureResponse(
                expenditure.getId(),
                expenditure.getEquipmentType().getId(),
                expenditure.getEquipmentType().getName(),
                expenditure.getBase().getId(),
                expenditure.getBase().getName(),
                expenditure.getQuantity(),
                expenditure.getExpenditureDate(),
                expenditure.getReason(),
                expenditure.getUser().getId(),
                expenditure.getUser().getUsername()
        );
    }
}
