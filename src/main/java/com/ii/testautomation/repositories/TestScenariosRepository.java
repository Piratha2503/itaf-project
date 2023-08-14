package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.TestScenarios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface TestScenariosRepository extends JpaRepository<TestScenarios,Long> {
 Page<TestScenarios> findByTestCasesSubModuleMainModuleModulesProjectId(Long projectId, Pageable pageable);
 boolean existsByTestCasesSubModuleMainModuleModulesProject_id(Long projectId);

}
