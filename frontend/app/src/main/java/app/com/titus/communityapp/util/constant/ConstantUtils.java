package app.com.titus.communityapp.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantUtils {
    public static final String EMPTY_STRING = "";
    public static final String COLOMN = ",";
    public static final int FIFTY_THOUSAND = 50000;
    public static final int ONE_THOUSAND = 1000;
    public static final String GPS = "gps";
    public static final Float ZOOM = 15.0f;
    public static String JWT;
    public static final String HEADERS = "Accept:application/json";
    private static final String TITLE = "Cluj Live";
    public static final Double clujLatitude = 46.770601;
    public static final Double clujLongitude = 23.588673;
    public static final String OPEN = "Open";
    public static final String CLOSED = "Closed";
    public static final String SLASH = "/";
}
