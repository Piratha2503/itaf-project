package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.SubModules;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubModulesRepository extends JpaRepository<SubModules,Long> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByPrefixIgnoreCase(String prefix);

    boolean existsByNameIgnoreCaseAndIdNot(String name,Long id);

    boolean existsByPrefixIgnoreCaseAndIdNot(String prefix,Long id);


}
