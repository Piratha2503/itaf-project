package com.ii.testautomation.service.impl;
import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.dto.response.TestGroupingResponse;
import com.ii.testautomation.dto.search.TestGroupingSearch;
import com.ii.testautomation.entities.TestCases;
import com.ii.testautomation.entities.TestGrouping;
import com.ii.testautomation.entities.TestTypes;
import com.ii.testautomation.repositories.ProjectRepository;
import com.ii.testautomation.entities.QTestGrouping;
import com.ii.testautomation.repositories.TestCasesRepository;
import com.ii.testautomation.repositories.TestGroupingRepository;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.TestGroupingService;
import com.ii.testautomation.utils.Utils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            testGroupingResponse.setSubModuleName(subModuleName);
            testGroupingResponse.setMainModuleName(mainModulesName);
            testGroupingResponse.setModuleName(modulesName);
            testGroupingResponse.setSubModuleName(subModuleName);
            testGroupingResponseList.add(testGroupingResponse);
        }

        return testGroupingResponseList;

    }

    @Override
    public void updateTestGroupingExecutionStatus(Long testGroupingId) {
        TestGrouping testGrouping=testGroupingRepository.findById(testGroupingId).get();
        testGrouping.setExecutionStatus(true);
        testGroupingRepository.save(testGrouping);
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
            BeanUtils.copyProperties(testGrouping,testGroupingResponse);
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
        for (TestGrouping testGrouping:testGroupingPage) {
            TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
            testGroupingResponse.setTestTypeName(testGrouping.getTestType().getName());
            List<TestCases> testCasesList = testGrouping.getTestCases();
            List<String> subModuleNameList = new ArrayList<>();
            List<String> mainMooduleNameList = new ArrayList<>();
            List<String> moduleNameList = new ArrayList<>();
            List<String> testCaseNameList = new ArrayList<>();
            for (TestCases testCases : testCasesList)
            {

                testCaseNameList.add(testCases.getName());
                subModuleNameList.add(testCases.getSubModule().getName());
                mainMooduleNameList.add(testCases.getSubModule().getMainModule().getName());
                moduleNameList.add(testCases.getSubModule().getMainModule().getModules().getName());
            }
            BeanUtils.copyProperties(testGrouping,testGroupingResponse);
            testGroupingResponse.setTestCaseName(testCaseNameList);
            testGroupingResponse.setSubModuleName(subModuleNameList);
            testGroupingResponse.setMainModuleName(mainMooduleNameList);
            testGroupingResponse.setModuleName(moduleNameList);
            testGroupingResponseList.add(testGroupingResponse);
        }
        return testGroupingResponseList;
    }

}
