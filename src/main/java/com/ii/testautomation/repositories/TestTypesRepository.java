package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.TestTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface TestTypesRepository extends JpaRepository<TestTypes, Long>, QuerydslPredicateExecutor<TestTypes> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
    @Query("SELECT tt FROM TestTypes tt JOIN TestGrouping tg ON tg.testType = tt JOIN tg.testCases tc JOIN tc.subModule sm JOIN sm.mainModule mm JOIN mm.modules m JOIN m.project p WHERE p.id = :id")
    List<TestTypes> findBymyProjectId(Long id);
}
