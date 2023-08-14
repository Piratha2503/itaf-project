package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.TestCases;
import com.ii.testautomation.entities.TestScenarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestScenariosRepository extends JpaRepository<TestScenarios,Long> {
    boolean existsByNameIgnoreCase(String name);


    TestScenarios findByTestCasesIn(List<TestCases> testCasesList);

    boolean existsByTestCasesIn(List<TestCases> testCasesList);

    //boolean existsByTestScenariosNameIgnoreCaseAndTestCases_SubModule_MainModule_Modules_Project_AndIdNot(String name, Long id);
    boolean existsByNameIgnoreCaseAndTestCasesSubModuleMainModuleModulesProjectAndIdNot(String name,Long id,Long projectId);

}
