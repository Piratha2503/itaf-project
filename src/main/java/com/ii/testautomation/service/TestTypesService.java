package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.TestTypesRequest;
import com.ii.testautomation.dto.response.TestTypesResponse;
import com.ii.testautomation.dto.search.TestTypesSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface TestTypesService {

    public void saveTestTypes(TestTypesRequest testTypesRequest);

    public void deleteTestTypeById(Long id);

    public TestTypesResponse getTestTypeById(Long id);

    public List<TestTypesResponse> SearchTestTypesWithPagination(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestTypesSearch testTypesSearch);

    public boolean isExistsTestTypeByName(String name);

    public boolean isExistsTestTypeById(Long id);

    public boolean isExistsTestTypesByNameIgnoreCaseAndIdNot(String name, Long id);

    public boolean existsByTestTypesId(Long id);

    public boolean hasExcelFormat(MultipartFile multipartFile);

    public List<TestTypesRequest> csvProcess(InputStream inputStream);

    public List<TestTypesRequest> excelProcess(MultipartFile multipartFile);

    public void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value);

    public boolean isExcelHeaderMatch(MultipartFile multipartFile);

    public boolean isCSVHeaderMatch(MultipartFile multipartFile);

    public boolean existsTestGroupingByTestTypeId(Long id);
}
