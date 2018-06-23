package com.comunityapp.converter;

import com.comunityapp.domain.Report;
import com.comunityapp.dto.ReportDto;
import com.comunityapp.util.mapper.ReportCategoryMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Base64;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportConverter.class);

    public static Report convertFromDto(ReportDto reportDto) {
        LOGGER.info("convertFromDto called:{}", reportDto);
        Report report = new Report();
        report.setCategory(ReportCategoryMapper.mapStringToReportCategory(reportDto.getCategory()));
        report.setXCoordinate(reportDto.getXCoordinate());
        report.setYCoordinate(reportDto.getYCoordinate());
        report.setIsActive(reportDto.isActive());
        report.setImage(Base64.getDecoder().decode(reportDto.getImage()));
        report.setDate(LocalDate.parse(reportDto.getDate()));
        report.setDetails(reportDto.getDetails());

        if (reportDto.getId() != null) {
            report.setId(reportDto.getId());
        }
        LOGGER.info("convertFromDto result:{}", report);
        return report;
    }

    public static ReportDto convertToDto(Report report) {
        LOGGER.info("convertToDto called:{}", report);

        ReportDto reportDto = null;
        try {
            reportDto = ReportDto.builder()
                    .category(ReportCategoryMapper.mapReportCategoryToString(report.getCategory()))
                    .xCoordinate(report.getXCoordinate())
                    .yCoordinate(report.getYCoordinate())
                    .details(report.getDetails())
                    .isActive(report.getIsActive())
                    .id(report.getId())
                    .date(report.getDate().toString())
                    .build();
            if (report.getImage() != null) {
                reportDto.setImage(new String(Base64.getEncoder().encode(report.getImage()), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        LOGGER.info("convertToDto result:{}", report);
        return reportDto;
    }
}
