package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.TestScenariosRequest;
import com.ii.testautomation.dto.response.TestScenariosResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TestScenariosService {
    boolean existsByTestScenarioId(Long testScenarioId);


    boolean existsByTestScenarioNameIgnoreCase(String name);

    boolean existByProjectId(Long projectId);

    List<TestScenariosResponse> getAllTestScenariosByProjectIdWithPagination(Long projectId, Pageable pageable, PaginatedContentResponse.Pagination pagination);

    void DeleteTestScenariosById(Long id);

    boolean existByTestCaseList(TestScenariosRequest testScenariosRequest);

    void saveTestScenario(TestScenariosRequest testScenariosRequest);

    boolean isUpdateTestScenariosNameExists(Long id,String name );

    TestScenariosResponse viewScenarioById(Long id);
}
