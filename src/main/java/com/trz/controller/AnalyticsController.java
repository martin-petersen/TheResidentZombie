package com.trz.controller;

import com.trz.dto.AnalyticsDTO;
import com.trz.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ResponseEntity<AnalyticsDTO> getStatistics() {
        AnalyticsDTO analyticsDTO = analyticsService.analytics();
        return ResponseEntity.ok(analyticsDTO);
    }
}
