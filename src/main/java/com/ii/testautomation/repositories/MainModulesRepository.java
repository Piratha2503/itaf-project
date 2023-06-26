package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.MainModules;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainModulesRepository extends JpaRepository<MainModules,Long>
{
    boolean existsByName(String name);
    boolean existsByPrefix(String prefix);

    boolean existsByNameIgnoreCaseAndIdNot(String mainmoduleName, Long mainmoduleId);

    boolean existsByPrefixIgnoreCaseAndIdNot(String mainModuleprefix, Long mainModuleId);
}