package com.comunityapp.util.algorithm.genetic;

import java.util.Random;

public class GA {
    private static final double mutationRate = 0.015;
    private static final int tournamentSize = 5;
    private static final boolean elitism = true;
    private static final Random random = new Random();

    public static Population evolvePopulation(Population population) {
        Population newPopulation = new Population();

        int elitismOffset;
        if (elitism) {
            newPopulation.getTourList().add(population.getFittest());
            elitismOffset = 1;
        }

        for (int i = elitismOffset; i < population.getTourList().size(); i++) {
            Tour tour1 = tournamentSelection(population);
            Tour tour2 = tournamentSelection(population);

            Tour child = crossover(tour1, tour2);
            newPopulation.getTourList().add(child);
        }

        for (int i = elitismOffset; i < population.getTourList().size(); i++) {
            mutate(newPopulation.getTourList().get(i));
        }

        return newPopulation;
    }

    private static void mutate(Tour tour) {
        for (int i = 0; i < tour.getTour().size(); i++) {
            if (Math.random() < mutationRate) {
                int tourPos = random.nextInt(tour.getTour().size());

                Location location1 = tour.getLocation(i);
                Location location2 = tour.getLocation(tourPos);

                tour.setLocation(tourPos, location1);
                tour.setLocation(i, location2);
            }
        }
    }

    private static Tour crossover(Tour parent1, Tour parent2) {
        Tour child = new Tour();

        for (int i = 0; i < parent1.getTour().size(); i++) {
            child.getTour().add(null);
        }
        parent1.getTour().forEach(tour -> {
            int pos = parent1.getTour().indexOf(tour);
            Boolean include = random.nextBoolean();
            if (include) {
                child.getTour().set(pos, tour);
            }
        });
        parent2.getTour().forEach(tour -> {
            if (!child.getTour().contains(tour)) {
                for (int i = 0; i < child.getTour().size(); i++) {
                    if (child.getTour().get(i) == null) {
                        child.getTour().set(i, tour);
                        break;
                    }
                }
            }
        });

        return child;
    }

    private static Tour tournamentSelection(Population population) {
        Population tournament = new Population();

        for (int i = 0; i < tournamentSize; i++) {
            int randomPos = random.nextInt(population.getTourList().size());
            tournament.getTourList().add(population.getTourList().get(randomPos));
        }

        return tournament.getFittest();
    }
}
