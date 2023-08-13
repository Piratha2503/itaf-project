package com.ii.testautomation.service.impl;

import com.ii.testautomation.repositories.TestScenarioRepository;
import com.ii.testautomation.service.TestScenarioService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestScenarioServiceImpl implements TestScenarioService {
    @Autowired
    private TestScenarioRepository testScenarioRepository;

    @Override
    public boolean existsByTestScenarioId(Long testScenarioId) {
        return testScenarioRepository.existsById(testScenarioId);
    }
}
