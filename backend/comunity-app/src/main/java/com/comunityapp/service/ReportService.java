package com.comunityapp.service;

import com.comunityapp.domain.Report;
import com.comunityapp.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    public Optional<Report> saveReport(Report report) {
        LOGGER.info("addReport called:{}", report);

        Optional<Report> reportOptional = Optional.ofNullable(reportRepository.save(report));

        LOGGER.info("addReport result:{}", report);
        return reportOptional;
    }

    public List<Report> getReports() {
        LOGGER.info("getReports called");

        List<Report> reports = reportRepository.findAll();

        LOGGER.info("getReports result:{}", reports);
        return reports;
    }

    public List<Report> getActiveAndRecentReports() {
        LOGGER.info("getActiveAndRecentReports called");

        List<Report> reports = reportRepository.findAll().stream()
                .filter(report -> report.getIsActive() || DAYS.between(LocalDate.now(), report.getDate()) <= 2)
                .collect(Collectors.toList());

        LOGGER.info("getActiveAndRecentReports result:{}", reports);
        return reports;
    }

    public Optional<Report> getReportById(long id) {
        LOGGER.info("GetReportById:{}", id);

        Optional<Report> reportOptional = reportRepository.findById(id);

        LOGGER.info("GetReportByIdResult:{}", id);
        return reportOptional;
    }
}
