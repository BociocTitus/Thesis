package app.com.titus.communityapp.util.mapper;

import java.util.ArrayList;
import java.util.List;

import app.com.titus.communityapp.domain.ReportCategory;
import app.com.titus.communityapp.util.constant.ConstantUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportCategoryMapper {

    private static final String BROKEN_LIGHT = "Broken street light";
    private static final String DEAD_ANIMAL = "Dead animal";
    private static final String GRAFITTI = "Grafitti";
    private static final String TRASH = "Trash";

    public static List<String> getCategoryStrings() {
        List<String> categories = new ArrayList<>();
        categories.add(BROKEN_LIGHT);
        categories.add(DEAD_ANIMAL);
        categories.add(GRAFITTI);
        categories.add(TRASH);
        return categories;
    }

    public static ReportCategory mapStringToReportCategory(String category) {
        switch (category) {
            case BROKEN_LIGHT:
                return ReportCategory.BROKEN_STREET_LIGHTS;
            case DEAD_ANIMAL:
                return ReportCategory.DEAD_ANIMAL;
            case GRAFITTI:
                return ReportCategory.GRAFFITI;
            case TRASH:
                return ReportCategory.TRASH;
            default:
                return ReportCategory.INVALID;
        }

    }

    public static String mapReportCategoryToString(ReportCategory reportCategory) {
        switch (reportCategory) {
            case BROKEN_STREET_LIGHTS:
                return BROKEN_LIGHT;
            case TRASH:
                return TRASH;
            case GRAFFITI:
                return GRAFITTI;
            case DEAD_ANIMAL:
                return DEAD_ANIMAL;
            default:
                return ConstantUtils.EMPTY_STRING;
        }
    }
}
