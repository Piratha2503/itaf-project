package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Licenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface LicenseRepository extends JpaRepository<Licenses,Long> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByDurationAndNoOfProjectsAndNoOfUsers(Long duration, Long noOfProjects, Long noOfUsers);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

}