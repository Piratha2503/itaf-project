package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Scheduling;
import com.ii.testautomation.entities.TestCases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface SchedulingRepository extends JpaRepository<Scheduling,Long> , QuerydslPredicateExecutor<Scheduling> {


}
