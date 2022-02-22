package com.trz.controller;

import com.trz.dto.AnalyticsDTO;
import com.trz.dto.ReportZombieDTO;
import com.trz.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/{id}/denunciation/{reportedAsZombieId}")
    public ResponseEntity<ReportZombieDTO> reportInfected(@PathVariable UUID id,
                                                          @PathVariable UUID reportedAsZombieId,
                                                          UriComponentsBuilder uriComponentsBuilder) {
        ReportZombieDTO reportZombieDTO = reportService.reportInfectedSurvivor(
                id,
                reportedAsZombieId
        );
        URI uri = uriComponentsBuilder.path("/reports/{id}").buildAndExpand(reportZombieDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(reportZombieDTO);
    }
}
