package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Scheduling;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ii.testautomation.entities.TestCases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface SchedulingRepository extends JpaRepository<Scheduling, Long> {
    Page<Scheduling> findByTestGrouping_ProjectId(Pageable pageable, Long projectId);
}
