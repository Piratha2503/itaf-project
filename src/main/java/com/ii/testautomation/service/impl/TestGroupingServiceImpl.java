package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.dto.response.TestGroupingResponse;
import com.ii.testautomation.dto.search.TestGroupingSearch;
import com.ii.testautomation.entities.*;
import com.ii.testautomation.repositories.*;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.TestGroupingService;
import com.ii.testautomation.utils.Utils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TestGroupingServiceImpl implements TestGroupingService {
    @Autowired
    private TestGroupingRepository testGroupingRepository;
    @Autowired
    private TestCasesRepository testCasesRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TestScenariosRepository testScenarioRepository;
    @Autowired
    private TestTypesRepository testTypesRepository;
    @Autowired
    private SubModulesRepository subModulesRepository;
    @Autowired
    private ModulesRepository modulesRepository;
    @Autowired
    private MainModulesRepository mainModulesRepository;

    @Override
    public void saveTestGrouping(TestGroupingRequest testGroupingRequest, List<String> excelFilePath) {
        TestGrouping testGrouping = new TestGrouping();
        testGrouping.setName(testGroupingRequest.getName());
        TestTypes testTypes = new TestTypes();
        testTypes.setId(testGroupingRequest.getTestTypeId());
        testGrouping.setTestType(testTypes);
        testGrouping.setExcelFilePath(excelFilePath);
        List<TestCases> testCasesList = new ArrayList<>();
        if (testGroupingRequest.getSubModuleIds() != null && !testGroupingRequest.getSubModuleIds().isEmpty()) {
            for (Long subModuleId : testGroupingRequest.getSubModuleIds()) {
                List<TestCases> testCases = testCasesRepository.findAllTestCasesBySubModuleId(subModuleId);
                for (TestCases testCases1 : testCases) {
                    testCasesList.add(testCases1);
                }
            }
        }
        if (testGroupingRequest.getMainModuleIds() != null && !testGroupingRequest.getMainModuleIds().isEmpty()) {
            for (Long mainModuleId : testGroupingRequest.getMainModuleIds()) {
                List<TestCases> testCases = testCasesRepository.findBySubModule_MainModule_Id(mainModuleId);
                for (TestCases testCase1 : testCases) {
                    testCasesList.add(testCase1);
                }
            }
        }
        if (testGroupingRequest.getModuleIds() != null && !testGroupingRequest.getModuleIds().isEmpty()) {
            for (Long moduleId : testGroupingRequest.getModuleIds()) {
                List<TestCases> testCases = testCasesRepository.findBySubModule_MainModule_Modules_Id(moduleId);
                for (TestCases testCase1 : testCases) {
                    testCasesList.add(testCase1);
                }
            }
        }
        if (testGroupingRequest.getTestCaseId() != null && !testGroupingRequest.getTestCaseId().isEmpty()) {
            for (Long testCaseId : testGroupingRequest.getTestCaseId()
            ) {
                TestCases testCases = testCasesRepository.findById(testCaseId).get();
                testCasesList.add(testCases);
            }
        }
        List<TestScenarios> testScenariosList = new ArrayList<>();
        if (testGroupingRequest.getTestScenarioIds() != null && !testGroupingRequest.getTestScenarioIds().isEmpty()) {
            for (Long testScenarioId : testGroupingRequest.getTestScenarioIds()) {
                TestScenarios testScenarios = testScenarioRepository.findById(testScenarioId).get();
                testScenariosList.add(testScenarios);
            }
        }
        testGrouping.setTestScenarios(testScenariosList);
        testGrouping.setTestCases(testCasesList);
        if (testGroupingRequest.getId() != null) {
            testGroupingRepository.deleteById(testGroupingRequest.getId());
        }
        testGroupingRepository.save(testGrouping);
    }


    @Override
    public boolean allTestCasesInSameProject(List<Long> testCaseIds) {
        Set<Long> uniqueProjectIds = new HashSet<>();
        for (Long testCaseId : testCaseIds) {
            Long projectId = testCasesRepository.findById(testCaseId).get().getSubModule().getMainModule().getModules().getProject().getId();
            if (!uniqueProjectIds.contains(projectId)) {
                uniqueProjectIds.add(projectId);
            }
        }
        return uniqueProjectIds.size() == 1;
    }

    @Override
    public boolean existsByTestGroupingId(Long testGroupingId) {
        return testGroupingRepository.existsById(testGroupingId);
    }

    @Override
    public boolean existsTestGroupingByTestScenarioId(Long id) {
        return testGroupingRepository.existsByTestScenariosId(id);

    }

    @Override
    public void deleteTestGroupingById(Long testGroupingId) {
        testGroupingRepository.deleteById(testGroupingId);
    }

    @Override
    public boolean existsByTestCasesId(Long testCaseId) {
        return testGroupingRepository.existsByTestCasesId(testCaseId);
    }

    @Override
    public boolean existsByTestTypesId(Long testTypeId) {
        return testGroupingRepository.existsByTestTypeId(testTypeId);
    }

    @Override
    public TestGroupingResponse getTestGroupingById(Long id) {
        TestGrouping testGrouping = testGroupingRepository.findById(id).get();
        TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
        BeanUtils.copyProperties(testGrouping, testGroupingResponse);
        testGroupingResponse.setTestTypeName(testGrouping.getTestType().getName());
        List<String> testCaseNames = new ArrayList<>();
        List<String> subModuleName = new ArrayList<>();
        List<String> mainModulesName = new ArrayList<>();
        List<String> modulesName = new ArrayList<>();
        for (TestCases testCase : testGrouping.getTestCases()) {
            testCaseNames.add(testCase.getName());
            subModuleName.add(testCase.getSubModule().getName());
            mainModulesName.add(testCase.getSubModule().getMainModule().getName());
            modulesName.add(testCase.getSubModule().getMainModule().getModules().getName());
        }
        testGroupingResponse.setTestCaseName(testCaseNames);
        testGroupingResponse.setSubModuleName(subModuleName);
        testGroupingResponse.setMainModuleName(mainModulesName);
        testGroupingResponse.setModuleName(modulesName);
        return testGroupingResponse;
    }

    @Override
    public boolean existByProjectId(Long projectId) {
        return testGroupingRepository.existsByTestCases_SubModule_MainModule_Modules_ProjectId(projectId);
    }

    @Override
    public List<TestGroupingResponse> getAllTestGroupingByProjectId(Pageable pageable, PaginatedContentResponse.Pagination pagination, Long projectId) {
        List<TestGroupingResponse> testGroupingResponseList = new ArrayList<>();
        Page<TestGrouping> testGroupingPage = testGroupingRepository.findDistinctTestGroupingByTestCases_SubModule_MainModule_Modules_Project_Id(pageable, projectId);
        List<String> testCaseNames = new ArrayList<>();
        List<String> subModuleName = new ArrayList<>();
        List<String> mainModulesName = new ArrayList<>();
        List<String> modulesName = new ArrayList<>();
        pagination.setTotalRecords(testGroupingPage.getTotalElements());
        pagination.setPageSize(testGroupingPage.getTotalPages());
        for (TestGrouping testGrouping : testGroupingPage) {
            TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
            testGroupingResponse.setTestTypeName(testGrouping.getTestType().getName());
            testGroupingResponse.setName(testGrouping.getName());
            testGroupingResponse.setId(testGrouping.getId());
            for (TestCases testCases : testGrouping.getTestCases()
            ) {
                testCaseNames.add(testCases.getName());
                subModuleName.add(testCases.getSubModule().getName());
                mainModulesName.add(testCases.getSubModule().getMainModule().getName());
                modulesName.add(testCases.getSubModule().getMainModule().getModules().getName());
            }
            testGroupingResponse.setTestCaseName(testCaseNames);
            testGroupingResponse.setSubModuleName(subModuleName);
            testGroupingResponse.setMainModuleName(mainModulesName);
            testGroupingResponse.setModuleName(modulesName);
            testGroupingResponse.setSubModuleName(subModuleName);
            testGroupingResponseList.add(testGroupingResponse);
        }


        return testGroupingResponseList;
    }

    @Override
    public void updateTestGroupingExecutionStatus(Long testGroupingId, Long projectId, List<Long> testScenarioIds, List<Long> testCaseIds) {
        TestGrouping testGrouping = testGroupingRepository.findById(testGroupingId).orElse(null);
        testGrouping.setExecutionStatus(true);
        testGroupingRepository.save(testGrouping);
        if (testScenarioIds != null && !testScenarioIds.isEmpty()) {
            for (Long testScenarioId : testScenarioIds
            ) {
                TestScenarios testScenarios = testScenarioRepository.findById(testScenarioId).get();
                testScenarios.setExecutionStatus(true);
                testScenarioRepository.save(testScenarios);
            }
        }
        if (testCaseIds != null && !testCaseIds.isEmpty()) {
            for (Long testCaseId : testCaseIds
            ) {
                TestCases testCases = testCasesRepository.findById(testCaseId).get();
                testCases.setExecutionStatus(true);
                testCasesRepository.save(testCases);
            }
        }
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
    public boolean existsByTestGroupingNameByProjectId(String name, Long projectId) {
        return testGroupingRepository.existsByNameIgnoreCaseAndTestCases_SubModule_MainModule_Modules_Project_Id(name, projectId);
    }

    @Override
    public boolean isUpdateTestGroupingNameByProjectId(String name, Long projectId, Long groupingId) {
        return testGroupingRepository.existsByNameIgnoreCaseAndTestCases_SubModule_MainModule_Modules_Project_IdAndIdNot(name, projectId, groupingId);
    }

    @Override
    public boolean hasExcelPath(Long testGroupingId) {
        TestGrouping testGrouping = testGroupingRepository.findById(testGroupingId).get();
        if (testGrouping.getExcelFilePath() != null) return true;
        return false;

    }

    @Override
    public List<TestGroupingResponse> getAllTestGroupingByTestCaseId(Long testCaseId) {
        List<TestGroupingResponse> testGroupingResponseList = new ArrayList<>();
        List<TestGrouping> testGroupingList = testGroupingRepository.findAllTestGroupingByTestCasesId(testCaseId);

        for (TestGrouping testGrouping : testGroupingList) {
            TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
            BeanUtils.copyProperties(testGrouping, testGroupingResponse);
            testGroupingResponse.setTestTypeName(testGrouping.getTestType().getName());
            List<String> testCaseNames = new ArrayList<>();
            List<String> subModuleName = new ArrayList<>();
            List<String> mainModulesName = new ArrayList<>();
            List<String> modulesName = new ArrayList<>();
            for (TestCases testCase : testGrouping.getTestCases()) {
                testCaseNames.add(testCase.getName());
                subModuleName.add(testCase.getSubModule().getName());
                mainModulesName.add(testCase.getSubModule().getMainModule().getName());
                modulesName.add(testCase.getSubModule().getMainModule().getModules().getName());
            }
            testGroupingResponse.setTestCaseName(testCaseNames);
            testGroupingResponse.setSubModuleName(subModuleName);
            testGroupingResponse.setMainModuleName(mainModulesName);
            testGroupingResponse.setModuleName(modulesName);
            testGroupingResponseList.add(testGroupingResponse);
        }
        return testGroupingResponseList;
    }

    @Override
    public List<TestGroupingResponse> getAllTestGroupingByTestTypeId(Long testTypeId) {
        List<TestGroupingResponse> testGroupingResponseList = new ArrayList<>();
        List<TestGrouping> testGroupingList = testGroupingRepository.findAllTestGroupingByTestTypeId(testTypeId);
        for (TestGrouping testGrouping : testGroupingList) {
            TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
            testGroupingResponse.setTestTypeName(testGrouping.getTestType().getName());
            List<TestCases> testCasesList = testGrouping.getTestCases();
            List<String> subModuleNameList = new ArrayList<>();
            List<String> mainMooduleNameList = new ArrayList<>();
            List<String> moduleNameList = new ArrayList<>();
            List<String> testCaseNameList = new ArrayList<>();

            for (TestCases testCases : testCasesList) {
                testCaseNameList.add(testCases.getName());
                subModuleNameList.add(testCases.getSubModule().getName());
                mainMooduleNameList.add(testCases.getSubModule().getMainModule().getName());
                moduleNameList.add(testCases.getSubModule().getMainModule().getModules().getName());

            }
            BeanUtils.copyProperties(testGrouping, testGroupingResponse);
            testGroupingResponse.setTestCaseName(testCaseNameList);
            testGroupingResponse.setSubModuleName(subModuleNameList);
            testGroupingResponse.setMainModuleName(mainMooduleNameList);
            testGroupingResponse.setModuleName(moduleNameList);
            testGroupingResponseList.add(testGroupingResponse);

        }
        return testGroupingResponseList;

    }

    @Override
    public List<TestGroupingResponse> multiSearchTestGrouping(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestGroupingSearch testGroupingSearch) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Utils.isNotNullAndEmpty(testGroupingSearch.getName())) {
            booleanBuilder.and(QTestGrouping.testGrouping.name.containsIgnoreCase(testGroupingSearch.getName()));
        }

        if (Utils.isNotNullAndEmpty(testGroupingSearch.getTestTypeName())) {
            booleanBuilder.and(QTestGrouping.testGrouping.testType.name.containsIgnoreCase(testGroupingSearch.getTestTypeName()));
        }
        List<TestGroupingResponse> testGroupingResponseList = new ArrayList<>();
        Page<TestGrouping> testGroupingPage = testGroupingRepository.findAll(booleanBuilder, pageable);
        pagination.setTotalRecords(testGroupingPage.getTotalElements());
        pagination.setPageSize(testGroupingPage.getTotalPages());
        for (TestGrouping testGrouping : testGroupingPage) {
            TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
            testGroupingResponse.setTestTypeName(testGrouping.getTestType().getName());
            List<TestCases> testCasesList = testGrouping.getTestCases();
            List<String> subModuleNameList = new ArrayList<>();
            List<String> mainMooduleNameList = new ArrayList<>();
            List<String> moduleNameList = new ArrayList<>();
            List<String> testCaseNameList = new ArrayList<>();
            for (TestCases testCases : testCasesList) {

                testCaseNameList.add(testCases.getName());
                subModuleNameList.add(testCases.getSubModule().getName());
                mainMooduleNameList.add(testCases.getSubModule().getMainModule().getName());
                moduleNameList.add(testCases.getSubModule().getMainModule().getModules().getName());
            }
            BeanUtils.copyProperties(testGrouping, testGroupingResponse);
            testGroupingResponse.setTestCaseName(testCaseNameList);
            testGroupingResponse.setSubModuleName(subModuleNameList);
            testGroupingResponse.setMainModuleName(mainMooduleNameList);
            testGroupingResponse.setModuleName(moduleNameList);
            testGroupingResponseList.add(testGroupingResponse);
        }
        return testGroupingResponseList;
    }
}
