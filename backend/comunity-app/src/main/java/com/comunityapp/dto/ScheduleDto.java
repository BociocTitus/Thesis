package com.comunityapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleDto {
    private String begin;
    private String end;
    private List<ScheduleJoinDto> scheduleJoinDtoList;
    private Boolean isFull;
}
