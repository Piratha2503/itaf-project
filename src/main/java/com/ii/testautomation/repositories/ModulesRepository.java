package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Modules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;


public interface ModulesRepository extends JpaRepository<Modules, Long>, QuerydslPredicateExecutor<Modules> {
    boolean existsByNameIgnoreCaseAndProjectId(String name, Long projectId);

    public boolean existsByPrefixIgnoreCaseAndProjectId(String prefix,Long projectId);

    public boolean existsByNameIgnoreCaseAndProjectIdAndIdNot(String name, Long projectId,Long id);

    public boolean existsByPrefixIgnoreCaseAndProjectIdAndIdNot(String prefix,Long projectId, Long id);

    public List<Modules> findAllModulesByProjectId(Long projectId);

    public boolean existsByProjectId(Long id);
}
