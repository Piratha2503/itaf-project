package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.TestTypesRequest;
import com.ii.testautomation.entities.TestTypes;
import com.ii.testautomation.repositories.TestTypesRepository;
import com.ii.testautomation.service.TestTypesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestTypesServiceImpl implements TestTypesService
{
    @Autowired
    private TestTypesRepository testTypesRepository;

    // CRUD
    public void saveTestTypes(TestTypesRequest testTypesRequest)
    {
        TestTypes testTypes = new TestTypes();
        BeanUtils.copyProperties(testTypesRequest,testTypes);
        testTypesRepository.save(testTypes);
    }




    // Check
    @Override
    public boolean isExistsTestTypeByName(String name)
    {
        return testTypesRepository.existsByName(name);
    }
    @Override
    public boolean isExistsTestTypeById(Long id) {return testTypesRepository.existsById(id);}
    @Override
    public boolean isExistsTestTypesByNameIgnoreCaseAndIdNot(String name, Long id) {
        return testTypesRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }


}
