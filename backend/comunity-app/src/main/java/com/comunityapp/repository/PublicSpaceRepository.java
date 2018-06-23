package com.comunityapp.repository;

import com.comunityapp.domain.PublicSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicSpaceRepository extends JpaRepository<PublicSpace, Long> {
}
