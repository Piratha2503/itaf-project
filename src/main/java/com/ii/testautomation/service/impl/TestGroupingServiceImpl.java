package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.dto.response.TestGroupingResponse;
import com.ii.testautomation.dto.search.TestGroupingSearch;
import com.ii.testautomation.entities.QTestGrouping;
import com.ii.testautomation.entities.TestCases;
import com.ii.testautomation.entities.TestGrouping;
import com.ii.testautomation.entities.TestTypes;
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
import java.util.List;

@Service
public class TestGroupingServiceImpl implements TestGroupingService {
    @Autowired
    private TestGroupingRepository testGroupingRepository;

    @Override
    public void saveTestGrouping(TestGroupingRequest testGroupingRequest) {
        TestGrouping testGrouping = new TestGrouping();
        TestCases testCases = new TestCases();
        testCases.setId(testGroupingRequest.getTestCaseId());
        testGrouping.setTestCases(testCases);
        TestTypes testTypes = new TestTypes();
        testTypes.setId(testGroupingRequest.getTestTypeId());
        testGrouping.setTestType(testTypes);
        BeanUtils.copyProperties(testGroupingRequest, testGrouping);
        testGroupingRepository.save(testGrouping);
    }

    @Override
    public boolean existsByTestGroupingName(String testGroupingName) {
        return testGroupingRepository.existsByNameIgnoreCase(testGroupingName);
    }

    @Override
    public boolean existsByTestGroupingId(Long testGroupingId) {
        return testGroupingRepository.existsById(testGroupingId);
    }

    @Override
    public boolean isUpdateTestGroupingNameExits(String testGroupingName, Long testGroupingId) {
        return testGroupingRepository.existsByNameIgnoreCaseAndIdNot(testGroupingName, testGroupingId);
    }

    @Override
    public TestGroupingResponse getTestGroupingById(Long testGroupingId) {
        TestGrouping testGrouping = testGroupingRepository.findById(testGroupingId).get();
        TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
        testGroupingResponse.setTestCasesName(testGrouping.getTestCases().getName());
        testGroupingResponse.setTestTypesName(testGrouping.getTestType().getName());
        testGroupingResponse.setSubModuleName(testGrouping.getTestCases().getSubModule().getName());
        testGroupingResponse.setMainModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getName());
        testGroupingResponse.setModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getModule().getName());
        BeanUtils.copyProperties(testGrouping, testGroupingResponse);
        return testGroupingResponse;
    }

    @Override
    public List<TestGroupingResponse> getALlTestGroupingByTestCaseId(Long testCaseId) {
        List<TestGrouping> testGroupingList = testGroupingRepository.findAllTestGroupingByTestCasesId(testCaseId);
        List<TestGroupingResponse> testGroupingResponses = new ArrayList<>();
        for (TestGrouping testGrouping : testGroupingList
        ) {
            TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
            testGroupingResponse.setTestCasesName(testGrouping.getTestCases().getName());
            testGroupingResponse.setTestTypesName(testGrouping.getTestType().getName());
            testGroupingResponse.setSubModuleName(testGrouping.getTestCases().getSubModule().getName());
            testGroupingResponse.setMainModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getName());
            testGroupingResponse.setModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getModule().getName());
            BeanUtils.copyProperties(testGrouping, testGroupingResponse);
            testGroupingResponses.add(testGroupingResponse);
        }
        return testGroupingResponses;
    }

    @Override
    public List<TestGroupingResponse> getALlTestGroupingByTestTypeId(Long testTypeId) {
        List<TestGrouping> testGroupingList = testGroupingRepository.findAllTestGroupingByTestTypeId(testTypeId);
        List<TestGroupingResponse> testGroupingResponses = new ArrayList<>();
        for (TestGrouping testGrouping : testGroupingList
        ) {
            TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
            testGroupingResponse.setTestCasesName(testGrouping.getTestCases().getName());
            testGroupingResponse.setTestTypesName(testGrouping.getTestType().getName());
            testGroupingResponse.setSubModuleName(testGrouping.getTestCases().getSubModule().getName());
            testGroupingResponse.setMainModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getName());
            testGroupingResponse.setModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getModule().getName());
            BeanUtils.copyProperties(testGrouping, testGroupingResponse);
            testGroupingResponses.add(testGroupingResponse);
        }
        return testGroupingResponses;
    }

    @Override
    public List<TestGroupingResponse> multiSearchTestGrouping(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestGroupingSearch testGroupingSearch) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Utils.isNotNullAndEmpty(testGroupingSearch.getName())) {
            booleanBuilder.and(QTestGrouping.testGrouping.name.eq(testGroupingSearch.getName()));
        }
        if (Utils.isNotNullAndEmpty(testGroupingSearch.getTestCaseName())) {
            booleanBuilder.and(QTestGrouping.testGrouping.testCases.name.eq(testGroupingSearch.getTestCaseName()));
        }
        if (Utils.isNotNullAndEmpty(testGroupingSearch.getTestTypeName())) {
            booleanBuilder.and(QTestGrouping.testGrouping.testType.name.eq(testGroupingSearch.getTestTypeName()));
        }
        List<TestGroupingResponse> testGroupingResponses = new ArrayList<>();
        Page<TestGrouping> testGroupings = testGroupingRepository.findAll(booleanBuilder, pageable);

        pagination.setTotalRecords(testGroupings.getTotalElements());
        pagination.setPageSize(testGroupings.getTotalPages());
        for (TestGrouping testGrouping : testGroupings
        ) {
            TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
            testGroupingResponse.setTestCasesName(testGrouping.getTestCases().getName());
            testGroupingResponse.setTestTypesName(testGrouping.getTestType().getName());
            testGroupingResponse.setSubModuleName(testGrouping.getTestCases().getSubModule().getName());
            testGroupingResponse.setMainModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getName());
            testGroupingResponse.setModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getModule().getName());
            BeanUtils.copyProperties(testGrouping, testGroupingResponse);
            testGroupingResponses.add(testGroupingResponse);
        }
        return testGroupingResponses;
    }
    @Override
    public void deleteTestGroupingById(Long testGroupingId) {
        testGroupingRepository.deleteById(testGroupingId);
    }

}
