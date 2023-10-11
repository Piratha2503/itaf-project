package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.MainModules;
import com.ii.testautomation.entities.TestTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface TestTypesRepository extends JpaRepository<TestTypes, Long>, QuerydslPredicateExecutor<TestTypes> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
    List<TestTypes> findAllByCompanyUserId(Long id);
}
