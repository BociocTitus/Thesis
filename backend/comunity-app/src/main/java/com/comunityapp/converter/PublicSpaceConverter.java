package com.comunityapp.converter;

import com.comunityapp.domain.PublicSpace;
import com.comunityapp.domain.Schedule;
import com.comunityapp.domain.ScheduleJoin;
import com.comunityapp.dto.PublicSpaceDto;
import com.comunityapp.dto.ScheduleDto;
import com.comunityapp.dto.ScheduleJoinDto;
import com.comunityapp.util.mapper.ActivityCategoryMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PublicSpaceConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicSpaceConverter.class);

    public static PublicSpaceDto convertToDto(PublicSpace publicSpace) {
        LOGGER.info("convertToDto called:{}", publicSpace);

        PublicSpaceDto publicSpaceDto =
                PublicSpaceDto.builder()
                        .address(publicSpace.getAddress())
                        .id(publicSpace.getId())
                        .activityCategory(ActivityCategoryMapper.mapReportCategoryToString(publicSpace.getActivityCategory()))
                        .capacity(publicSpace.getCapacity())
                        .details(publicSpace.getDetails())
                        .xCoordinate(publicSpace.getXCoordinate())
                        .yCoordinate(publicSpace.getYCoordinate())
                        .name(publicSpace.getName())
                        .build();
        List<ScheduleDto> scheduleDtoList = publicSpace.getSchedules()
                .stream()
                .map(schedule -> ScheduleDto.builder()
                        .begin(schedule.getBeginDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)))
                        .end(schedule.getEndDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)))
                        .isFull(getScheduledUsers(schedule, publicSpaceDto))
                        .scheduleJoinDtoList(schedule.getScheduleJoinList().stream().map(ScheduleJoinConverter::convertToDto).collect(Collectors.toList()))
                        .build()
                )
                .collect(Collectors.toList());
        publicSpaceDto.setSchedules(scheduleDtoList);

        LOGGER.info("convertToDto result:{}", publicSpaceDto);
        return publicSpaceDto;
    }

    public static PublicSpace convertFromDto(PublicSpaceDto publicSpaceDto) {
        LOGGER.info("convertToDto called:{}", publicSpaceDto);

        PublicSpace publicSpace = PublicSpace.builder()
                .address(publicSpaceDto.getAddress())
                .activityCategory(ActivityCategoryMapper.mapStringToReportCategory(publicSpaceDto.getActivityCategory()))
                .capacity(publicSpaceDto.getCapacity())
                .details(publicSpaceDto.getDetails())
                .xCoordinate(publicSpaceDto.getXCoordinate())
                .yCoordinate(publicSpaceDto.getYCoordinate())
                .name(publicSpaceDto.getName())
                .build();

        if (publicSpaceDto.getId() != 0) {
            publicSpace.setId(publicSpaceDto.getId());
        }

        List<Schedule> schedules = publicSpaceDto.getSchedules().stream().map(ScheduleConverter::convertFromDto)
                .collect(Collectors.toList());

        publicSpace.setSchedules(schedules);
        LOGGER.info("convertFromDto result:{}", publicSpace);
        return publicSpace;

    }

    private static Boolean getScheduledUsers(Schedule schedule, PublicSpaceDto publicSpaceDto) {
        return publicSpaceDto.getCapacity() <= schedule.getScheduleJoinList().stream()
                .mapToInt(ScheduleJoin::getMembers)
                .sum();
    }
}