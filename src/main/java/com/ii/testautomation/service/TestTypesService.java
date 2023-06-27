package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.TestTypesRequest;
import com.ii.testautomation.dto.response.TestTypesResponse;
import com.ii.testautomation.dto.search.TestTypesSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TestTypesService
{
    // CRUD Functions
    public void saveTestTypes(TestTypesRequest testTypesRequest);
    public void deleteTestTypeById(Long id);
    public TestTypesResponse getTestTypeById(Long id);

    // Search & Pagination
    public List<TestTypesResponse> SearchTestTypesWithPagination(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestTypesSearch testTypesSearch);


    // Checking Functions
    public boolean isExistsTestTypeByName(String name);
    public boolean isExistsTestTypeById(Long id);
    public boolean isExistsTestTypesByNameIgnoreCaseAndIdNot(String name, Long id);
}
