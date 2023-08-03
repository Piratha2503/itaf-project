package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.TestGrouping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface TestGroupingRepository extends JpaRepository<TestGrouping, Long>, QuerydslPredicateExecutor<TestGrouping> {

}
