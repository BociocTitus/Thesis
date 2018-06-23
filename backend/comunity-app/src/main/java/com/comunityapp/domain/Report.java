package com.comunityapp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity(name = "reports")
@Table
@Data
@NoArgsConstructor
@ToString(exclude = "image")
public class Report extends BaseEntity<Long> {
    private double xCoordinate;
    private double yCoordinate;
    private String details;
    @Enumerated
    private ReportCategory category;
    private Boolean isActive;
    private byte[] image;
    private LocalDate date;
}
