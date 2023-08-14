package com.ii.testautomation.service;

public interface TestScenariosService {
    boolean existsByTestScenarioId(Long testScenarioId);

    boolean isUpdateTestScenariosNameExists(Long id, String name);

}
