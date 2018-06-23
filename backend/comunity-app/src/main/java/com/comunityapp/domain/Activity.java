package com.comunityapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "activity")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Activity extends BaseEntity<Long> {
    private String description;
    @ManyToMany
    private List<User> users;
}
