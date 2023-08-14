package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.dto.response.TestGroupingResponse;
import com.ii.testautomation.dto.search.TestGroupingSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TestGroupingService {
    void saveTestGrouping(TestGroupingRequest testGroupingRequest,List<MultipartFile> excelFiles);
     boolean hasExcelFormat(List<MultipartFile> multipartFiles);

    boolean allTestCasesInSameProject(List<Long> testCaseIds);

    boolean existsByTestGroupingId(Long testGroupingId);

    void deleteTestGroupingById(Long testGroupingId);

    boolean existsByTestCasesId(Long testCaseId);

    boolean existsByTestTypesId(Long testTypeId);

    List<TestGroupingResponse> getAllTestGroupingByTestTypeId(Long testTypeId);

    List<TestGroupingResponse> multiSearchTestGrouping(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestGroupingSearch testGroupingSearch);

    List<TestGroupingResponse> getAllTestGroupingByTestCaseId(Long testCaseId);

    TestGroupingResponse getTestGroupingById(Long id);

    boolean existByProjectId(Long projectId);

    List<TestGroupingResponse> getAllTestGroupingByProjectId(Pageable pageable, PaginatedContentResponse.Pagination pagination,Long projectId);

    void updateTestGroupingExecutionStatus(Long testGroupingId, Long projectId);


    boolean existsByTestGroupingNameByProjectId(String name, Long projectId);
    boolean isUpdateTestGroupingNameByProjectId(String name, Long projectId, Long groupingId);
}
