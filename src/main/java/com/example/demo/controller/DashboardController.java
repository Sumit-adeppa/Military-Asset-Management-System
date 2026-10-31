package com.example.demo.controller;

import com.example.demo.dto.response.DashboardResponse;
import com.example.demo.service.DashboardService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboardData(
            @RequestParam(required = false) Long baseId,
            @RequestParam(required = false) Long equipmentTypeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> data = dashboardService.getDashboardData(baseId, equipmentTypeId, startDate, endDate);
        DashboardResponse response = new DashboardResponse(
                (Integer) data.get("openingBalance"),
                (Integer) data.get("purchases"),
                (Integer) data.get("transferIn"),
                (Integer) data.get("transferOut"),
                (Integer) data.get("netMovement"),
                (Integer) data.get("assigned"),
                (Integer) data.get("expended"),
                (Integer) data.get("closingBalance")
        );
        return ResponseEntity.ok(response);
    }
}
