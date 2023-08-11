package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.TestCases;
import com.ii.testautomation.entities.TestGrouping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;


public interface TestCasesRepository extends JpaRepository<TestCases, Long>, QuerydslPredicateExecutor<TestCases> {
    boolean existsByNameIgnoreCaseAndSubModule_MainModule_Modules_Project_Id(String name, Long projectId);

    boolean existsByNameIgnoreCaseAndSubModule_MainModule_Modules_Project_IdAndIdNot(String name, Long projectId, Long id);

    List<TestCases> findAllTestCasesBySubModuleId(Long id);

    List<TestCases> findBySubModule_MainModule_Modules_Id(Long ModuleId);

    List<TestCases> findBySubModule_MainModule_Id(Long MainModuleId);

    Page<TestCases> findBySubModuleMainModuleModulesProjectId(Long ProjectId, Pageable pageable);

    boolean existsBySubModuleId(Long id);

    boolean existsBySubModule_MainModule_Modules_Project_id(Long ProjectId);


}
