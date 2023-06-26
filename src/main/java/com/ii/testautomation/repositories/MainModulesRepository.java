package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.MainModules;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MainModulesRepository extends JpaRepository<MainModules,Long>, QuerydslPredicateExecutor<MainModules>
=======

public interface MainModulesRepository extends JpaRepository<MainModules,Long>
>>>>>>> 0a0f24cb0bbeb23441effb6462c41af7925d5675
{
    boolean existsByName(String name);
    boolean existsByPrefix(String prefix);

    boolean existsByNameIgnoreCaseAndIdNot(String mainmoduleName, Long mainmoduleId);

    boolean existsByPrefixIgnoreCaseAndIdNot(String mainModuleprefix, Long mainModuleId);
}