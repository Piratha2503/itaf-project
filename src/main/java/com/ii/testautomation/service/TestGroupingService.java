package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.dto.response.TestGroupingResponse;
import com.ii.testautomation.entities.TestGrouping;
import com.ii.testautomation.dto.response.TestCaseResponse;
import com.ii.testautomation.dto.response.TestGroupingResponse;
import com.ii.testautomation.dto.search.TestGroupingSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TestGroupingService {
    void saveTestGrouping(TestGroupingRequest testGroupingRequest, List<Long> moduleIds, List<Long> subModuleIds, List<Long> mainModuleIds);
    boolean existsByTestGroupingNameModule(String name, Long moduleId);

    boolean existsByTestGroupingNameSubModule(String name, Long subModuleId);

    boolean existsByTestGroupingNameMainModule(String name, Long mainModuleId);
    boolean existsByTestGroupingName(String testGroupingName, Long testCaseId);

    boolean existsByTestGroupingId(Long testGroupingId);

    boolean isUpdateTestGroupingNameExits(String testGroupingName, Long testCaseId, Long testGroupingId);

    void deleteTestGroupingById(Long testGroupingId);

    boolean existsByTestCasesId(Long testCaseId);

    boolean allTestCasesInSameProject(List<Long> testCaseIds);

    boolean existsByTestTypesId(Long testTypeId);

    List<TestGroupingResponse> getAllTestGroupingByTestTypeId(Long testTypeId);

    List<TestGroupingResponse> multiSearchTestGrouping(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestGroupingSearch testGroupingSearch);

    List<TestGroupingResponse> getAllTestGroupingByTestCaseId(Long testCaseId);

    TestGroupingResponse getTestGroupingById(Long id);

    boolean existByProjectId(Long projectId);

    List<TestGroupingResponse> getAllTestGroupingByProjectId(Long projectId);

    void updateTestGroupingExecutionStatus(Long testGroupingId, Long projectId);


}
