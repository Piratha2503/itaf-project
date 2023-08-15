package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.TestScenariosRequest;
import com.ii.testautomation.entities.TestCases;
import com.ii.testautomation.entities.TestScenarios;
import com.ii.testautomation.repositories.ProjectRepository;
import com.ii.testautomation.repositories.TestCasesRepository;
import com.ii.testautomation.repositories.TestScenariosRepository;
import com.ii.testautomation.service.TestScenariosService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestScenariosServiceImpl implements TestScenariosService {
    @Autowired
    private TestScenariosRepository testScenariosRepository;
    @Autowired
    private TestCasesRepository testCasesRepository;
    @Autowired
    private ProjectRepository projectRepository;
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
        for (Long testCaseId : testScenariosRequest.getTestCasesId())
        {
            testCasesList.add(testCasesRepository.findById(testCaseId).get());
        }
        List<TestScenarios> testScenariosList = testScenariosRepository.findAll();
        for (TestScenarios testScenarios : testScenariosList)
        {
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
        for (Long testCaseId: testScenariosRequest.getTestCasesId())
        {
            testCasesList.add(testCasesRepository.findById(testCaseId).get());
        }
        BeanUtils.copyProperties(testScenariosRequest,testScenarios);
        testScenarios.setTestCases(testCasesList);
        testScenariosRepository.save(testScenarios);
    }

    @Override
    public void updateExecutionStatus(Long testScenarioId,Long projectId) {
        TestScenarios testScenarios=testScenariosRepository.findById(testScenarioId).get();
        testScenarios.setExecutionStatus(true);
        testScenariosRepository.save(testScenarios);
        String savedFilePath = projectRepository.findById(projectId).get().getJarFilePath();
        File jarFile = new File(savedFilePath);
        String jarFileName = jarFile.getName();
        String jarDirectory = jarFile.getParent();
        try {
            ProcessBuilder runProcessBuilder = new ProcessBuilder("java", "-jar", jarFileName);
            runProcessBuilder.directory(new File(jarDirectory));
            runProcessBuilder.redirectErrorStream(true);
            Process runProcess = runProcessBuilder.start();
            runProcess.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean hasExcelPath(Long testScenarioId) {
        TestScenarios testScenarios=testScenariosRepository.findById(testScenarioId).get();
        if(testScenarios.getExcelPath()!=null)return true;
        return false;
    }


}
