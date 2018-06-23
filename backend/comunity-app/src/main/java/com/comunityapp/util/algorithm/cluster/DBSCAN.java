package com.comunityapp.util.algorithm.cluster;

import com.comunityapp.domain.Report;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DBSCAN {
    private static final Integer EPSILON = 10;
    private static final Integer MINIMUM_POINTS = 2;
    private static final Integer EARTH_RADIUS_M = 673;

    public static List<Set<Report>> solve(List<Report> reportList) {
        List<Set<Report>> resultList = new ArrayList<>();
        HashSet<Report> visited = new HashSet<>();

        for (Report report : reportList) {
            if (!visited.contains(report)) {
                visited.add(report);
                Set<Report> neighbours = rangeQuery(report, reportList);

                if (neighbours.size() >= MINIMUM_POINTS) {
                    for (Report report1 : neighbours) {
                        if (!visited.contains(report1)) {
                            visited.add(report1);
                            Set<Report> individualNeighbours = rangeQuery(report1, reportList);
                            if (individualNeighbours.size() >= MINIMUM_POINTS) {
                                neighbours.addAll(individualNeighbours);
                            }
                        }
                    }
                    resultList.add(neighbours);
                }
            }
        }
        return resultList;
    }

    private static Set<Report> rangeQuery(Report report, List<Report> reports) {
        Set<Report> neighbours = new TreeSet<>(Comparator.comparing(Report::getId));
        reports.forEach(report1 -> {
            if (distFunction(report, report1) <= EPSILON) {
                neighbours.add(report1);
            }
        });
        return neighbours;
    }

    private static int distFunction(Report report1, Report report2) {
        double x = (report1.getXCoordinate() - report2.getXCoordinate()) * (report1.getXCoordinate() - report2.getXCoordinate());
        double y = (report1.getYCoordinate() - report2.getYCoordinate()) * (report1.getYCoordinate() - report2.getYCoordinate());
        Double res = Math.sqrt(x + y) * EARTH_RADIUS_M;
        return res.intValue();
    }
}
