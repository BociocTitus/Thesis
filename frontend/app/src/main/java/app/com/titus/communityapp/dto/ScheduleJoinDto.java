package app.com.titus.communityapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ScheduleJoinDto {
    private Long id;
    private Long userId;
    private Integer members;
}
