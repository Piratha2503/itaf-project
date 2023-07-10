package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.TestCaseRequest;
import com.ii.testautomation.dto.response.TestCaseResponse;
import com.ii.testautomation.dto.search.TestCaseSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface TestCasesService {
    public void saveTestCase(TestCaseRequest testCaseRequest);

    public boolean existsByTestCasesId(Long id);

    public boolean existsByTestCasesName(String testCaseName);

    public TestCaseResponse getById(Long id);

    public boolean isUpdateTestCaseNameExists(Long id, String name);

    public List<TestCaseResponse> multiSearchTestCase(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestCaseSearch testCaseSearch);

    public List<TestCaseResponse> getAllTestCaseBySubModuleId(Long subModuleId);

    public void DeleteTestCaseById(Long id);

    public boolean existsBySubModuleId(Long subModuleId);
    public boolean hasExcelFormat(MultipartFile multipartFile);
    public List<TestCaseRequest> csvToTestCaseRequest(InputStream inputStream);
    public List<TestCaseRequest> excelToTestCaseRequest(MultipartFile multipartFile);

    void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value);

}
