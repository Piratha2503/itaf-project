package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.TestCases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;


public interface TestCasesRepository extends JpaRepository<TestCases, Long>, QuerydslPredicateExecutor<TestCases> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    List<TestCases> findAllTestCasesBySubModuleId(Long id);

    List<TestCases> findBySubModule_MainModule_Modules_Project_Id(Long ProjectId);

    boolean existsBySubModuleId(Long id);
    boolean existsBySubModule_MainModule_Modules_Project_id(Long ProjectId);
}
