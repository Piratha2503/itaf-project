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

    void saveTestTypes(TestTypesRequest testTypesRequest);

    void deleteTestTypeById(Long id);

    TestTypesResponse getTestTypeById(Long id);

    List<TestTypesResponse> SearchTestTypesWithPagination(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestTypesSearch testTypesSearch);

    boolean isExistsTestTypeByName(String name);

    boolean isExistsTestTypeById(Long id);

    boolean isExistsTestTypesByNameIgnoreCaseAndIdNot(String name, Long id);

    boolean existsByTestTypesId(Long id);

    boolean hasExcelFormat(MultipartFile multipartFile);

    List<TestTypesRequest> csvProcess(InputStream inputStream);

    List<TestTypesRequest> excelProcess(MultipartFile multipartFile);

    void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value);

    boolean isExcelHeaderMatch(MultipartFile multipartFile);

    boolean isCSVHeaderMatch(MultipartFile multipartFile);
}
