package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.ExecutedTestCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutedTestCaseRepository extends JpaRepository<ExecutedTestCase,Long> {
}
