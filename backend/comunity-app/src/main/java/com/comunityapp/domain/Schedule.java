package com.comunityapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "schedules")
@Data
@Builder
@AllArgsConstructor
@ToString(exclude = "scheduleJoinList")
@NoArgsConstructor
public class Schedule extends BaseEntity<Long> {
    @Column
    private LocalDateTime beginDate;
    @Column
    private LocalDateTime endDate;
    @OneToMany(fetch = FetchType.EAGER)
    private List<ScheduleJoin> scheduleJoinList;
}