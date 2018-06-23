package com.comunityapp.util.algorithm.genetic;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TourManager {
    private static List<Location> locations = new ArrayList<>();

    public static void addLocation(Location location) {
        locations.add(location);
    }

    public static Location getLocation(int index) {
        return locations.get(index);
    }

    public static int size() {
        if(!CollectionUtils.isEmpty(locations))
            return locations.size();
        return 0;
    }
}
