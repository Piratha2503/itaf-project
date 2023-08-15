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

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Boolean> booleans = new ArrayList<>();
        List<TestCases> testCasesList = new ArrayList<>();
        for (Long testCaseId : testScenariosRequest.getTestCasesId()) {
            testCasesList.add(testCasesRepository.findById(testCaseId).get());
        }
        List<TestScenarios> testScenariosList = testScenariosRepository.findAll();
        for (TestScenarios testScenarios : testScenariosList) {
            if (testScenarios.getTestCases().containsAll(testCasesList)) booleans.add(true);
            else booleans.add(false);
        }
        if (booleans.contains(true)) return true;
        else return false;
    }

    @Override
    public void saveTestScenario(TestScenariosRequest testScenariosRequest) {

        TestScenarios testScenarios = new TestScenarios();
        List<TestCases> testCasesList = new ArrayList<>();

        if (testScenariosRequest.getTestCasesId() != null) {
            for (Long testCaseId : testScenariosRequest.getTestCasesId())
                testCasesList.add(testCasesRepository.findById(testCaseId).get());
        }
        if (testScenariosRequest.getSubModuleIds() != null) {
            for (Long subModuleId : testScenariosRequest.getSubModuleIds())
                testCasesRepository.findAllTestCasesBySubModuleId(subModuleId).forEach(TestCases -> testCasesList.add(TestCases));
        }
        if (testScenariosRequest.getMainModuleIds() != null) {
            for (Long mainModuleId : testScenariosRequest.getMainModuleIds())
                testCasesRepository.findBySubModule_MainModule_Id(mainModuleId).forEach(TestCases -> testCasesList.add(TestCases));
        }
        if (testScenariosRequest.getModuleIds() != null) {
            for (Long moduleId : testScenariosRequest.getModuleIds())
                testCasesRepository.findBySubModule_MainModule_Modules_Id(moduleId).forEach(TestCases -> testCasesList.add(TestCases));
        }

        List<TestCases> sortedTestCaseList = testCasesList.stream().distinct().collect(Collectors.toList());
        BeanUtils.copyProperties(testScenariosRequest, testScenarios);
        testScenarios.setTestCases(sortedTestCaseList);
        testScenariosRepository.save(testScenarios);
    }

    @Override
    public boolean isUpdateTestScenariosNameExists(Long id, String name) {
        return testScenariosRepository.existsByNameIgnoreCaseAndIdNot(name,id);

    }
}