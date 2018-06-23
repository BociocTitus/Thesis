package com.comunityapp.util.algorithm.genetic;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Tour {
    private List<Location> tour = new ArrayList<>();
    private int distance = 0;

    public Tour() {
    }

    public Tour(List<Location> tour) {
        this.tour = tour;
    }

    public Location getLocation(int pos) {
        return tour.get(pos);
    }

    public void setLocation(int pos, Location location) {
        tour.set(pos, location);
        distance = 0;
    }

    public double getFitness() {
        return getDistance();
    }

    public int getDistance() {
        if (distance == 0) {
            int tourDistance = 0;

            for (int i = 0; i < tour.size(); i++) {
                Location location = getLocation(i);

                Location destination;

                if (i + 1 < tour.size()) {
                    destination = getLocation(i + 1);
                } else {
                    destination = getLocation(0);
                }

                tourDistance += location.distanceTo(destination);
            }
            distance = tourDistance;
        }
        return distance;
    }

}
