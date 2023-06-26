package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.MainModules;
import com.ii.testautomation.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MainModulesRepository extends JpaRepository<MainModules,Long>, QuerydslPredicateExecutor<MainModules>
{
    boolean existsByName(String name);
    boolean existsByPrefix(String prefix);

    boolean existsByNameIgnoreCaseAndIdNot(String mainmoduleName, Long mainmoduleId);

    boolean existsByPrefixIgnoreCaseAndIdNot(String mainModuleprefix, Long mainModuleId);
}