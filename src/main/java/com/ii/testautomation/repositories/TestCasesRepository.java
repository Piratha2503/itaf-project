package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.TestCases;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCasesRepository extends JpaRepository<TestCases, Long> {
}
