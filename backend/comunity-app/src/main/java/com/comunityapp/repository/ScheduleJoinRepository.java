package com.comunityapp.repository;

import com.comunityapp.domain.ScheduleJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleJoinRepository extends JpaRepository<ScheduleJoin, Long> {
}
