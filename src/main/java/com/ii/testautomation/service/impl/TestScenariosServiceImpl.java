package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.TestScenariosRequest;
import com.ii.testautomation.entities.TestCases;
import com.ii.testautomation.entities.TestScenarios;
import com.ii.testautomation.repositories.TestCasesRepository;
import com.ii.testautomation.repositories.TestScenariosRepository;
import com.ii.testautomation.service.TestScenariosService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestScenariosServiceImpl implements TestScenariosService {
    @Autowired
    private TestScenariosRepository testScenariosRepository;
    @Autowired
    private TestCasesRepository testCasesRepository;

    @Override
    public boolean existsByTestScenarioId(Long testScenarioId) {
        return testScenariosRepository.existsById(testScenarioId);
    }

    @Override
    public boolean existsByTestScenarioNameIgnoreCase(String name) {
        return testScenariosRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existByTestCaseList(TestScenariosRequest testScenariosRequest) {
        List<TestCases> testCasesList = new ArrayList<>();
        for (Long testCaseId : testScenariosRequest.getTestCasesId())
        {
            testCasesList.add(testCasesRepository.findById(testCaseId).get());
        }
        return testScenariosRepository.existsByTestCasesIn(testCasesList);
    }

    @Override
    public void saveTestScenario(TestScenariosRequest testScenariosRequest) {

        TestScenarios testScenarios = new TestScenarios();
        List<TestCases> testCasesList = new ArrayList<>();
        for (Long testCaseId: testScenariosRequest.getTestCasesId())
        {
            testCasesList.add(testCasesRepository.findById(testCaseId).get());
        }
        BeanUtils.copyProperties(testScenariosRequest,testScenarios);
        testScenarios.setTestCases(testCasesList);
        testScenariosRepository.save(testScenarios);
    }


    @Override
    public boolean isUpdateTestScenariosNameExists(Long id,String name,Long projectId) {
        return testScenariosRepository.existsByNameIgnoreCaseAndTestCasesSubModuleMainModuleModulesProjectAndIdNot(name,id,projectId);
    }

}
