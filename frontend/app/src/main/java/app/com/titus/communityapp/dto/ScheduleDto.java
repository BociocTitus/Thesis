package app.com.titus.communityapp.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScheduleDto {
    private int id;
    private String begin;
    private String end;
    private List<ScheduleJoinDto> scheduleJoinDtoList;
    private Boolean isFull;
}
