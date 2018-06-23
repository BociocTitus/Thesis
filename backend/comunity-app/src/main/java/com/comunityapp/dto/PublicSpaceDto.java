package com.comunityapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString(exclude = "schedules")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicSpaceDto {
    private long id;
    private String name;
    @JsonProperty
    private double xCoordinate;
    @JsonProperty
    private double yCoordinate;
    private String address;
    private String details;
    private String activityCategory;
    private int capacity;
    @JsonProperty
    private List<ScheduleDto> schedules;
}
