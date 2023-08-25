package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Scheduling;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ii.testautomation.entities.TestCases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SchedulingRepository extends JpaRepository<Scheduling,Long>, QuerydslPredicateExecutor<Scheduling> {
    Page<Scheduling> findByTestGrouping_ProjectId(Pageable pageable, Long projectId);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
    boolean existsByNameIgnoreCaseAndTestGrouping_TestCases_SubModule_MainModule_Modules_Project_Id(String name, Long projectId);
}
