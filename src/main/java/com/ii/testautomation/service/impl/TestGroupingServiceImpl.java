package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.dto.response.TestGroupingResponse;
import com.ii.testautomation.entities.SubModules;
import com.ii.testautomation.entities.TestCases;
import com.ii.testautomation.entities.TestGrouping;
import com.ii.testautomation.entities.TestTypes;
import com.ii.testautomation.repositories.ProjectRepository;
import com.ii.testautomation.repositories.TestCasesRepository;
import com.ii.testautomation.repositories.TestGroupingRepository;
import com.ii.testautomation.service.TestGroupingService;
import org.aspectj.weaver.ast.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TestGroupingServiceImpl implements TestGroupingService {
    @Autowired
    private TestGroupingRepository testGroupingRepository;
    @Autowired
    private TestCasesRepository testCasesRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public void saveTestGrouping(TestGroupingRequest testGroupingRequest) {
        TestGrouping testGrouping = new TestGrouping();
        List<TestCases> testCasesList = testGroupingRequest.getTestCaseId().stream()
                .map(testCaseId -> {
                    TestCases testCases = new TestCases();
                    testCases.setId(testCaseId);
                    return testCases;
                })
                .collect(Collectors.toList());
        testGrouping.setTestCases(testCasesList);
        TestTypes testTypes = new TestTypes();
        testTypes.setId(testGroupingRequest.getTestTypeId());
        testGrouping.setTestType(testTypes);
        BeanUtils.copyProperties(testGroupingRequest, testGrouping);
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
    public boolean existsByTestGroupingName(String testGroupingName, Long testCaseId) {
        Long projectId = testCasesRepository.findById(testCaseId).get().getSubModule().getMainModule().getModules().getProject().getId();
        return testGroupingRepository.existsByNameIgnoreCaseAndTestCases_SubModule_MainModule_Modules_Project_Id(testGroupingName, projectId);
    }

    @Override
    public boolean existsByTestGroupingId(Long testGroupingId) {
        return testGroupingRepository.existsById(testGroupingId);
    }

    @Override
    public boolean isUpdateTestGroupingNameExits(String testGroupingName, Long testCaseId, Long testGroupingId) {
        Long projectId = testCasesRepository.findById(testCaseId).get().getSubModule().getMainModule().getModules().getProject().getId();
        return testGroupingRepository.existsByNameIgnoreCaseAndTestCases_SubModule_MainModule_Modules_Project_IdAndIdNot(testGroupingName, projectId, testGroupingId);
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
//        testGroupingResponse.setSubModuleName(subModuleName);
//        testGroupingResponse.setMainModuleName(mainModulesName);
//        testGroupingResponse.setModuleName(modulesName);
        return testGroupingResponse;
    }

    @Override
    public boolean existsByTestGroupId(Long id) {
        return testGroupingRepository.existsById(id);
    }

    @Override
    public boolean existByProjectId(Long projectId) {
        return testGroupingRepository.existsByTestCases_SubModule_MainModule_Modules_ProjectId(projectId);
    }


    public List<TestGroupingResponse> getAllTestGroupingByProjectId(Long projectId) {
        List<TestGroupingResponse> testGroupingResponseList = new ArrayList<>();
        List<TestGrouping> testGroupings = testGroupingRepository.findDistinctTestGroupingByTestCases_SubModule_MainModule_Modules_Project_Id(projectId);
        List<String> testCaseNames = new ArrayList<>();
        List<String> subModuleName = new ArrayList<>();
        List<String> mainModulesName = new ArrayList<>();
        List<String> modulesName = new ArrayList<>();
        for (TestGrouping testGrouping:testGroupings) {
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
            testGroupingResponse.setMainModuleName(mainModulesName);
            testGroupingResponse.setModuleName(modulesName);
            testGroupingResponse.setSubModuleName(subModuleName);
            testGroupingResponseList.add(testGroupingResponse);
        }

        return testGroupingResponseList;

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
}
