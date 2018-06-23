package com.comunityapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;

@Entity(name = "public_space")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class PublicSpace extends BaseEntity<Long> {
    private String name;
    private double xCoordinate;
    private double yCoordinate;
    private String address;
    private String details;
    @Enumerated
    private ActivityCategory activityCategory;
    private int capacity;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Schedule> schedules;
}
