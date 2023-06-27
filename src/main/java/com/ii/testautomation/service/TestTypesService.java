package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.TestTypesRequest;

public interface TestTypesService
{
    // CRUD Functions
    public void saveTestTypes(TestTypesRequest testTypesRequest);


    // Checking Functions
    public boolean isExistsTestTypeByName(String name);
    public boolean isExistsTestTypeById(Long id);
    public boolean isExistsTestTypesByNameIgnoreCaseAndIdNot(String name, Long id);
}
