package com.comunityapp.repository;

import com.comunityapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(@Email String email);
    User findByEmailAndPassword(@Email String email, String password);
}
