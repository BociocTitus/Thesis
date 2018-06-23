package com.comunityapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity(name = "schedule_joiners")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "user")
public class ScheduleJoin extends BaseEntity<Long> {
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    private Integer members;
}
