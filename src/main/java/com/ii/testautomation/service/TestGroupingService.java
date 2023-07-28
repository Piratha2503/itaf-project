package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.dto.response.SubModulesResponse;
import com.ii.testautomation.dto.response.TestGroupingResponse;
import com.ii.testautomation.dto.search.TestGroupingSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

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

    public boolean existsByTestCasesId(Long testCaseId);

    public boolean existsByTestTypesId(Long testTypeId);

    Map<Integer, TestGroupingRequest> csvToTestGroupingRequest(InputStream inputStream);

    Map<Integer, TestGroupingRequest> excelToTestGroupingRequest(MultipartFile multipartFile);

    public boolean hasExcelFormat(MultipartFile multipartFile);

    boolean hasCsvFormat(MultipartFile multipartFile);

    void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value);

    public boolean isExcelHeaderMatch(MultipartFile multipartFile);

    public boolean isCSVHeaderMatch(MultipartFile multipartFile);
    List<TestGroupingResponse> getTestGroupingByProjectId(Long id);
}
