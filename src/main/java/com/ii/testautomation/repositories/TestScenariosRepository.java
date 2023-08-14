package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.TestScenarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TestScenariosRepository extends JpaRepository<TestScenarios,Long>, QuerydslPredicateExecutor<TestScenarios> {


    boolean existsByNameIgnoreCaseAndTestCases_SubModule_MainModule_Modules_Project_IdAndIdNot(String name, Long projectId, Long id);
}
