package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.TestScenarios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public interface TestScenariosRepository extends JpaRepository<TestScenarios, Long> {
    Page<TestScenarios> findDistinctTestScenariosByTestCases_SubModule_MainModule_Modules_Project_Id(Long projectId,Pageable pageable);
    boolean existsByTestCasesSubModuleMainModuleModulesProject_id(Long projectId);

    boolean existsByNameIgnoreCaseAndTestCases_SubModule_MainModule_Modules_Project_Id(String name,Long projectId);

    boolean existsByNameIgnoreCaseAndIdNot(String name,Long id);

    TestScenarios findByIdAndTestCases_SubModule_MainModule_Modules_Project_Id(Long id, Long projectId);

    boolean existsByNameIgnoreCaseAndTestCases_SubModule_MainModule_Modules_Project_IdAndIdNot(String name, Long projectId, Long id);

    List<TestScenarios> findByTestCasesId(Long id);

    List<TestScenarios> findByTestCases_SubModule_MainModule_Modules_Project_Id(Long projectId);
}
