package com.comunityapp.service;

import com.comunityapp.domain.Schedule;
import com.comunityapp.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class.getName());

    public Schedule addSchedule(Schedule schedule) {
        LOGGER.info("addSchedule:{}", schedule);

        schedule = scheduleRepository.save(schedule);

        LOGGER.info("addSchedule result:{}", schedule);
        return schedule;
    }

    public void addAllSchedules(Iterable<Schedule> schedules) {
        LOGGER.info("addAllSchedules called:{}", schedules);

        scheduleRepository.saveAll(schedules);
    }
}
