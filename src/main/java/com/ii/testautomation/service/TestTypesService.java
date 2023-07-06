package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.TestTypesRequest;
import com.ii.testautomation.dto.response.TestTypesResponse;
import com.ii.testautomation.dto.search.TestTypesSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TestTypesService
{
    // CRUD Functions
    public void saveTestTypes(TestTypesRequest testTypesRequest);
    public void deleteTestTypeById(Long id);

    // Get Functions
    public TestTypesResponse getTestTypeById(Long id);
    public List<TestTypesResponse> SearchTestTypesWithPagination(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestTypesSearch testTypesSearch);

     // Checking Functions
    public boolean isExistsTestTypeByName(String name);
    public boolean isExistsTestTypeById(Long id);
    public boolean isExistsTestTypesByNameIgnoreCaseAndIdNot(String name, Long id);
    public boolean existsByTestTypesId(Long id);

    // Bulk Import
    public boolean hasExcelFormat(MultipartFile multipartFile);
    public List<TestTypesRequest> csvProcess(InputStream inputStream);
    public List<TestTypesRequest> excelProcess(MultipartFile multipartFile);
    public void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value);

}
