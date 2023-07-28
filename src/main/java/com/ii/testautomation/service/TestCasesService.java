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
    void saveTestCase(TestCaseRequest testCaseRequest);

    boolean existsByTestCasesId(Long id);

    boolean existsByTestCasesName(String testCaseName);

    TestCaseResponse getById(Long id);

    boolean isUpdateTestCaseNameExists(Long id, String name);

    List<TestCaseResponse> multiSearchTestCase(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestCaseSearch testCaseSearch);

    List<TestCaseResponse> getAllTestCaseBySubModuleId(Long subModuleId);

    void DeleteTestCaseById(Long id);

    boolean existsBySubModuleId(Long subModuleId);

    boolean hasExcelFormat(MultipartFile multipartFile);

    Map<Integer,TestCaseRequest> csvToTestCaseRequest(InputStream inputStream);

    Map<Integer,TestCaseRequest> excelToTestCaseRequest(MultipartFile multipartFile);
    boolean isExcelHeaderMatch(MultipartFile multipartFile);
    boolean isCSVHeaderMatch(MultipartFile multipartFile);

    void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value);

}
