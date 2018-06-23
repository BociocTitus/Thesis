package com.comunityapp.converter;

import com.comunityapp.domain.ScheduleJoin;
import com.comunityapp.dto.ScheduleJoinDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleJoinConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleJoinConverter.class);

    public static ScheduleJoin convertFromDto(ScheduleJoinDto scheduleJoinDto) {
        LOGGER.info("convertFromDto called:{}", scheduleJoinDto);

        ScheduleJoin scheduleJoin = ScheduleJoin.builder()
                .members(scheduleJoinDto.getMembers())
                .build();

        LOGGER.info("convertFromDto result:{}", scheduleJoin);
        return scheduleJoin;
    }

    public static ScheduleJoinDto convertToDto(ScheduleJoin scheduleJoin) {
        LOGGER.info("convertToDto:{}", scheduleJoin);

        ScheduleJoinDto scheduleJoinDto = ScheduleJoinDto.builder()
                .id(scheduleJoin.getId())
                .userId(scheduleJoin.getUser().getId())
                .members(scheduleJoin.getMembers())
                .build();
        scheduleJoinDto.setId(scheduleJoin.getId());
        LOGGER.info("convertFromDtoResult:{}", scheduleJoinDto);
        return scheduleJoinDto;
    }
}
