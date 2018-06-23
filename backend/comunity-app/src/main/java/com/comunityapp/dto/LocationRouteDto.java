package com.comunityapp.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LocationRouteDto {
    private List<ReportDto> reportDtoList;
}
