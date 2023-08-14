package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.TestCases;
import com.ii.testautomation.entities.TestGrouping;
import com.ii.testautomation.entities.TestScenarios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestScenariosRepository extends JpaRepository<TestScenarios, Long> {
    Page<TestScenarios> findDistinctTestScenariosByTestCases_SubModule_MainModule_Modules_Project_Id(Long projectId,Pageable pageable);
    boolean existsByTestCasesSubModuleMainModuleModulesProject_id(Long projectId);

    boolean existsByNameIgnoreCase(String name);

    // List<TestScenarios> findByTestCases(List<TestCases> testCases);
    boolean existsByTestCasesIn(List<TestCases> testCasesList);

}
