package com.comunityapp.util.mapper;

import com.comunityapp.domain.ActivityCategory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivityCategoryMapper {
    private static final String TENNIS = "Tenins";
    private static final String FOOTBALL = "Football";
    private static final String BASKETBALL = "Basketball";
    private static final String GREEN_SPACE = "Green Space";

    public static ActivityCategory mapStringToReportCategory(String category) {
        switch (category) {
            case TENNIS:
                return ActivityCategory.TENNIS;
            case FOOTBALL:
                return ActivityCategory.FOOTBALL;
            case BASKETBALL:
                return ActivityCategory.BASKETBALL;
            case GREEN_SPACE:
                return ActivityCategory.GREEN_SPACE;
            default:
                return ActivityCategory.INVALID;
        }
    }

    public static String mapReportCategoryToString(ActivityCategory activityCategory) {
        switch (activityCategory) {
            case TENNIS:
                return TENNIS;
            case FOOTBALL:
                return FOOTBALL;
            case BASKETBALL:
                return BASKETBALL;
            case GREEN_SPACE:
                return GREEN_SPACE;
            default:
                return "";
        }
    }
}
