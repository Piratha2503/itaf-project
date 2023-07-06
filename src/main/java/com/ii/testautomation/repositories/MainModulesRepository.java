package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.MainModules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface MainModulesRepository extends JpaRepository<MainModules, Long>, QuerydslPredicateExecutor<MainModules> {
    boolean existsByName(String name);

    boolean existsByPrefix(String prefix);

    boolean existsByNameIgnoreCaseAndIdNot(String mainmoduleName, Long mainmoduleId);

    boolean existsByModulesId(Long id);

    boolean existsByPrefixIgnoreCaseAndIdNot(String mainModuleprefix, Long mainModuleId);

    List<MainModules> findAllByModulesId(Long id);


}