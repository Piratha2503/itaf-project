package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.dto.response.TestGroupingResponse;
import com.ii.testautomation.dto.search.TestGroupingSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TestGroupingService {
    void saveTestGrouping(TestGroupingRequest testGroupingRequest);

    public boolean existsByTestGroupingName(String testGroupingName);

    public boolean existsByTestGroupingId(Long testGroupingId);

    public boolean isUpdateTestGroupingNameExits(String testGroupingName, Long testGroupingId);

    public TestGroupingResponse getTestGroupingById(Long testGroupingId);

    public List<TestGroupingResponse> getALlTestGroupingByTestCaseId(Long testCaseId);

    public List<TestGroupingResponse> getALlTestGroupingByTestTypeId(Long testTypeId);

    public List<TestGroupingResponse> multiSearchTestGrouping(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestGroupingSearch testGroupingSearch);

    public void deleteTestGroupingById(Long testGroupingId);
}
