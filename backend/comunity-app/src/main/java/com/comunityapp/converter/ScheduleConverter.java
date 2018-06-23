package com.comunityapp.converter;

import com.comunityapp.domain.Schedule;
import com.comunityapp.dto.ScheduleDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleConverter.class);

    public static ScheduleDto convertToDto(Schedule schedule) {
        LOGGER.info("convertToDto called:{}", schedule);

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .begin(schedule.getBeginDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)))
                .end(schedule.getEndDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)))
                .build();

        LOGGER.info("convertToDto result:{}", scheduleDto);
        return scheduleDto;
    }

    public static Schedule convertFromDto(ScheduleDto scheduleDto) {
        LOGGER.info("convertFromDto called:{}", scheduleDto);

        Schedule schedule = Schedule.builder()
                .beginDate(LocalDateTime.parse(scheduleDto.getBegin()))
                .endDate(LocalDateTime.parse(scheduleDto.getEnd()))
                .build();

        LOGGER.info("convertFromDto result:{}", schedule);
        return schedule;
    }
}
