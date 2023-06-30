package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.MainModules;
import com.ii.testautomation.entities.TestTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TestTypesRepository extends JpaRepository <TestTypes,Long>, QuerydslPredicateExecutor<TestTypes>
{
    boolean existsByName(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
