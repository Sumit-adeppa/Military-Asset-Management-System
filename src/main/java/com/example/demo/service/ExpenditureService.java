package com.example.demo.service;

import com.example.demo.entity.Expenditure;
import java.time.LocalDate;
import java.util.List;

public interface ExpenditureService {
    Expenditure recordExpenditure(Long equipmentTypeId, Long baseId, Integer quantity,
                                 LocalDate expenditureDate, String reason, Long userId);
    Expenditure getExpenditureById(Long id);
    List<Expenditure> getAllExpenditures();
    List<Expenditure> getExpendituresByBase(Long baseId);
    List<Expenditure> getExpendituresByEquipmentType(Long equipmentTypeId);
    List<Expenditure> getExpendituresByDateRange(LocalDate startDate, LocalDate endDate);
}
