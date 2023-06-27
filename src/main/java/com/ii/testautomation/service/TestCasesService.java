package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.TestCaseRequest;
import com.ii.testautomation.dto.response.TestCaseResponse;
import com.ii.testautomation.dto.search.TestCaseSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TestCasesService {
    public void saveTestCase(TestCaseRequest testCaseRequest);

    public boolean existsByTestCasesId(Long id);

    public boolean existsByTestCasesName(String testCaseName);

    public TestCaseResponse getById(Long id);

    public boolean isUpdateTestCaseNameExists(Long id, String name);

    public List<TestCaseResponse> multiSearchTestCase(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestCaseSearch testCaseSearch);

    public List<TestCaseResponse> getAllTestCaseBySubModuleId(Long subModuleId);

    public void DeleteTestCaseById(Long id);
}
