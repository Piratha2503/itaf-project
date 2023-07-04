package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.TestTypesRequest;
import com.ii.testautomation.dto.response.TestTypesResponse;
import com.ii.testautomation.dto.search.TestTypesSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TestTypesService
{
    // CRUD Functions
    public void saveTestTypes(TestTypesRequest testTypesRequest);
    public void deleteTestTypeById(Long id);

    // Get Functions
    public TestTypesResponse getTestTypeById(Long id);
    public List<TestTypesResponse> SearchTestTypesWithPagination(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestTypesSearch testTypesSearch);

    //public void importfromFile(MultipartFile file) throws IOException;

    // Checking Functions
    public boolean isExistsTestTypeByName(String name);
    public boolean isExistsTestTypeById(Long id);
    public boolean isExistsTestTypesByNameIgnoreCaseAndIdNot(String name, Long id);
    public boolean existsByTestTypesId(Long id);
}
