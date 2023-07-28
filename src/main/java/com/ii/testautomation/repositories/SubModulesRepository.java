package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.SubModules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface SubModulesRepository extends JpaRepository<SubModules, Long>, QuerydslPredicateExecutor<SubModules> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByPrefixIgnoreCase(String prefix);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    boolean existsByPrefixIgnoreCaseAndIdNot(String prefix, Long id);

    List<SubModules> findAllSubModulesByMainModuleId(Long id);

    List<SubModules> findByMainModule_Modules_Project_Id(Long projectId);

    boolean existsByMainModuleId(Long id);

    boolean existsByMainModule_Modules_ProjectId(Long projectId);

}
