package com.ii.testautomation.service.impl;

import com.ii.testautomation.repositories.TestScenariosRepository;
import com.ii.testautomation.service.TestScenariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestScenariosServiceImpl implements TestScenariosService {
    @Autowired
    private TestScenariosRepository testScenarioRepository;

    @Override
    public boolean existsByTestScenarioId(Long testScenarioId) {
        return testScenarioRepository.existsById(testScenarioId);
    }

    @Override
    public boolean isUpdateTestScenariosNameExists(Long id, String name) {

        return testScenarioRepository.existsByNameIgnoreCaseAndTestCases_SubModule_MainModule_Modules_Project_IdAndIdNot(id,name);
    }
}
