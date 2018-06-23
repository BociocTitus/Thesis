package com.comunityapp.domain;

import com.comunityapp.domain.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import java.util.List;

@Entity(name = "users")
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ToString(exclude = "scheduleJoinList")
public class User extends BaseEntity<Long> {
    @Column
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String address;
    @Column
    @Email
    private String email;
    @OneToMany(fetch = FetchType.EAGER)
    private List<ScheduleJoin> scheduleJoinList;
}

