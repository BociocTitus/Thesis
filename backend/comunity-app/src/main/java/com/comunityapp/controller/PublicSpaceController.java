package com.comunityapp.controller;

import com.comunityapp.converter.PublicSpaceConverter;
import com.comunityapp.converter.ScheduleConverter;
import com.comunityapp.domain.ActivityCategory;
import com.comunityapp.domain.PublicSpace;
import com.comunityapp.domain.Schedule;
import com.comunityapp.domain.ScheduleJoin;
import com.comunityapp.domain.User;
import com.comunityapp.dto.PublicSpaceDto;
import com.comunityapp.dto.ScheduleDto;
import com.comunityapp.service.PublicSpaceService;
import com.comunityapp.service.ScheduleJoinService;
import com.comunityapp.service.ScheduleService;
import com.comunityapp.service.UserService;
import com.comunityapp.util.mapper.ActivityCategoryMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PublicSpaceController {
    private final PublicSpaceService publicSpaceService;
    private final ScheduleService scheduleService;
    private final ScheduleJoinService scheduleJoinService;
    private final UserService userService;
    private final Logger LOGGER = LoggerFactory.getLogger(PublicSpaceController.class.getName());


    @RequestMapping(value = "/savePublicSpace", method = RequestMethod.POST)
    public ResponseEntity<Boolean> savePublicSpace(@RequestBody PublicSpaceDto publicSpaceDto) {
        LOGGER.info("savePublicSpace:{}", publicSpaceDto);

        PublicSpace publicSpace = publicSpaceService.savePublicSpace(PublicSpaceConverter.convertFromDto(publicSpaceDto));

        LOGGER.info("savePublicSpace result:{}", publicSpace);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @RequestMapping(value = "/updatePublicSpace", method = RequestMethod.POST)
    public ResponseEntity<PublicSpaceDto> updatePublicSpace(@RequestBody PublicSpaceDto publicSpaceDto) {
        LOGGER.info("updatePublicSpace:{}", publicSpaceDto);

        if (!publicSpaceService.findById(publicSpaceDto.getId()).isPresent()) {
            return ResponseEntity.ok(PublicSpaceDto.builder().id(-1L).build());
        }

        List<Schedule> schedules = publicSpaceDto.getSchedules()
                .stream()
                .map(scheduleDto -> {
                    Schedule schedule = ScheduleConverter.convertFromDto(scheduleDto);
                    List<ScheduleJoin> scheduleJoinsMapped = scheduleDto.getScheduleJoinDtoList().stream()
                            .map(scheduleJoinDto -> {
                                ScheduleJoin scheduleJoin = ScheduleJoin.builder()
                                        .members(scheduleJoinDto.getMembers())
                                        .build();
                                Optional<User> user = userService.findById(scheduleJoinDto.getUserId());
                                if (user.isPresent()) {
                                    scheduleJoin.setUser(user.get());
                                    scheduleJoinService.addScheduleJoin(scheduleJoin);
                                    return scheduleJoin;
                                } else {
                                    throw new RuntimeException("Invalid user id");
                                }
                            }).collect(Collectors.toList());
                    schedule.setScheduleJoinList(scheduleJoinsMapped);
                    scheduleService.addSchedule(schedule);
                    return schedule;
                }).collect(Collectors.toList());

        PublicSpace publicSpace = PublicSpaceConverter.convertFromDto(publicSpaceDto);
        publicSpace.setSchedules(schedules);
        publicSpace = publicSpaceService.savePublicSpace(publicSpace);

        LOGGER.info("updatePublicSpace result:{}", publicSpace);
        return ResponseEntity.ok(PublicSpaceConverter.convertToDto(publicSpace));
    }

    @RequestMapping(value = "/findPublicSpaceById/{id}", method = RequestMethod.GET)
    public ResponseEntity<PublicSpaceDto> findPublicSpaceById(@PathVariable long id) {
        LOGGER.info("findPublicSpaceById:{}", id);

        Optional<PublicSpace> publicSpaceDtoOptional = publicSpaceService.findById(id);
        LOGGER.info("findPublicSpaceById result :{}", publicSpaceDtoOptional);

        return publicSpaceDtoOptional.
                map(publicSpace -> ResponseEntity.ok(PublicSpaceConverter.convertToDto(publicSpace)))
                .orElseGet(() -> ResponseEntity.ok(PublicSpaceDto.builder().id(-1L).build()));
    }

    @RequestMapping(value = "/getPublicSpaces", method = RequestMethod.GET)
    public ResponseEntity<List<PublicSpaceDto>> getPublicSpaces() {
        LOGGER.info("getPublicSpaces called");

        publicSpaceService.getPublicSpaces()
                .forEach(publicSpace -> {
                    publicSpace.setSchedules(publicSpace.getSchedules().stream()
                            .filter(schedule -> schedule.getEndDate().compareTo(LocalDateTime.now()) > 0)
                            .collect(Collectors.toList()));
                    publicSpaceService.savePublicSpace(publicSpace);
                });
        List<PublicSpaceDto> publicSpaceDtos = publicSpaceService.getPublicSpaces()
                .stream()
                .map(PublicSpaceConverter::convertToDto)
                .collect(Collectors.toList());

        LOGGER.info("getPublicSpaces result:{}", publicSpaceDtos);
        return ResponseEntity.ok(publicSpaceDtos);
    }

    @RequestMapping(value = "/getPublicSpaces/{category}", method = RequestMethod.GET)
    public ResponseEntity<List<PublicSpaceDto>> getPublicSpaces(@PathVariable String category) {
        LOGGER.info("getPublicSpaces called:{}", category);

        ActivityCategory activityCategory = ActivityCategoryMapper.mapStringToReportCategory(category);
        List<PublicSpaceDto> publicSpaceDtos = publicSpaceService.getPublicSpaces()
                .stream()
                .filter(publicSpace -> publicSpace.getActivityCategory().equals(activityCategory))
                .map(PublicSpaceConverter::convertToDto)
                .collect(Collectors.toList());

        LOGGER.info("getPublicSpaces result:{}", publicSpaceDtos);
        return ResponseEntity.ok(publicSpaceDtos);
    }

    @RequestMapping(value = "/getSchedulesForPublicSpace/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<ScheduleDto>> getSchedulesForPublicSpace(@PathVariable int id) {
        LOGGER.info("getSchedulesForPublicSpace called:{}", id);

        Optional<PublicSpace> publicSpaceOptional = publicSpaceService.findById(id);
        if (publicSpaceOptional.isPresent()) {
            List<ScheduleDto> scheduleDtos = publicSpaceOptional.get().getSchedules()
                    .stream()
                    .filter(schedule -> schedule.getEndDate().compareTo(LocalDateTime.now()) > 0)
                    .map(ScheduleConverter::convertToDto)
                    .collect(Collectors.toList());
            LOGGER.info("getSchedulesForPublicSpace result:{}", scheduleDtos);
            return ResponseEntity.ok(scheduleDtos);
        }

        LOGGER.info("No schedules found");
        return ResponseEntity.ok(new ArrayList<>());
    }
}