package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.TestTypesRequest;
import com.ii.testautomation.dto.response.TestTypesResponse;
import com.ii.testautomation.dto.search.TestTypesSearch;
import com.ii.testautomation.entities.QTestTypes;
import com.ii.testautomation.entities.TestTypes;
import com.ii.testautomation.repositories.TestTypesRepository;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.TestTypesService;
import com.ii.testautomation.utils.Utils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
@Service
public class TestTypesServiceImpl implements TestTypesService
{
    @Autowired
    private TestTypesRepository testTypesRepository;

    // CRUD
    @Override
    public void saveTestTypes(TestTypesRequest testTypesRequest)
    {
        TestTypes testTypes = new TestTypes();
        BeanUtils.copyProperties(testTypesRequest,testTypes);
        testTypesRepository.save(testTypes);
    }

    @Override
    public void deleteTestTypeById(Long id)
    {
        testTypesRepository.deleteById(id);
    }

    @Override
    public TestTypesResponse getTestTypeById(Long id)
    {
        TestTypes testTypes = testTypesRepository.findById(id).get();
        TestTypesResponse testTypesResponse = new TestTypesResponse();
        BeanUtils.copyProperties(testTypes,testTypesResponse);
        return testTypesResponse;
    }

    @Override
    public List<TestTypesResponse> SearchTestTypesWithPagination(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestTypesSearch testTypesSearch)
    {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Utils.isNotNullAndEmpty(testTypesSearch.getName()))
        {
            booleanBuilder.and(QTestTypes.testTypes.name.eq(testTypesSearch.getName()));
        }
        List<TestTypesResponse> testTypesResponseList = new ArrayList<>();
        Page<TestTypes> testTypesPage = testTypesRepository.findAll(booleanBuilder, pageable);

        pagination.setTotalRecords(testTypesPage.getTotalElements());
        pagination.setPageSize(testTypesPage.getTotalPages());
        for (TestTypes testTypes : testTypesPage)
        {
            TestTypesResponse testTypesResponse = new TestTypesResponse();
            BeanUtils.copyProperties(testTypes, testTypesResponse);
            testTypesResponseList.add(testTypesResponse);
        }
        return testTypesResponseList;
    }

    @Override
    @Transactional

    public void importfromFile(MultipartFile file) throws IOException {
     try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream())))
        {
            String line;
        while ((line = reader.readLine()) != null)
            {
                String[] data = line.split(",");
                TestTypes testTypes = new TestTypes();
                testTypes.setName(data[1]);
                testTypes.setDescription(data[2]);
                testTypesRepository.save(testTypes);
            }
        }
    }

    // Check
    @Override
    public boolean isExistsTestTypeByName(String name) {return testTypesRepository.existsByName(name);    }
    @Override
    public boolean isExistsTestTypeById(Long id) {return testTypesRepository.existsById(id);}
    @Override
    public boolean isExistsTestTypesByNameIgnoreCaseAndIdNot(String name, Long id) {
        return testTypesRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }

    @Override
    public boolean existsByTestTypesId(Long id) {
        return testTypesRepository.existsById(id);
    }

}
