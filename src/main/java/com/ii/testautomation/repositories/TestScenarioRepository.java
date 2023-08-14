package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.TestScenarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestScenarioRepository extends JpaRepository<TestScenarios,Long> {
}
