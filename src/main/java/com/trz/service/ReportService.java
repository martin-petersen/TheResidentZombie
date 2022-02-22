package com.trz.service;

import com.trz.config.Constants;
import com.trz.dto.ReportZombieDTO;
import com.trz.exception.BusinessException;
import com.trz.exception.EntityNotFoundException;
import com.trz.model.Report;
import com.trz.model.Survivor;
import com.trz.repository.ReportRepository;
import com.trz.repository.SurvivorRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class ReportService {

    private final SurvivorRepository survivorRepository;

    private final ReportRepository reportRepository;

    public ReportService(SurvivorRepository survivorRepository, ReportRepository reportRepository) {
        this.survivorRepository = survivorRepository;
        this.reportRepository = reportRepository;
    }

    @Transactional
    public ReportZombieDTO reportInfectedSurvivor(UUID id, UUID reportedAsZombieId) {
        if (id.equals(reportedAsZombieId)) {
            throw new BusinessException(Constants.SURVIVOR_SELF_REPORT_NOT_ALLOWED);
        }
        Survivor reporter = survivorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("survivor not found in database"));
        Survivor reported = survivorRepository.findById(reportedAsZombieId).orElseThrow(() -> new EntityNotFoundException("survivor not found in database"));

        List<Report> reports = reportRepository.findByInfected(reported);
        Map<UUID, Report> reportMap = new HashMap<>();
        reports.forEach(report -> reportMap.put(report.getReporter().getId(), report));
        Report newReport = new Report();
        if (reportMap.containsKey(reporter.getId()))
            throw new BusinessException(Constants.DOUBLE_REPORT_NOT_ALLOWED);
        else {
            newReport.setId(reportRepository.getNextId());
            newReport.setInfected(reported);
            newReport.setReporter(reporter);
            reportRepository.save(newReport);
            reportMap.put(newReport.getReporter().getId(), newReport);
        }
        reports = new ArrayList<>(reportMap.values());
        if (reports.size() < 5) {
            return new ReportZombieDTO(newReport.getId(), String.format(Constants.SURVIVOR_REPORTS, reports.size()));
        } else {
            reported.setInfected(true);
            survivorRepository.save(reported);
            return new ReportZombieDTO(newReport.getId(), Constants.SURVIVOR_IS_A_ZOMBIE);
        }
    }
}
