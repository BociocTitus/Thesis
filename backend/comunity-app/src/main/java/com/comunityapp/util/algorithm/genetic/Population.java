package com.comunityapp.util.algorithm.genetic;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
public class Population {
    private List<Tour> tourList;

    public Population(Tour tour, boolean init) {
        tourList = new ArrayList<>();
        tourList.add(tour);
        if (init) {
            for (int i = 0; i < 50; i++) {
                Tour otherTour = new Tour();
                otherTour.setTour(new ArrayList<>(tour.getTour()));
                Collections.shuffle(otherTour.getTour());
                tourList.add(otherTour);
            }
        }
    }

    public Population() {
        tourList = new ArrayList<>();
    }

    public Tour getFittest() {
        return tourList
                .stream()
                .min(Comparator.comparing(Tour::getDistance))
                .orElse(new Tour());

    }

    public int populationSize() {
        return tourList.size();
    }
}
