package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.TestCaseRequest;
import com.ii.testautomation.dto.response.TestCaseResponse;
import com.ii.testautomation.dto.search.TestCaseSearch;
import com.ii.testautomation.entities.QTestCases;
import com.ii.testautomation.entities.SubModules;
import com.ii.testautomation.entities.TestCases;
import com.ii.testautomation.repositories.TestCasesRepository;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.TestCasesService;
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
public class TestCasesServiceImpl implements TestCasesService {

    @Autowired
    private TestCasesRepository testCasesRepository;

    @Override
    public void saveTestCase(TestCaseRequest testCaseRequest) {
        TestCases testCases = new TestCases();
        SubModules subModules = new SubModules();
        subModules.setId(testCaseRequest.getSubModuleId());
        testCases.setSubModule(subModules);
        BeanUtils.copyProperties(testCaseRequest, testCases);
        testCasesRepository.save(testCases);
    }

    @Override
    public boolean existsByTestCasesId(Long id) {
        return testCasesRepository.existsById(id);
    }

    @Override
    public boolean existsByTestCasesName(String testCaseName) {
        return testCasesRepository.existsByNameIgnoreCase(testCaseName);
    }

    @Override
    public TestCaseResponse getById(Long id) {
        TestCaseResponse testCaseResponse = new TestCaseResponse();
        TestCases testCases = testCasesRepository.findById(id).get();
        testCaseResponse.setSubModuleId(testCases.getSubModule().getId());
        testCaseResponse.setSubModuleName(testCases.getSubModule().getName());
        BeanUtils.copyProperties(testCases, testCaseResponse);

        return testCaseResponse;
    }

    @Override
    public boolean isUpdateTestCaseNameExists(Long id, String name) {
        return testCasesRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }

    @Override
    public List<TestCaseResponse> multiSearchTestCase(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestCaseSearch testCaseSearch) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Utils.isNotNullAndEmpty(testCaseSearch.getName())) {
            booleanBuilder.and(QTestCases.testCases.name.eq(testCaseSearch.getName()));
        }
        if (Utils.isNotNullAndEmpty(testCaseSearch.getSubModuleName())) {
            booleanBuilder.and(QTestCases.testCases.subModule.name.eq(testCaseSearch.getSubModuleName()));
        }
        List<TestCaseResponse> testCaseResponseList = new ArrayList<>();
        Page<TestCases> testCasesPage = testCasesRepository.findAll(booleanBuilder, pageable);
        pagination.setTotalRecords(testCasesPage.getTotalElements());
        pagination.setPageSize(testCasesPage.getTotalPages());
        for (TestCases testcases : testCasesPage) {
            TestCaseResponse testCaseResponse = new TestCaseResponse();
            testCaseResponse.setSubModuleId(testcases.getSubModule().getId());
            testCaseResponse.setSubModuleName(testcases.getSubModule().getName());
            BeanUtils.copyProperties(testcases, testCaseResponse);
            testCaseResponseList.add(testCaseResponse);
        }
        return testCaseResponseList;
    }

    @Override
    public List<TestCaseResponse> getAllTestCaseBySubModuleId(Long subModuleId) {
        List<TestCaseResponse> testCaseResponseList = new ArrayList<>();
        List<TestCases> testCasesList = testCasesRepository.findAllTestCasesBySubModuleId(subModuleId);
        for (TestCases testCases : testCasesList) {
            TestCaseResponse testCaseResponse = new TestCaseResponse();
            testCaseResponse.setSubModuleId(testCases.getSubModule().getId());
            testCaseResponse.setSubModuleName(testCases.getSubModule().getName());
            BeanUtils.copyProperties(testCases, testCaseResponse);
            testCaseResponseList.add(testCaseResponse);
        }
        return testCaseResponseList;
    }

    @Override
    public void DeleteTestCaseById(Long id) {
        testCasesRepository.deleteById(id);
    }
}
