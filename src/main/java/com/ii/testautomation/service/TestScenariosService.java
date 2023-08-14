package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.TestScenariosRequest;

public interface TestScenariosService {
    boolean existsByTestScenarioId(Long testScenarioId);

    boolean existsByTestScenarioNameIgnoreCase(String name);

    boolean existByTestCaseList(TestScenariosRequest testScenariosRequest);

    void saveTestScenario(TestScenariosRequest testScenariosRequest);

    void updateExecutionStatus(Long testScenarioId);
}
