package com.comunityapp.controller;

import com.comunityapp.converter.ReportConverter;
import com.comunityapp.domain.Report;
import com.comunityapp.domain.ReportCategory;
import com.comunityapp.dto.LocationRouteDto;
import com.comunityapp.dto.ReportDto;
import com.comunityapp.dto.StatisticsDto;
import com.comunityapp.service.ReportService;
import com.comunityapp.util.algorithm.cluster.DBSCAN;
import com.comunityapp.util.algorithm.genetic.GA;
import com.comunityapp.util.algorithm.genetic.Location;
import com.comunityapp.util.algorithm.genetic.Population;
import com.comunityapp.util.algorithm.genetic.Tour;
import com.comunityapp.util.mapper.ReportCategoryMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @RequestMapping(value = "/saveReport", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('NORMAL','OFFICE')")
    public ResponseEntity<Boolean> addReport(@RequestBody ReportDto reportDto) {
        LOGGER.info("addReport called:{}", reportDto);
        Report report = ReportConverter.convertFromDto(reportDto);
        Optional<Report> reportOptional = reportService.saveReport(report);
        if (reportOptional.isPresent()) {
            LOGGER.info("addReport result{}", report);
            return ResponseEntity.ok(true);
        }
        LOGGER.info("addReport some error occured");
        return ResponseEntity.ok(false);
    }

    @RequestMapping(value = "/getReports", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('NORMAL','OFFICE')")
    public ResponseEntity<List<ReportDto>> getReports() {
        LOGGER.info("getReports called");

        List<ReportDto> reportDtos = reportService.getReports()
                .stream()
                .filter(report -> report.getIsActive() || (report.getDate().compareTo(LocalDate.now().minusDays(2L)) > 0))
                .map(ReportConverter::convertToDto)
                .collect(Collectors.toList());

        LOGGER.info("getReports result:{}", reportDtos);
        return ResponseEntity.ok(reportDtos);
    }

    @RequestMapping(value = "/getActiveReports", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('NORMAL','OFFICE')")
    public ResponseEntity<List<ReportDto>> getActiveAndRecentReports() {
        LOGGER.info("getActiveAndRecentReports called");

        List<ReportDto> reportDtos = reportService.getActiveAndRecentReports()
                .stream()
                .filter(report -> report.getIsActive() || (report.getDate().compareTo(LocalDate.now().minusDays(2L)) > 0))
                .map(ReportConverter::convertToDto).collect(Collectors.toList());

        LOGGER.info("getActiveAndRecentReports result:{}", reportDtos);
        return ResponseEntity.ok(reportDtos);
    }

    @RequestMapping(value = "/closeReport/{reportId}", method = RequestMethod.PUT)
    @PreAuthorize("hasAnyRole('OFFICE')")
    public ResponseEntity<Boolean> closeReport(@PathVariable Long reportId) {
        LOGGER.info("closeReport:{}", reportId);

        Optional<Report> reportOptional = reportService.getReportById(reportId);
        if (reportOptional.isPresent()) {
            Report report = reportOptional.get();
            report.setIsActive(false);
            reportService.saveReport(report);
            return ResponseEntity.ok(Boolean.TRUE);
        }

        return ResponseEntity.ok(Boolean.FALSE);
    }

    @RequestMapping(value = "/findById/{reportId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('OFFICE')")
    public ResponseEntity<ReportDto> findById(@PathVariable Long reportId) {
        LOGGER.info("closeReport:{}", reportId);

        Optional<Report> reportOptional = reportService.getReportById(reportId);
        return reportOptional
                .map(report -> ResponseEntity.ok(ReportConverter.convertToDto(report)))
                .orElseGet(() -> ResponseEntity.ok(ReportDto.builder().id(-1L).build()));
    }

    @RequestMapping(value = "/getFilteredReports/{reportCategory}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('NORMAL','OFFICE')")
    public ResponseEntity<List<ReportDto>> getFilteredReports(@PathVariable String reportCategory) {
        LOGGER.info("getFilteredReports:{}", reportCategory);

        ReportCategory category = ReportCategoryMapper.mapStringToReportCategory(reportCategory);
        List<ReportDto> reportDtoList = reportService.getReports().stream().filter(report -> report.getCategory().equals(category))
                .filter(report -> report.getIsActive() || (report.getDate().compareTo(LocalDate.now().minusDays(2L)) > 0))
                .map(ReportConverter::convertToDto).collect(Collectors.toList());

        LOGGER.info("getFilteredReports result :{}", reportDtoList);
        return ResponseEntity.ok(reportDtoList);
    }

    @RequestMapping(value = "/programRoute/{category}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('OFFICE')")
    public ResponseEntity<LocationRouteDto> programRoute(@PathVariable String category) {
        LOGGER.info("programRoute called:{}", category);

        ReportCategory category1 = ReportCategoryMapper.mapStringToReportCategory(category);

        List<Location> toProgram = reportService.getReports().stream()
                .filter(report -> report.getIsActive() && report.getCategory().equals(category1))
                .map(report -> new Location(report.getId(), report.getXCoordinate(), report.getYCoordinate(), report))
                .collect(Collectors.toList());

        Tour tour = new Tour(toProgram);
        Population population = new Population(tour, Boolean.TRUE);

        for (int i = 0; i < 10000; i++) {
            population = GA.evolvePopulation(population);
        }
        LOGGER.info("Population size:{}", population.getTourList().size());

        population.getTourList().forEach(tour1 -> LOGGER.info(tour1.getFitness() + " " + tour1.getDistance()));
        List<ReportDto> reportDtoArrayList = population.getFittest().getTour().stream()
                .map(location -> ReportConverter.convertToDto(location.getReport()))
                .collect(Collectors.toList());
        LocationRouteDto locationRouteDto = LocationRouteDto.builder().reportDtoList(reportDtoArrayList).build();

        LOGGER.info("programRoute result:{} {} {}", population.getFittest().getDistance(), population.getFittest().getFitness(), reportDtoArrayList);
        return ResponseEntity.ok(locationRouteDto);
    }

    @RequestMapping(value = "/createReportsClusters/{category}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('OFFICE')")
    public ResponseEntity<StatisticsDto> createClusters(@PathVariable String category) {
        LOGGER.info("createClusters called:{}", category);

        ReportCategory reportCategory = ReportCategoryMapper.mapStringToReportCategory(category);
        List<Report> reports = reportService.getReports()
                .stream()
                .filter(report -> report.getCategory().equals(reportCategory))
                .collect(Collectors.toList());

        List<Set<ReportDto>> reportsClusters = DBSCAN.solve(reports)
                .stream()
                .map(reportSet -> reportSet.stream().map(ReportConverter::convertToDto).collect(Collectors.toSet()))
                .collect(Collectors.toList());

        StatisticsDto statisticsDto = new StatisticsDto(reportsClusters);

        LOGGER.info("createClusters result:{}", statisticsDto);
        return ResponseEntity.ok(statisticsDto);

    }

}