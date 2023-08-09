package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.dto.response.TestGroupingResponse;
import com.ii.testautomation.entities.TestGrouping;

import java.util.List;

public interface TestGroupingService {
    void saveTestGrouping(TestGroupingRequest testGroupingRequest);

    public boolean existsByTestGroupingName(String testGroupingName, Long testCaseId);

    boolean existsByTestGroupingId(Long testGroupingId);

    boolean isUpdateTestGroupingNameExits(String testGroupingName, Long testCaseId, Long testGroupingId);

    void deleteTestGroupingById(Long testGroupingId);

    boolean existsByTestCasesId(Long testCaseId);

    boolean allTestCasesInSameProject(List<Long> testCaseIds);

    boolean existsByTestTypesId(Long testTypeId);

    List<TestGroupingResponse> getAllTestGroupingByTestCaseId(Long testCaseId);

    public TestGroupingResponse getTestGroupingById(Long id);

    boolean existsByTestGroupId(Long id);
    boolean existByProjectId(Long projectId);
    public List<TestGroupingResponse> getAllTestGroupingByProjectId(Long projectId);
}
