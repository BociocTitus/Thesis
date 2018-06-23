package com.comunityapp.util.algorithm.genetic;

import com.comunityapp.domain.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class Location {
    private Long id;
    private double xCoordinate;
    private double yCoordinate;
    private Report report;
    private static final Integer EARTH_RADIUS_KM = 6371;


    public double distanceTo(Location location) {
        double xDistance = Math.abs(xCoordinate - location.getXCoordinate());
        double yDistance = Math.abs(yCoordinate - location.getYCoordinate());
        return Math.sqrt((xDistance * xDistance) + (yDistance * yDistance)) * EARTH_RADIUS_KM;
    }
}
