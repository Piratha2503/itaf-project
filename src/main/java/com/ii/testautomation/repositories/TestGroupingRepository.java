package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.TestGrouping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface TestGroupingRepository extends JpaRepository<TestGrouping, Long>, QuerydslPredicateExecutor<TestGrouping> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    List<TestGrouping> findAllTestGroupingByTestCasesId(Long id);

    List<TestGrouping> findAllTestGroupingByTestTypeId(Long id);

    boolean existsByTestCasesId(Long id);

    boolean existsByTestTypeId(Long id);

}
