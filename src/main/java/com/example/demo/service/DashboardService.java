package com.example.demo.service;

import java.time.LocalDate;
import java.util.Map;

public interface DashboardService {
    Map<String, Object> getDashboardData(Long baseId, Long equipmentTypeId,
                                         LocalDate startDate, LocalDate endDate);
}
