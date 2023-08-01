package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.TestGrouping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface TestGroupingRepository extends JpaRepository<TestGrouping, Long>, QuerydslPredicateExecutor<TestGrouping> {
    boolean existsByNameIgnoreCaseAndTestCases_SubModule_MainModule_Modules_Project_Id(String name,Long ProjectId);

    boolean existsByNameIgnoreCaseAndTestCases_SubModule_MainModule_Modules_Project_IdAndIdNot(String name,Long projectId, Long id);

    boolean existsByTestCases_IdAndTestCases_SubModule_MainModule_Modules_Project_Id(Long testCaseId,Long projectId);

    List<TestGrouping> findAllTestGroupingByTestCasesId(Long id);

    List<TestGrouping> findAllTestGroupingByTestTypeId(Long id);

    boolean existsByTestCasesId(Long id);

    boolean existsByTestTypeId(Long id);

    List<TestGrouping> findByTestCases_SubModule_MainModule_Modules_Project_Id(Long projectId);

    boolean existsByTestCases_SubModule_MainModule_Modules_ProjectId(Long projectId);

}
