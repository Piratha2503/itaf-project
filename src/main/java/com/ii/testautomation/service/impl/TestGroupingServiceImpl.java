package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.entities.TestCases;
import com.ii.testautomation.entities.TestGrouping;
import com.ii.testautomation.entities.TestTypes;
import com.ii.testautomation.repositories.TestCasesRepository;
import com.ii.testautomation.repositories.TestGroupingRepository;
import com.ii.testautomation.service.TestGroupingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
