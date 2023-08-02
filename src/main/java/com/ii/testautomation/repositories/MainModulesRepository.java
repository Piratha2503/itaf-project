package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.MainModules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import java.util.List;

public interface MainModulesRepository extends JpaRepository<MainModules, Long>, QuerydslPredicateExecutor<MainModules> {
    boolean existsByNameIgnoreCaseAndModules_ProjectId(String name,Long projectId);
    boolean existsByPrefixIgnoreCaseAndModules_ProjectId(String prefix,Long projectId);

    boolean existsByNameIgnoreCaseAndModules_ProjectIdAndIdNot(String mainModuleName,Long projectId, Long mainModuleId);

    boolean existsByPrefixIgnoreCaseAndModules_ProjectIdAndIdNot(String mainModulePrefix,Long projectId, Long mainModuleId);

    boolean existsByModulesId(Long id);

    List<MainModules> findByModules_ProjectId(Long id);

    List<MainModules> findAllByModulesId(Long id);
    List<MainModules> findAllByNameIgnoreCase(String name);

    MainModules findByModulesId(Long id);

}