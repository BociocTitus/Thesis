package com.comunityapp.service;

import com.comunityapp.domain.ScheduleJoin;
import com.comunityapp.repository.ScheduleJoinRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScheduleJoinService {
    private final ScheduleJoinRepository scheduleJoinRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleJoinService.class.getName());

    public ScheduleJoin addScheduleJoin(ScheduleJoin scheduleJoin) {
        LOGGER.info("addScheduleJoin called:{}", scheduleJoin);

        scheduleJoin = scheduleJoinRepository.save(scheduleJoin);

        LOGGER.info("addScheduleJoin result:{}", scheduleJoin);
        return scheduleJoin;
    }
}
