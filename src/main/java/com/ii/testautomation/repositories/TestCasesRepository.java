package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.TestCases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;


public interface TestCasesRepository extends JpaRepository<TestCases, Long>, QuerydslPredicateExecutor<TestCases> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    List<TestCases> findAllTestCasesBySubModuleId(Long id);

    boolean existsBySubModuleId(Long id);
}