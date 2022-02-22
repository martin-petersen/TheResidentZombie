package com.trz.service;

import com.trz.dto.ReportZombieDTO;
import com.trz.exception.BusinessException;
import com.trz.exception.EntityNotFoundException;
import com.trz.model.Report;
import com.trz.model.Survivor;
import com.trz.repository.ReportRepository;
import com.trz.repository.SurvivorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ReportServiceTest {

    private ReportService reportService;

    @Mock
    private SurvivorRepository survivorRepository;
    @Mock
    private ReportRepository reportRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportService = new ReportService(survivorRepository, reportRepository);
    }

    @Test
    void should_Not_Allow_Self_Report() {
        UUID survivor = UUID.randomUUID();
        Exception exception = assertThrows(BusinessException.class, () -> {
            reportService.reportInfectedSurvivor(survivor, survivor);
        });
        assertEquals("you can't report yourself", exception.getMessage());
    }

    @Test
    void should_Throw_EntityNotFound_When_Missing_A_Survivor() {
        UUID survivor1Id = UUID.randomUUID();
        UUID survivor2Id = UUID.randomUUID();
        when(survivorRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            reportService.reportInfectedSurvivor(survivor1Id, survivor2Id);
        });

        assertEquals("survivor not found in database", exception.getMessage());
    }

    @Test
    void should_Not_Allow_More_Than_One_Report_By_Same_Person() {
        Survivor reportedSurvivor = new Survivor();
        reportedSurvivor.setId(UUID.randomUUID());
        Survivor reporterSurvivor = new Survivor();
        reporterSurvivor.setId(UUID.randomUUID());
        Report report = new Report();
        report.setId(UUID.randomUUID());
        report.setReporter(reporterSurvivor);
        report.setInfected(reportedSurvivor);
        List<Report> reports = new ArrayList<>();
        reports.add(report);
        when(survivorRepository.findById(eq(reporterSurvivor.getId()))).thenReturn(Optional.of(reporterSurvivor));
        when(survivorRepository.findById(eq(reportedSurvivor.getId()))).thenReturn(Optional.of(reportedSurvivor));
        when(reportRepository.findByInfected(reportedSurvivor)).thenReturn(reports);

        Exception exception = assertThrows(BusinessException.class, () -> {
            reportService.reportInfectedSurvivor(reporterSurvivor.getId(), reportedSurvivor.getId());
        });

        assertEquals("you already report mark this person as a zombie", exception.getMessage());
    }

    @Test
    void should_Assert_ReportZombieDTO_With_One_Time_Report_Message() {
        Survivor reportedSurvivor = new Survivor();
        reportedSurvivor.setId(UUID.randomUUID());
        Survivor reporterSurvivor = new Survivor();
        reporterSurvivor.setId(UUID.randomUUID());
        List<Report> reports = new ArrayList<>();
        when(survivorRepository.findById(eq(reporterSurvivor.getId()))).thenReturn(Optional.of(reporterSurvivor));
        when(survivorRepository.findById(eq(reportedSurvivor.getId()))).thenReturn(Optional.of(reportedSurvivor));
        when(reportRepository.findByInfected(eq(reportedSurvivor))).thenReturn(reports);

        ReportZombieDTO reportZombieDTO = reportService.reportInfectedSurvivor(reporterSurvivor.getId(), reportedSurvivor.getId());

        assertEquals("this survivor has been marked as a zombie 1 times", reportZombieDTO.getMessage());
    }

    @Test
    void should_Assert_ReportZombieDTO_With_Infected_Message_And_Change_Survivor_Status_As_Well() {
        Survivor reportedSurvivor = new Survivor();
        reportedSurvivor.setId(UUID.randomUUID());
        Survivor survivor = new Survivor();
        survivor.setId(UUID.randomUUID());
        List<Report> reports = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            Survivor reporterSurvivor = new Survivor();
            reporterSurvivor.setId(UUID.randomUUID());
            Report report = new Report();
            report.setId(UUID.randomUUID());
            report.setReporter(reporterSurvivor);
            report.setInfected(reportedSurvivor);
            reports.add(report);
        }
        when(survivorRepository.findById(eq(survivor.getId()))).thenReturn(Optional.of(survivor));
        when(survivorRepository.findById(eq(reportedSurvivor.getId()))).thenReturn(Optional.of(reportedSurvivor));
        when(reportRepository.findByInfected(reportedSurvivor)).thenReturn(reports);
        when(survivorRepository.save(eq(reportedSurvivor))).thenReturn(reportedSurvivor);

        ReportZombieDTO reportZombieDTO = reportService.reportInfectedSurvivor(survivor.getId(), reportedSurvivor.getId());

        assertEquals("this survivor is on the dead side now, don`t hangout with him", reportZombieDTO.getMessage());
        assertTrue(reportedSurvivor.isInfected());
    }
}