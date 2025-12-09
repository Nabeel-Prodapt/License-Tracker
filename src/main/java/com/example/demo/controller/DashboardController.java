package com.example.demo.controller;

import com.example.demo.dto.DashboardOverviewDTO;
import com.example.demo.service.DashboardService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/api/dashboard/overview")
    public DashboardOverviewDTO getOverview() {
        return dashboardService.getDashboardOverview();
    }
}