package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Licenses;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepository extends JpaRepository<Licenses,Long> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByDurationAndNoOfProjectsAndNoOfUsers(Long duration, Long noOfProjects, Long noOfUsers);
}
