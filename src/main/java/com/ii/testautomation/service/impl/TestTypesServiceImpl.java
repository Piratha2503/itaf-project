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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            booleanBuilder.and(QTestTypes.testTypes.name.containsIgnoreCase(testTypesSearch.getName()));
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

    // Bulk Import
    @Override
    public boolean hasExcelFormat(MultipartFile multipartFile)
    {
        try {
            Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream());
            workbook.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public List<TestTypesRequest> csvProcess(InputStream inputStream)
    {
        List<TestTypesRequest> testTypesRequestList = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords)
            {
                TestTypesRequest testTypesRequest = new TestTypesRequest();
                testTypesRequest.setDescription(csvRecord.get("description"));
                testTypesRequest.setName(csvRecord.get("name"));
                testTypesRequestList.add(testTypesRequest);
            }

        } catch (IOException e)
        {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
        return testTypesRequestList;
    }
    @Override
    public List<TestTypesRequest> excelProcess(MultipartFile multipartFile)
    {
        List<TestTypesRequest> testTypesRequestList = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnMap = getColumnMap(headerRow);
            for (Row row : sheet)
            {
                if (row.getRowNum() == 0) continue;
                TestTypesRequest testTypesRequest = new TestTypesRequest();
                testTypesRequest.setName(getStringCellValue(row.getCell(columnMap.get("name"))));
                testTypesRequest.setDescription(getStringCellValue(row.getCell(columnMap.get("description"))));
                testTypesRequestList.add(testTypesRequest);
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
        return testTypesRequestList;
    }
    @Override
    public void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value)
    {
        List<Integer> errorList = errorMessages.getOrDefault(key, new ArrayList<>());
        errorList.add(value);
        errorMessages.put(key, errorList);
    }

    private Map<String, Integer> getColumnMap(Row headerRow) {
        Map<String, Integer> columnMap = new HashMap<>();

        for (Cell cell : headerRow) {
            String cellValue = cell.getStringCellValue().toLowerCase();
            int columnIndex = cell.getColumnIndex();
            columnMap.put(cellValue, columnIndex);
        }

        return columnMap;
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }

    private Long getLongCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        cell.setCellType(CellType.NUMERIC);
        return (long) cell.getNumericCellValue();
    }


}
