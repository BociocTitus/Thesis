package com.comunityapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReportDto {
    private Long id;
    private String details;
    private String category;
    @JsonProperty
    private double xCoordinate;
    @JsonProperty
    private double yCoordinate;
    private String image;
    @JsonProperty
    private boolean isActive;
    private String date;

    @Override
    public String toString() {
        return "Report(" + id + " " + details + " " + category + " " + xCoordinate + " " + yCoordinate + " " + isActive + " " + date + ")";
    }
}
