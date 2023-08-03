package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.dto.search.TestGroupingSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface TestGroupingService {
    void saveTestGrouping(TestGroupingRequest testGroupingRequest);

    public boolean existsByTestGroupingName(String testGroupingName,Long testCaseId);

    boolean existsByTestGroupingId(Long testGroupingId);

    boolean isUpdateTestGroupingNameExits(String testGroupingName,Long testCaseId ,Long testGroupingId);

     TestGroupingResponse getTestGroupingById(Long testGroupingId);

    List<TestGroupingResponse> getALlTestGroupingByTestCaseId(Long testCaseId);

    List<TestGroupingResponse> getALlTestGroupingByTestTypeId(Long testTypeId);

    List<TestGroupingResponse> multiSearchTestGrouping(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestGroupingSearch testGroupingSearch);

    void deleteTestGroupingById(Long testGroupingId);

    boolean existsByTestCasesId(Long testCaseId);
  boolean allTestCasesInSameProject(List<Long> testCaseIds);

    boolean existsByTestTypesId(Long testTypeId);

    Map<Integer, TestGroupingRequest> csvToTestGroupingRequest(InputStream inputStream);

    Map<Integer, TestGroupingRequest> excelToTestGroupingRequest(MultipartFile multipartFile);

    boolean hasExcelFormat(MultipartFile multipartFile);

    boolean hasCsvFormat(MultipartFile multipartFile);

    void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value);

    boolean isExcelHeaderMatch(MultipartFile multipartFile);

    boolean isCSVHeaderMatch(MultipartFile multipartFile);

    List<TestGroupingResponse> getTestGroupingByProjectId(Long id);

    boolean existsByProjectId(Long projectId);
}
