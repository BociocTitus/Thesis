package app.com.titus.communityapp.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicSpaceDto {
    private long id;
    private String name;
    private double xCoordinate;
    private double yCoordinate;
    private String address;
    private String details;
    private String activityCategory;
    private int capacity;
    private List<ScheduleDto> schedules;
}
