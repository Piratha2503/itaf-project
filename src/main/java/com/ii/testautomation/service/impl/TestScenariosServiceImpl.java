package com.ii.testautomation.service.impl;


import com.ii.testautomation.dto.response.TestScenariosResponse;
import com.ii.testautomation.entities.TestCases;
import com.ii.testautomation.entities.TestScenarios;
import com.ii.testautomation.repositories.TestScenariosRepository;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.TestScenariosService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class TestScenariosServiceImpl implements TestScenariosService {
    @Autowired
    private TestScenariosRepository testScenariosRepository;

    @Override
    public boolean existsByTestScenarioId(Long testScenarioId) {
        return testScenariosRepository.existsById(testScenarioId);
    }

    @Override
    public boolean existsByTestScenarioNameIgnoreCase(String name) {
        return false;
    }

    @Override
    public boolean existByProjectId(Long projectId) {
          return testScenariosRepository.existsByTestCasesSubModuleMainModuleModulesProject_id(projectId);

    }

    @Override
    public List<TestScenariosResponse> getAllTestScenariosByProjectIdWithPagination(Long projectId, Pageable pageable, PaginatedContentResponse.Pagination pagination)      {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        List<TestScenariosResponse> testScenariosResponseList = new ArrayList<>();
        Page<TestScenarios> testScenariosPage = testScenariosRepository.findByTestCasesSubModuleMainModuleModulesProjectId(projectId,pageable);
        pagination.setTotalRecords(testScenariosPage.getTotalElements());
        pagination.setPageSize(testScenariosPage.getTotalPages());

        for (TestScenarios testScenarios:testScenariosPage) {
            TestScenariosResponse testScenariosResponse=new TestScenariosResponse();
            testScenariosResponse.setId(testScenarios.getId());
            testScenariosResponse.setName(testScenarios.getName());
            List<String> testCasesNames = new ArrayList<>();
            for (TestCases testCase : testScenarios.getTestCases()) {
                String TestCaseName = testCase.getName().substring(testCase.getName().lastIndexOf(".") + 1);
                testCasesNames.add(TestCaseName);
            }
            testScenariosResponse.setTestCasesName(testCasesNames);
            testScenariosResponseList.add(testScenariosResponse);
        }
           return testScenariosResponseList;
        }

    @Override
    public void DeleteTestScenariosById(Long id) {
         testScenariosRepository.deleteById(id);
    }


}
