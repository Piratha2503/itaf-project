package com.ii.testautomation.service;

public interface TestScenarioService {
    boolean existsByTestScenarioId(Long testScenarioId);
    boolean existsByTestScenarioNameIgnoreCase(String name);
}
