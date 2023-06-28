package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Modules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ModulesRepository extends JpaRepository<Modules, Long>, QuerydslPredicateExecutor<Modules> {
    public boolean existsByNameIgnoreCase(String name);

    public boolean existsByPrefixIgnoreCase(String prefix);

    public boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    public boolean existsByPrefixIgnoreCaseAndIdNot(String prefix, Long id);

    public List<Modules> findAllModulesByProjectId(Long projectId);

    public boolean existsByProjectId(Long id);
}
